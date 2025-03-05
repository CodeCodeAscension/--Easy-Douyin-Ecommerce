package com.example.payment.service;

import java.time.LocalDate;

/**
 * 银行卡的银行服务接口
 */
public interface BankService {

    /**
     * 检查银行卡信息是否正确
     * @param creditNumber 银行卡号
     * @param cvv cvv值
     * @return 是否正确
     */
    boolean checkCredit(String creditNumber, Integer cvv);

    /**
     * 获取余额
     * @param creditNumber 银行卡号
     * @return 余额
     */
    Float getBalance(String creditNumber);

    /**
     * 获取过期时间
     * @param creditNumber 银行卡号
     * @return 过期时间
     */
    LocalDate getExpiration(String creditNumber);
}
