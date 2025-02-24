package com.example.payment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.api.client.OrderClient;
import com.example.api.domain.po.CartItem;
import com.example.api.domain.vo.order.OrderInfoVo;
import com.example.common.config.rabbitmq.PayQueue;
import com.example.common.config.rabbitmq.RabbitQueuesConfig;
import com.example.common.domain.ResponseResult;
import com.example.common.domain.ResultCode;
import com.example.common.domain.message.*;
import com.example.common.exception.*;
import com.example.common.util.UserContextUtil;
import com.example.payment.convert.CreditConvertToVo;
import com.example.payment.convert.CreditDtoConvertToPo;
import com.example.payment.domain.dto.ChargeCancelDto;
import com.example.payment.domain.dto.ChargeDto;
import com.example.payment.domain.dto.CreditDto;
import com.example.payment.domain.dto.CreditUpdateDto;
import com.example.payment.domain.po.Credit;
import com.example.payment.domain.po.Transaction;
import com.example.payment.domain.vo.ChargeVo;
import com.example.payment.domain.vo.CreditVo;
import com.example.payment.enums.CreditStatusEnum;
import com.example.payment.enums.PaymentStatusEnum;
import com.example.payment.mapper.CreditMapper;
import com.example.payment.service.CreditService;
import com.example.payment.service.TransactionService;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.tools.rmi.RemoteException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditServiceImpl extends ServiceImpl<CreditMapper, Credit> implements CreditService {

    // 新增常量定义
    // 使用ConcurrentHashMap存储待取消的交易ID和取消时间戳
    private final Map<String, Long> scheduledCancellations = new ConcurrentHashMap<>();
    private static final String CANCEL_LOCK_KEY = "payment:cancel-lock:";

    // 新增Redis依赖注入
    private final RedisTemplate<String, Object> redisTemplate;

    @Resource
    private CreditMapper creditMapper;

    @Resource
    private TransactionService transactionService;

    @Resource
    private OrderClient orderClient;

    @Resource
    private RabbitQueuesConfig mqConfig;

    private final RabbitTemplate rabbitTemplate;

//    /**
//     * 支付功能
//     *
//     * @param chargeDto 订单支付信息
//     * @rerurn chargeVo 支付结果
//     */
//    @GlobalTransactional(name = "paymentCharge", rollbackFor = Exception.class)
//    @Override
//    public ResponseResult<ChargeVo> charge(ChargeDto chargeDto) {
//        try {
//            Credit credit = checkCreditPermission(chargeDto.getCreditId());
//
//            // 验证订单状态
//            ResponseResult<OrderInfoVo> orderResult = orderClient.getOrderById(chargeDto.getOrderId());
//            if (orderResult.getCode() != 200 || orderResult.getData().getStatus() != 1) {
//                throw new UserException(ResultCode.BAD_REQUEST, "订单状态异常");
//            }
//
//            // 生成预支付记录
//            String transId = UUID.randomUUID().toString();
//            Transaction preTransaction = buildPreTransaction(credit, chargeDto, transId);
//            transactionService.save(preTransaction);
//
//            // 发送支付启动消息
//            sendPaymentStartMessage(preTransaction);
//
//            // 默认设置30分钟自动取消
//            scheduleAutoCancel(transId, 30);
//
//            return ResponseResult.success(new ChargeVo(transId));
//        } catch (Exception e) {
//            log.error("支付请求处理失败: {}", e.getMessage());
//            return ResponseResult.error(ResultCode.BAD_REQUEST, e.getMessage());
//        }
//    }
//
//    /**
//     * 确认支付
//     *
//     * @param transactionId
//     */
//    @GlobalTransactional(name = "paymentConfirm", rollbackFor = Exception.class)
//    @Override
//    public ResponseResult<Object> confirmCharge(String transactionId) {
//        try {
//            Transaction transaction = validateTransaction(transactionId, 0);
//            Credit credit = creditMapper.selectById(transaction.getCreditId());
//
//            // 使用Redis分布式锁防止重复确认
//            String lockKey = CANCEL_LOCK_KEY + transactionId;
//            Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", 30, TimeUnit.SECONDS);
//            if (Boolean.FALSE.equals(lockAcquired)) {
//                throw new UserException(ResultCode.BAD_REQUEST,"支付操作正在进行中，请稍后重试");
//            }
//
//            try {
//                // 扣除余额
//                if (credit.getBalance() < transaction.getAmount()) {
//                    throw new UserException(ResultCode.BAD_REQUEST, "余额不足");
//                }
//                credit.setBalance(credit.getBalance() - transaction.getAmount());
//                creditMapper.updateById(credit);
//
//                // 更新交易状态
//                updateTransactionStatus(transaction, 1, null);
//
//                // 取消定时任务
//                cancelScheduledTask(transactionId);
//
//                List<ProductQuantity> products = getProducts(transaction.getOrderId());
//                // 发送支付成功消息
//                sendPaymentSuccessMessage(transaction, products);
//
//                return ResponseResult.success();
//            } finally {
//                redisTemplate.delete(lockKey);
//            }
//        } catch (Exception e) {
//            log.error("支付确认失败: {}", e.getMessage());
//            handlePaymentFailure(transactionId, e.getMessage());
//            return ResponseResult.error(ResultCode.BAD_REQUEST, e.getMessage());
//        }
//    }
//
//
//    /**
//     * 取消支付
//     *
//     * @param transactionId
//     * @return
//     */
//    @GlobalTransactional(name = "paymentCancel", rollbackFor = Exception.class)
//    @Override
//    public ResponseResult<Object> cancelCharge(String transactionId) {
//
//        checkUserLogin();
//
//        Transaction transaction = transactionService.getById(transactionId);
//        if (transaction == null) {
//            throw new NotFoundException("交易记录不存在");
//        }
//
//        if (transaction.getStatus() != 0) {
//            throw new UserException(ResultCode.BAD_REQUEST, "当前状态无法取消");
//        }
//
//        // 更新交易状态为已取消
//        transaction.setStatus(3);
//        boolean update = transactionService.updateById(transaction);
//        if (!update) {
//            log.error("交易状态更新失败");
//            throw new DatabaseException("交易状态更新失败", new ConcurrentModificationException());
//        }
//
//        sendPaymentCancelMessage(transaction);
//        return ResponseResult.success();
//    }
//
//    /**
//     * 定期取消支付
//     *
//     * @param chargeCancelDto
//     * @return
//     */
//    @Override
//    public ResponseResult<Object> autoCancelCharge(ChargeCancelDto chargeCancelDto) {
////        checkUserLogin();
//        String transactionId = chargeCancelDto.getTransactionId();
//
//        // 验证交易记录
//        Transaction transaction = transactionService.getById(transactionId);
//        if (transaction == null) {
//            throw new NotFoundException("交易记录不存在");
//        }
//        if (transaction.getStatus() != 0) {
//            throw new UserException(ResultCode.BAD_REQUEST, "当前状态无法取消");
//        }
//
//        if (chargeCancelDto.getStatus()) {
//            // 设置/修改定时取消
//            scheduleAutoCancel(transactionId, chargeCancelDto.getCancelAfterMinutes());
//        }
//
//        return ResponseResult.success();
//    }

    /**
     * 创建信用卡信息
     * @param creditDto dto
     * @return ResponseResult 对象
     */
    @Override
    public ResponseResult<CreditVo> createCredit(Long userId, CreditDto creditDto) {
        // 判断信用卡信息是否已存在
        Credit creditExist = creditMapper.selectById(creditDto.getCardNumber());
        if (creditExist != null) {
            log.error("信用卡信息已录入");
            throw new BadRequestException("信用卡信息已录入");
        }

        Credit credit = CreditDtoConvertToPo.convertToPo(userId, creditDto);
        int insert = creditMapper.insert(credit);
        if (insert == 0){
            log.error("信用卡信息保存失败");
            throw new DatabaseException("信用卡信息保存失败", new ConcurrentModificationException());
        }

        CreditVo creditVo = CreditConvertToVo.convertToVo(credit);

        return ResponseResult.success(creditVo);
    }

    /**
     * 删除信用卡信息
     *
     * @param cardNumber 信用卡卡号
     * @return ResponseResult 对象
     */
    @Override
    public ResponseResult<Object> deleteCredit(String cardNumber) {

        Long userId = UserContextUtil.getUserId();
        checkCreditPermission(userId, cardNumber);

        int delete = creditMapper.deleteById(cardNumber);
        if (delete == 0) {
            log.error("信用卡信息删除失败");
            throw new DatabaseException("信用卡信息删除失败", new ConcurrentModificationException());
        }

        return ResponseResult.success();
    }

    /**
     * 更新信用卡信息
     *
     * @param creditUpdateDto dto
     * @return ResponseResult 对象
     */
    @Override
    public ResponseResult<CreditVo> updateCredit(CreditUpdateDto creditUpdateDto) {

        // 校验用户权限与银行卡信息
        Credit credit = checkCreditPermission(creditUpdateDto.getUserId(), creditUpdateDto.getCardNumber());

        Float balance = creditUpdateDto.getBalance();
        if (balance != null) {
            credit.setBalance(balance);
        }

        CreditStatusEnum status = creditUpdateDto.getStatus();
        if (status != null) {
            credit.setStatus(status);
        }

        LocalDate expireDate = creditUpdateDto.getExpireDate();
        if (expireDate != null) {
            credit.setExpireDate(expireDate);
        }

        credit.setUpdateTime(LocalDateTime.now());
        int update = creditMapper.updateById(credit);
        if (update == 0) {
            log.error("信用卡信息更新失败");
            throw new DatabaseException("信用卡信息更新失败", new ConcurrentModificationException());
        }

        CreditVo creditVo = CreditConvertToVo.convertToVo(credit);

        return ResponseResult.success(creditVo);
    }

    /**
     * 查询信用卡信息
     *
     * @param cardNumber
     */
    @Override
    public ResponseResult<CreditVo> getCredit(String cardNumber) {

        Long userId = UserContextUtil.getUserId();
        Credit credit = checkCreditPermission(userId, cardNumber);

        CreditVo creditVo = CreditConvertToVo.convertToVo(credit);
        return ResponseResult.success(creditVo);
    }

    // -------------------------------------------- 工具方法 --------------------------------------------

    /**
     * 校验用户权限与信用卡信息
     * @param cardNumber
     * @return
     */
    public Credit checkCreditPermission(Long userId, String cardNumber) {
//        Long userId = UserContextUtil.getUserId();

        Credit credit = creditMapper.selectById(cardNumber);
        if (credit == null) {
            log.error("信用卡不存在，cardNumber: {}", cardNumber);
            throw new NotFoundException("信用卡不存在");
        }

        if (!userId.equals(credit.getUserId())) {
            log.error("用户ID不符合，userId: {}, creditUserId: {}", userId, credit.getUserId());
            throw new BadRequestException("指定用户Id与银行卡记录不符合");
        }

        return credit;
    }
//
//    /**
//     * 发送支付启动消息
//     * @param preTransaction
//     */
//    private void sendPaymentStartMessage(Transaction preTransaction) {
//        PayStartMessage message = new PayStartMessage();
//        message.setOrderId(preTransaction.getOrderId());
//
//        rabbitTemplate.convertAndSend(
//                mqConfig.getExchangeName(),
//                mqConfig.getQueues().pay.getStart(),
//                message
//        );
//    }
//
//    /**
//     * 发送支付取消消息
//     * @param transaction
//     */
//    private void sendPaymentCancelMessage(Transaction transaction) {
//        PayCancelMessage message = new PayCancelMessage();
//        message.setOrderId(transaction.getOrderId());
//
//        PayQueue payQueue = mqConfig.getQueues().pay;
//        rabbitTemplate.convertAndSend(
//                mqConfig.getExchangeName(),
//                payQueue.getCancel(),
//                message
//        );
//    }
//
//    /**
//     * 发送支付失败消息
//     * @param transaction
//     * @param products
//     */
//    private void sendPaymentFailedMessage(Transaction transaction, List<ProductQuantity> products) {
//        PayFailMessage message = new PayFailMessage();
//        message.setOrderId(transaction.getOrderId());
//        message.setProducts(products);
//        rabbitTemplate.convertAndSend(
//                mqConfig.getExchangeName(),
//                mqConfig.getQueues().pay.getFail(),
//                message
//        );
//    }
//
//    /**
//     * 发送支付成功消息
//     * @param transaction
//     * @param products
//     */
//    private void sendPaymentSuccessMessage(Transaction transaction, List<ProductQuantity> products) {
//        PaySuccessMessage message = new PaySuccessMessage();
//        message.setOrderId(transaction.getOrderId());
//        message.setProducts(products);
//        rabbitTemplate.convertAndSend(
//                mqConfig.getExchangeName(),
//                mqConfig.getQueues().pay.getSuccess(),
//                message
//        );
//    }
//
//    /**
//     * 获取订单商品信息
//     * @param orderId
//     * @return
//     */
//    private List<ProductQuantity> getProducts(String orderId) {
//        ResponseResult<OrderInfoVo> orderResult = orderClient.getOrderById(orderId);
//        if (orderResult.getCode() != 200) {
//            throw new NotFoundException("订单信息不存在");
//        }
//
//        List<CartItem> cartItems = orderResult.getData().getCartItems();
//        return cartItems.stream()
//                .map(item -> new ProductQuantity(item.getProductId(), item.getQuantity()))
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * 构建预支付交易记录
//     * @param credit
//     * @param chargeDto
//     * @param transId
//     * @return
//     */
//    private Transaction buildPreTransaction(Credit credit, ChargeDto chargeDto, String transId) {
//        return Transaction.builder()
//                .transId(transId)
//                .preTransId(transId)
//                .userId(credit.getUserId())
//                .orderId(chargeDto.getOrderId())
//                .creditId(chargeDto.getCreditId())
//                .amount(chargeDto.getAmount())
//                .status(0)
//                .createTime(LocalDateTime.now())
//                .updateTime(LocalDateTime.now())
//                .deleted(0)
//                .build();
//    }
//
//    /**
//     * 处理支付失败
//     * @param transactionId
//     * @param reason
//     */
//    private void handlePaymentFailure(String transactionId, String reason) {
//        Transaction transaction = transactionService.getById(transactionId);
//        if (transaction != null) {
//            updateTransactionStatus(transaction, 2, reason);
//            List<ProductQuantity> products = getProducts(transaction.getOrderId());
//            sendPaymentFailedMessage(transaction, products);
//            cancelScheduledTask(transactionId);
//        }
//    }
//
//    /**
//     * 更新交易状态
//     */
//    private void updateTransactionStatus(Transaction transaction, Integer status, String reason) {
//        transaction.setStatus(status);
//        transaction.setReason(reason);
//        transaction.setUpdateTime(LocalDateTime.now());
//        transactionService.updateById(transaction);
//    }
//
//    /**
//     * 设置定时任务
//     * @param transactionId
//     * @param minutes
//     */
//    void scheduleAutoCancel(String transactionId, int minutes) {
//        long cancelTime = System.currentTimeMillis() + minutes * 60 * 1000L;
//        scheduledCancellations.put(transactionId, cancelTime);
//        log.info("已设置交易{}在{}分钟后自动取消", transactionId, minutes);
//    }
//
//    /**
//     * 取消定时任务
//     */
//    private void cancelScheduledTask(String transactionId) {
//        scheduledCancellations.remove(transactionId);
//        log.info("已移除交易{}的自动取消任务", transactionId);
//    }
//
//    /**
//     * 定时任务扫描方法（每分钟执行一次）
//     */
//    @Scheduled(cron = "0 * * * * ?")
//    public void processScheduledCancellations() {
//        long now = System.currentTimeMillis();
//        // 创建副本避免并发修改
//        Set<String> transactionIds = new HashSet<>(scheduledCancellations.keySet());
//
//        for (String transactionId : transactionIds) {
//            Long cancelTime = scheduledCancellations.get(transactionId);
//            if (cancelTime == null || cancelTime > now) {
//                continue;
//            }
//
//            // 使用Redis分布式锁保证原子性
//            String lockKey = CANCEL_LOCK_KEY + transactionId;
//            try {
//                Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent(
//                        lockKey, "locked", 30, TimeUnit.SECONDS
//                );
//                if (Boolean.FALSE.equals(lockAcquired)) {
//                    continue;
//                }
//
//                Transaction transaction = transactionService.getById(transactionId);
//                if (transaction != null && transaction.getStatus() == 0) {
//                    log.info("执行自动取消支付: {}", transactionId);
//                    cancelCharge(transactionId);
//                }
//                // 移除已处理的任务
//                scheduledCancellations.remove(transactionId);
//            } catch (Exception e) {
//                log.error("自动取消支付失败: {}", e.getMessage());
//            } finally {
//                redisTemplate.delete(lockKey);
//            }
//        }
//    }
//
//    /**
//     * 验证交易记录
//     * @param transactionId
//     * @param expectedStatus
//     * @return
//     */
//    private Transaction validateTransaction(String transactionId, int expectedStatus) {
//        Transaction transaction = transactionService.getById(transactionId);
//        if (transaction == null) {
//            throw new NotFoundException("交易记录不存在");
//        }
//        if (transaction.getStatus() != expectedStatus) {
//            throw new UserException(ResultCode.BAD_REQUEST, "当前状态无法操作");
//        }
//        return transaction;
//    }
}
