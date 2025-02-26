package com.example.payment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.api.client.OrderClient;
import com.example.api.client.ProductClient;
import com.example.api.domain.dto.payment.ChargeCancelDto;
import com.example.api.domain.dto.payment.ChargeDto;
import com.example.api.domain.po.CartItem;
import com.example.api.domain.vo.order.OrderInfoVo;
import com.example.api.domain.vo.payment.ChargeVo;
import com.example.api.domain.vo.product.ProductInfoVo;
import com.example.common.config.rabbitmq.PayQueue;
import com.example.common.config.rabbitmq.RabbitQueuesConfig;
import com.example.common.domain.ResponseResult;
import com.example.common.domain.ResultCode;
import com.example.common.domain.message.*;
import com.example.common.exception.*;
import com.example.common.util.UserContextUtil;
import com.example.payment.domain.po.Credit;
import com.example.payment.domain.po.Transaction;
import com.example.payment.mapper.CreditMapper;
import com.example.payment.mapper.TransactionMapper;
import com.example.payment.service.CreditService;
import com.example.payment.service.TransactionService;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl extends ServiceImpl<TransactionMapper, Transaction> implements TransactionService {

    // 新增常量定义
    // 使用ConcurrentHashMap存储待取消的交易ID和取消时间戳
    private final Map<String, Long> scheduledCancellations = new ConcurrentHashMap<>();
    private static final String CANCEL_LOCK_KEY = "payment:cancel-lock:";

    // 新增Redis依赖注入
    private final RedisTemplate<String, Object> redisTemplate;

    @Resource
    private CreditMapper creditMapper;

    @Resource
    private OrderClient orderClient;

    @Resource
    private RabbitQueuesConfig mqConfig;

    private final RabbitTemplate rabbitTemplate;

    private final CreditService creditService;

    private final ProductClient productClient;

    @GlobalTransactional(name = "paymentCharge", rollbackFor = Exception.class)
    @Override
    public ChargeVo charge(ChargeDto chargeDto) throws UserException, SystemException {
        try {
            Long userId = UserContextUtil.getUserId();
            Credit credit = creditService.checkCreditPermission(userId, chargeDto.getCreditId());

            // 验证订单状态
            ResponseResult<OrderInfoVo> orderResult = orderClient.getOrderById(chargeDto.getOrderId());
            if (orderResult.getCode() != 200 || orderResult.getData().getStatus() != 1) {
                throw new BadRequestException("订单状态异常");
            }

            // 生成预支付记录
            String transId = UUID.randomUUID().toString();
            Float amount = this.calculateTotalAmount(chargeDto.getOrderId());
            Transaction preTransaction = buildPreTransaction(credit, chargeDto, transId, amount);
            this.save(preTransaction);

            // 发送支付启动消息
            sendPaymentStartMessage(preTransaction);

            // 默认设置30分钟自动取消
            scheduleAutoCancel(transId, 30);

            ChargeVo chargeVo = new ChargeVo();
            chargeVo.setTransactionId(transId);
            return chargeVo;
        } catch (Exception e) {
            log.error("支付请求处理失败: {}", e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
    }

    @GlobalTransactional(name = "paymentConfirm", rollbackFor = Exception.class)
    @Override
    public void confirmCharge(String transactionId) throws UserException, SystemException {
        try {
            Transaction transaction = validateTransaction(transactionId, 0);
            Credit credit = creditMapper.selectById(transaction.getCreditId());

            // 使用Redis分布式锁防止重复确认
            String lockKey = CANCEL_LOCK_KEY + transactionId;
            Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", 30, TimeUnit.SECONDS);
            if (Boolean.FALSE.equals(lockAcquired)) {
                throw new BadRequestException("支付操作正在进行中，请稍后重试");
            }

            try {
                // 扣除余额
                if (credit.getBalance() < transaction.getAmount()) {
                    throw new BadRequestException("余额不足");
                }
                credit.setBalance(credit.getBalance() - transaction.getAmount());
                creditMapper.updateById(credit);

                // 更新交易状态
                updateTransactionStatus(transaction, 1, null);

                // 取消定时任务
                cancelScheduledTask(transactionId);

                List<ProductQuantity> products = getProducts(transaction.getOrderId());
                // 发送支付成功消息
                sendPaymentSuccessMessage(transaction, products);
            } finally {
                redisTemplate.delete(lockKey);
            }
        } catch (Exception e) {
            log.error("支付确认失败: {}", e.getMessage());
            handlePaymentFailure(transactionId, e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
    }

    @GlobalTransactional(name = "paymentCancel", rollbackFor = Exception.class)
    @Override
    public void cancelCharge(String transactionId) throws UserException, SystemException {
//        checkUserLogin();
        Transaction transaction = this.getById(transactionId);
        if (transaction == null) {
            throw new NotFoundException("交易记录不存在");
        }

        if (transaction.getStatus() != 0) {
            throw new BadRequestException("当前状态无法取消");
        }

        // 更新交易状态为已取消
        transaction.setStatus(3);
        boolean update = this.updateById(transaction);
        if (!update) {
            log.error("交易状态更新失败");
            throw new DatabaseException("交易状态更新失败", new ConcurrentModificationException());
        }

        sendPaymentCancelMessage(transaction);
    }

    @Override
    public void autoCancelCharge(ChargeCancelDto chargeCancelDto) throws UserException, SystemException {
//        checkUserLogin();
        String transactionId = chargeCancelDto.getPreTransactionId();

        // 验证交易记录
        Transaction transaction = this.getById(transactionId);
        if (transaction == null) {
            throw new NotFoundException("交易记录不存在");
        }
        if (transaction.getStatus() != 0) {
            throw new BadRequestException("当前状态无法取消");
        }

        if (chargeCancelDto.getStatus()) {
            // 设置/修改定时取消
            scheduleAutoCancel(transactionId, chargeCancelDto.getCancelAfterMinutes());
        }
    }



//    工具方法

    /**
     * 发送支付启动消息
     * @param preTransaction 预交易ID
     */
    private void sendPaymentStartMessage(Transaction preTransaction) {
        PayStartMessage message = new PayStartMessage();
        message.setOrderId(preTransaction.getOrderId());

        rabbitTemplate.convertAndSend(
                mqConfig.getExchangeName(),
                mqConfig.getQueues().pay.getStart(),
                message
        );
    }

    /**
     * 发送支付取消消息
     * @param transaction 交易ID
     */
    private void sendPaymentCancelMessage(Transaction transaction) {
        PayCancelMessage message = new PayCancelMessage();
        message.setOrderId(transaction.getOrderId());

        PayQueue payQueue = mqConfig.getQueues().pay;
        rabbitTemplate.convertAndSend(
                mqConfig.getExchangeName(),
                payQueue.getCancel(),
                message
        );
    }

    /**
     * 发送支付失败消息
     * @param transaction 交易ID
     * @param products 商品信息
     */
    private void sendPaymentFailedMessage(Transaction transaction, List<ProductQuantity> products) {
        PayFailMessage message = new PayFailMessage();
        message.setOrderId(transaction.getOrderId());
        message.setProducts(products);
        rabbitTemplate.convertAndSend(
                mqConfig.getExchangeName(),
                mqConfig.getQueues().pay.getFail(),
                message
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
        rabbitTemplate.convertAndSend(
                mqConfig.getExchangeName(),
                mqConfig.getQueues().pay.getSuccess(),
                message
        );
    }

    /**
     * 获取订单商品信息
     * @param orderId 订单ID
     * @return 商品信息
     */
    private List<ProductQuantity> getProducts(String orderId) {
        ResponseResult<OrderInfoVo> orderResult = orderClient.getOrderById(orderId);
        if (orderResult.getCode() != 200) {
            throw new NotFoundException("订单信息不存在");
        }

        List<CartItem> cartItems = orderResult.getData().getCartItems();
        return cartItems.stream()
                .map(item -> new ProductQuantity(item.getProductId(), item.getQuantity()))
                .collect(Collectors.toList());
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
                .transId(transId)
                .preTransId(transId)
                .userId(credit.getUserId())
                .orderId(chargeDto.getOrderId())
                .creditId(chargeDto.getCreditId())
                .amount(amount)
                .status(0)
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
    private void handlePaymentFailure(String transactionId, String reason) {
        Transaction transaction = this.getById(transactionId);
        if (transaction != null) {
            updateTransactionStatus(transaction, 2, reason);
            List<ProductQuantity> products = getProducts(transaction.getOrderId());
            sendPaymentFailedMessage(transaction, products);
            cancelScheduledTask(transactionId);
        }
    }

    /**
     * 更新交易状态
     * @param transaction 交易ID
     * @param status 交易状态
     * @param reason 原因
     */
    private void updateTransactionStatus(Transaction transaction, Integer status, String reason) {
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
    void scheduleAutoCancel(String transactionId, int minutes) {
        long cancelTime = System.currentTimeMillis() + minutes * 60 * 1000L;
        scheduledCancellations.put(transactionId, cancelTime);
        log.info("已设置交易{}在{}分钟后自动取消", transactionId, minutes);
    }

    /**
     * 取消定时任务
     * @param transactionId 交易ID
     */
    private void cancelScheduledTask(String transactionId) {
        scheduledCancellations.remove(transactionId);
        log.info("已移除交易{}的自动取消任务", transactionId);
    }

    /**
     * 定时任务扫描方法（每分钟执行一次）
     */
    @Scheduled(cron = "0 * * * * ?")
    public void processScheduledCancellations() {
        long now = System.currentTimeMillis();
        // 创建副本避免并发修改
        Set<String> transactionIds = new HashSet<>(scheduledCancellations.keySet());

        for (String transactionId : transactionIds) {
            Long cancelTime = scheduledCancellations.get(transactionId);
            if (cancelTime == null || cancelTime > now) {
                continue;
            }

            // 使用Redis分布式锁保证原子性
            String lockKey = CANCEL_LOCK_KEY + transactionId;
            try {
                Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(
                        lockKey, "locked", 30, TimeUnit.SECONDS
                );
                if (Boolean.FALSE.equals(lockAcquired)) {
                    continue;
                }

                Transaction transaction = this.getById(transactionId);
                if (transaction != null && transaction.getStatus() == 0) {
                    log.info("执行自动取消支付: {}", transactionId);
                    cancelCharge(transactionId);
                }
                // 移除已处理的任务
                scheduledCancellations.remove(transactionId);
            } catch (Exception e) {
                log.error("自动取消支付失败: {}", e.getMessage());
            } finally {
                redisTemplate.delete(lockKey);
            }
        }
    }

    /**
     * 验证交易记录
     * @param transactionId 交易ID
     * @param expectedStatus 预期状态
     * @return 交易对象
     */
    private Transaction validateTransaction(String transactionId, int expectedStatus) {
        Transaction transaction = this.getById(transactionId);
        if (transaction == null) {
            throw new NotFoundException("交易记录不存在");
        }
        if (transaction.getStatus() != expectedStatus) {
            throw new BadRequestException("当前状态无法操作");
        }
        return transaction;
    }
}
