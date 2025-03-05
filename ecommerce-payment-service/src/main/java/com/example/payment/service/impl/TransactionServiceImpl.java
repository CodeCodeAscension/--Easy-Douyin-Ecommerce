package com.example.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.api.client.OrderClient;
import com.example.api.client.ProductClient;
import com.example.api.domain.dto.payment.ChargeDto;
import com.example.api.domain.dto.product.DecProductDto;
import com.example.api.domain.po.CartItem;
import com.example.api.domain.vo.order.OrderInfoVo;
import com.example.api.domain.vo.payment.ChargeVo;
import com.example.api.domain.vo.payment.TransactionInfoVo;
import com.example.api.domain.vo.product.ProductInfoVo;
import com.example.api.enums.OrderStatusEnum;
import com.example.common.config.rabbitmq.RabbitQueueNamesConfig;
import com.example.common.config.rabbitmq.RetryableCorrelationData;
import com.example.common.domain.ResponseResult;
import com.example.common.domain.ResultCode;
import com.example.common.domain.message.*;
import com.example.common.exception.*;
import com.example.common.util.UserContextUtil;
import com.example.payment.config.RabbitMQTimeoutConfig;
import com.example.payment.domain.po.Credit;
import com.example.payment.domain.po.Transaction;
import com.example.api.enums.TransactionStatusEnum;
import com.example.payment.mapper.TransactionMapper;
import com.example.payment.service.CreditService;
import com.example.payment.service.TransactionService;
import com.example.payment.util.RedisHashUtil;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl extends ServiceImpl<TransactionMapper, Transaction> implements TransactionService {

    // 新增常量定义
    private static final String CANCEL_LOCK_KEY = "ecommerce-payment:cancel-lock:";
    private static final String SCHEDULED_KEY = "ecommerce-payment:scheduled:";

    // 新增Redis依赖注入
    private final RedisTemplate<String, Object> redisTemplate;
    // 使用RedisHash存储待取消的交易ID和取消时间戳
    private final RedisHashUtil scheduledHashOperations;

    @Resource
    private OrderClient orderClient;

    @Resource
    private RabbitQueueNamesConfig mqConfig;

    private final RabbitTemplate rabbitTemplate;

    private final CreditService creditService;

    private final ProductClient productClient;

    private final TransactionMapper transactionMapper;

    @Transactional
    @Override
    public ChargeVo charge(ChargeDto chargeDto) throws UserException, SystemException {
        ChargeVo vo = chargeInner(chargeDto);
        // 发送支付启动消息
        sendPaymentStartMessage(this.getById(vo.getTransactionId()));
        // 默认设置30分钟自动取消
        scheduleAutoCancel(vo.getTransactionId(), 30);
        return vo;
    }

    /**
     * 支付（内部方法）
     * @param chargeDto dto
     * @return o
     * @throws UserException 用户异常
     * @throws SystemException 系统异常
     */
    private ChargeVo chargeInner(ChargeDto chargeDto) throws UserException, SystemException {
        try {
            log.error(RootContext.getXID());
            // 检查这个orderId是否已经创建了交易信息
            LambdaQueryWrapper<Transaction> exist = new LambdaQueryWrapper<>();
            exist.eq(Transaction::getOrderId, chargeDto.getOrderId());
            if(transactionMapper.exists(exist)) {
                throw new BadRequestException("该订单ID已经生成了对应的交易记录");
            }
            Long userId = UserContextUtil.getUserId();
            Credit credit = creditService.checkCreditPermission(userId, chargeDto.getCreditId());

            // 验证订单状态
            ResponseResult<OrderInfoVo> orderResult = orderClient.getOrderById(chargeDto.getOrderId());
            if (orderResult.getCode() != ResultCode.SUCCESS) {
                log.error("订单服务状态异常：{}", orderResult.getMsg());
                throw new SystemException("订单服务状态异常："+orderResult.getMsg());
            } else if (orderResult.getData() == null) {
                throw new NotFoundException("未找到指定的订单");
            } else if (!Objects.equals(orderResult.getData().getStatus(), OrderStatusEnum.WAIT_FOR_CONFIRM)) {
                throw new BadRequestException("该订单不处于待确认的状态");
            }

            // 生成预支付记录
            String transId = UUID.randomUUID().toString();
            Float amount = this.calculateTotalAmount(chargeDto.getOrderId());
            Transaction preTransaction = buildPreTransaction(credit, chargeDto, transId, amount);
            this.save(preTransaction);

            ChargeVo chargeVo = new ChargeVo();
            chargeVo.setTransactionId(transId);
            chargeVo.setPreTransactionId(transId);
            return chargeVo;
        } catch (UserException e) {
            log.info("支付请求处理失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("支付请求处理失败: {}", e.getMessage());
            throw new SystemException(e);
        }
    }

    @Transactional
    @Override
    public ChargeVo quickCharge(ChargeDto chargeDto) throws UserException, SystemException {
        ChargeVo vo = chargeInner(chargeDto);
        confirmChargeInner(vo.getTransactionId());
        return vo;
    }

    private void confirmChargeInner(String preTransactionId) {
        Transaction transaction = validateTransaction(preTransactionId, TransactionStatusEnum.WAIT_FOR_CONFIRM);
        String transactionId = transaction.getTransactionId();
        List<Long> addProductIds = new ArrayList<>();
        try {
            // 使用Redis分布式锁防止重复确认
            String lockKey = CANCEL_LOCK_KEY + transaction.getTransactionId();
            Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", 30, TimeUnit.SECONDS);
            if (Boolean.FALSE.equals(lockAcquired)) {
                throw new BadRequestException("支付操作正在进行中，请稍后重试");
            }
            try {
                // 扣除余额
                creditService.pay(transaction.getUserId(), transaction.getCreditId(), transaction.getAmount());

                // 扣除库存
                String orderId = transaction.getOrderId();
                ResponseResult<OrderInfoVo> orderResult = orderClient.getOrderById(orderId);
                if (orderResult.getCode() != ResultCode.SUCCESS || orderResult.getData() == null) {
                    throw new SystemException(orderResult.getMsg());
                }
                orderResult.getData().getCartItems().forEach(cartItem -> {
                    DecProductDto dto = new DecProductDto();
                    dto.setProductId(cartItem.getProductId());
                    dto.setDecStock(cartItem.getQuantity());
                    ResponseResult<Object> response = productClient.decProductStock(dto);
                    if (response.getCode() != ResultCode.SUCCESS) {
                        throw new SystemException(response.getMsg());
                    }
                    addProductIds.add(cartItem.getProductId());
                });

                // 更新交易状态
                updateTransactionStatus(transaction, TransactionStatusEnum.PAY_SUCCESS, null);

                List<ProductQuantity> products = getProducts(transaction.getOrderId());
                // 发送支付成功消息
                sendPaymentSuccessMessage(transaction, products);
            } finally {
                redisTemplate.delete(lockKey);
            }
        } catch (Exception e) {
            log.error("支付确认失败: {}", e.getMessage());
            throw new SystemException(e.getMessage(), () -> {
                // 处理失败的方法抛到全局事物外解决
                handlePaymentFailure(transactionId, e.getMessage(), addProductIds);
            });
        }
    }

    @GlobalTransactional(name = "paymentConfirm", rollbackFor = Exception.class)
    @Override
    public void confirmCharge(String preTransactionId) throws UserException, SystemException {
        confirmChargeInner(preTransactionId);
    }

    @Override
    public void cancelCharge(String preTransactionId) throws UserException, SystemException {
        Long userId = UserContextUtil.getUserId();
        cancelCharge(userId, preTransactionId);
    }

    /**
     * 取消支付（内部方法）
     * @param userId 用户ID
     * @param preTransactionId 预交易ID
     * @throws UserException 用户异常
     * @throws SystemException 系统异常
     */
    @Transactional
    public void cancelCharge(Long userId, String preTransactionId) throws UserException, SystemException {
        // 取消定时任务
        Transaction transaction = transactionMapper.findByIdAndPreId(userId, null, preTransactionId);
        // 更新交易状态为已取消
        if(transaction.getStatus() != TransactionStatusEnum.WAIT_FOR_CONFIRM) {
            // 该支付不能取消
            return;
        }
        transaction.setStatus(TransactionStatusEnum.CANCELED);
        boolean update = this.updateById(transaction);
        if (!update) {
            log.error("交易状态更新失败");
            throw new DatabaseException("交易状态更新失败", new ConcurrentModificationException());
        }
        // 发送pay.cancel消息
        sendPaymentCancelMessage(transaction);
    }

    @Override
    public TransactionInfoVo getTransaction(Long userId, String transactionId, String preTransactionId) throws UserException, SystemException {
        Transaction transaction = transactionMapper.findByIdAndPreId(userId, transactionId, preTransactionId);
        if(transaction == null) {
            throw new NotFoundException("未找到指定ID的交易记录");
        }
        return TransactionInfoVo.builder()
                .transactionId(transaction.getTransactionId())
                .preTransactionId(transaction.getPreTransactionId())
                .status(transaction.getStatus())
                .amount(transaction.getAmount())
                .createTime(transaction.getCreateTime())
                .updateTime(transaction.getUpdateTime())
                .reason(transaction.getReason())
                .orderId(transaction.getOrderId())
                .creditId(transaction.getCreditId())
                .build();
    }

    @Override
    public IPage<TransactionInfoVo> getTransactionInfos(Integer pageNum, Integer pageSize, Long userId) throws UserException, SystemException {
        IPage<TransactionInfoVo> page = new Page<>(pageNum, pageSize);
        return transactionMapper.findByUserId(page, userId);
    }

    //    工具方法

    /**
     * 发送支付启动消息
     * @param preTransaction 交易对象
     */
    private void sendPaymentStartMessage(Transaction preTransaction) {
        PayStartMessage message = new PayStartMessage();
        message.setOrderId(preTransaction.getOrderId());
        RetryableCorrelationData data = new RetryableCorrelationData(message, mqConfig.getExchangeName(), mqConfig.getQueues().getPay().getStart());
        rabbitTemplate.convertAndSend(
                data.getExchange(),
                data.getRoutingKey(),
                data.getMessage(),
                data
        );
    }

    /**
     * 发送支付取消消息
     * @param transaction 交易对象
     */
    private void sendPaymentCancelMessage(Transaction transaction) {
        PayCancelMessage message = new PayCancelMessage();
        message.setOrderId(transaction.getOrderId());
        RetryableCorrelationData data = new RetryableCorrelationData(message, mqConfig.getExchangeName(), mqConfig.getQueues().getPay().getCancel());
        rabbitTemplate.convertAndSend(
                data.getExchange(),
                data.getRoutingKey(),
                data.getMessage(),
                data
        );
    }

    /**
     * 发送支付失败消息
     * @param transaction 交易对象
     * @param products 商品信息
     */
    private void sendPaymentFailedMessage(Transaction transaction, List<ProductQuantity> products, List<Long> addProductIds) {
        PayFailMessage message = new PayFailMessage();
        message.setOrderId(transaction.getOrderId());
        message.setProducts(products);
        message.setAddProductIds(addProductIds);
        RetryableCorrelationData data = new RetryableCorrelationData(message, mqConfig.getExchangeName(), mqConfig.getQueues().getPay().getFail());
        rabbitTemplate.convertAndSend(
                data.getExchange(),
                data.getRoutingKey(),
                data.getMessage(),
                data
        );
    }

    /**
     * 发送支付成功消息
     * @param transaction 交易ID
     * @param products 商品信息
     */
    private void sendPaymentSuccessMessage(Transaction transaction, List<ProductQuantity> products) {
        PaySuccessMessage message = new PaySuccessMessage();
        message.setOrderId(transaction.getOrderId());
        message.setProducts(products);
        RetryableCorrelationData data = new RetryableCorrelationData(message, mqConfig.getExchangeName(), mqConfig.getQueues().getPay().getSuccess());
        rabbitTemplate.convertAndSend(
                data.getExchange(),
                data.getRoutingKey(),
                data.getMessage(),
                data
        );
    }

    /**
     * 获取订单商品信息
     * @param orderId 订单ID
     * @return 商品信息
     */
    private List<ProductQuantity> getProducts(String orderId) {
        ResponseResult<OrderInfoVo> orderResult = orderClient.getOrderById(orderId);
        if (orderResult.getCode() != ResultCode.SUCCESS) {
            return null;
        }

        try {
            List<CartItem> cartItems = orderResult.getData().getCartItems();
            return cartItems.stream()
                    .map(item -> new ProductQuantity(item.getProductId(), item.getQuantity()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取商品信息：: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 计算订单所需要的花费
     * @param orderId 订单ID
     * @throws SystemException 系统异常
     * @return 所需金额
     */
    private Float calculateTotalAmount(String orderId) throws SystemException {
        // 调用用户信息，查询
        ResponseResult<OrderInfoVo> orderResult = orderClient.getOrderById(orderId);
        if(orderResult.getCode() != ResultCode.SUCCESS) {
            log.error("order-service: {}", orderResult.getMsg());
            throw new SystemException("order-service : "+orderResult.getMsg());
        }
        float totalAmount = 0f;
        for(CartItem cartItem : orderResult.getData().getCartItems()) {
            Long productId = cartItem.getProductId();
            ResponseResult<ProductInfoVo> productResult = productClient.getProductInfoById(productId);
            if(productResult.getCode() != ResultCode.SUCCESS) {
                log.error("product-service: {}", productResult.getMsg());
                throw new SystemException("product-service : "+productResult.getMsg());
            }
            Float price = productResult.getData().getPrice();
            totalAmount += price * cartItem.getQuantity();
        }
        return totalAmount;
    }

    /**
     * 构建预支付交易记录
     * @param credit 银行卡对象
     * @param chargeDto 支付dto
     * @param transId 交易ID
     * @return 交易对象
     */
    private Transaction buildPreTransaction(Credit credit, ChargeDto chargeDto, String transId, Float amount) {
        return Transaction.builder()
                .transactionId(transId)
                .preTransactionId(transId)
                .userId(credit.getUserId())
                .orderId(chargeDto.getOrderId())
                .creditId(chargeDto.getCreditId())
                .amount(amount)
                .status(TransactionStatusEnum.WAIT_FOR_CONFIRM)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .deleted(0)
                .build();
    }

    /**
     * 处理支付失败
     * @param transactionId 交易ID
     * @param reason 失败原因
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void handlePaymentFailure(String transactionId, String reason, List<Long> addProductIds) {
        Transaction transaction = this.getById(transactionId);
        if (transaction != null) {
            if(reason.length() > 200) {
                reason = reason.substring(0, 200);
            }
            updateTransactionStatus(transaction, TransactionStatusEnum.PAY_FAIL, reason);
            List<ProductQuantity> products = getProducts(transaction.getOrderId());
            sendPaymentFailedMessage(transaction, products, addProductIds);
        }
    }

    /**
     * 更新交易状态
     * @param transaction 交易ID
     * @param status 交易状态
     * @param reason 原因
     */
    private void updateTransactionStatus(Transaction transaction, TransactionStatusEnum status, String reason) {
        transaction.setStatus(status);
        transaction.setReason(reason);
        transaction.setUpdateTime(LocalDateTime.now());
        this.updateById(transaction);
    }

    /**
     * 设置定时任务
     * @param transactionId 交易ID
     * @param minutes 时间
     */
    private void scheduleAutoCancel(String transactionId, int minutes) {
        // 将订单和取消时间存储到Redis中
        long cancelTime = System.currentTimeMillis() + minutes * 60 * 1000L;
        scheduledHashOperations.put(SCHEDULED_KEY, transactionId, cancelTime);
        // 发送MQ消息到延迟队列中
        RetryableCorrelationData data = new RetryableCorrelationData(
                transactionId,
                RabbitMQTimeoutConfig.EXCHANGE_NAME,
                RabbitMQTimeoutConfig.ROUTING_KEY,
                message -> {
                    message.getMessageProperties().setExpiration(String.valueOf(minutes * 60 * 1000L));
                    return message;
                });
        rabbitTemplate.convertAndSend(data.getExchange(), data.getRoutingKey(), data.getMessage(), data.getMessagePostProcessor(), data);
        log.info("已设置交易{}在{}分钟后自动取消", transactionId, minutes);
    }

    /**
     * 定时任务扫描方法，主动检查订单状态（每分钟执行一次）
     */
    @Scheduled(cron = "0 * * * * ?")
    public void processScheduledCancellations() {
        // 创建副本避免并发修改
        Set<String> transactionIds = new HashSet<>(scheduledHashOperations.keys(SCHEDULED_KEY));
        for (String transactionId : transactionIds) {
            Transaction tran = this.getById(transactionId);
            if (tran == null || tran.getStatus() != TransactionStatusEnum.WAIT_FOR_CONFIRM) {
                // 说明这个订单已经进行过处理
                scheduledHashOperations.delete(SCHEDULED_KEY, transactionId);
                continue;
            }
            ResponseResult<OrderInfoVo> resp = orderClient.getOrderById(tran.getOrderId());
            if (resp.getCode() != ResultCode.SUCCESS || resp.getData() == null) {
                // 未知订单状态，跳过
                log.error("order-service: {}", resp.getMsg());
                continue;
            }
            if (resp.getData().getStatus() == OrderStatusEnum.CANCELED) {
                // 订单的状态已经变成了取消
                sendPaymentCancelMessage(tran);
            }
        }
    }

    /**
     * 验证交易记录
     * @param preTransactionId 预交易ID
     * @param expectedStatus 预期状态
     * @return 交易对象
     */
    private Transaction validateTransaction(String preTransactionId, TransactionStatusEnum expectedStatus) {
        Transaction transaction = transactionMapper.findByIdAndPreId(null, null, preTransactionId);
        if (transaction == null) {
            throw new NotFoundException("交易记录不存在");
        }
        if (!transaction.getStatus().equals(expectedStatus)) {
            throw new BadRequestException("当前状态无法操作");
        }
        return transaction;
    }
}
