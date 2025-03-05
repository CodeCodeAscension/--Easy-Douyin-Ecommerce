package com.example.payment.service.impl;

import com.example.payment.service.BankService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;

/**
 * 模拟银行卡的接口服务
 */
@Service
public class BankServiceImpl implements BankService {
    @Override
    public boolean checkCredit(String creditNumber, Integer cvv) {
        return true;
    }

    @Override
    public Float getBalance(String creditNumber) {
        float min = 10000.0f;
        float max = 100000.0f;
        float randomValue = (float) (min + (max - min) * Math.random());
        randomValue = (float) (Math.round(randomValue * 100) / 100.0);
        return randomValue;
    }

    @Override
    public LocalDate getExpiration(String creditNumber) {
        return LocalDate.now().plusYears(1L);
    }
}
