package com.example.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.exception.*;
import com.example.common.util.UserContextUtil;
import com.example.payment.convert.CreditConvertToVo;
import com.example.payment.convert.CreditDtoConvertToPo;
import com.example.payment.domain.dto.CreditCreateDto;
import com.example.payment.domain.dto.CreditDto;
import com.example.payment.domain.dto.CreditUpdateDto;
import com.example.payment.domain.po.Credit;
import com.example.payment.domain.vo.CreditVo;
import com.example.payment.enums.CreditStatusEnum;
import com.example.payment.mapper.CreditMapper;
import com.example.payment.service.BankService;
import com.example.payment.service.CreditService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditServiceImpl extends ServiceImpl<CreditMapper, Credit> implements CreditService {

    @Resource
    private CreditMapper creditMapper;
    private final BankService bankService;

    @Override
    public CreditVo createCredit(Long userId, CreditCreateDto creditCreateDto) throws UserException, SystemException {
        // 判断信用卡信息是否已存在
        Credit creditExist = creditMapper.selectById(creditCreateDto.getCardNumber());
        if (creditExist != null) {
            log.error("信用卡信息已录入");
            throw new BadRequestException("信用卡信息已录入");
        }

        // 使用银行服务判断银行卡信息是否正确
        if(!bankService.checkCredit(creditCreateDto.getCardNumber(), creditCreateDto.getCvv())) {
            throw new BadRequestException("信用卡信息不正确！");
        }

        // 转化为createDto
        CreditDto creditDto = CreditDto.builder()
                .cardNumber(creditCreateDto.getCardNumber())
                .cvv(creditCreateDto.getCvv())
                .balance(bankService.getBalance(creditCreateDto.getCardNumber()))
                .expireDate(bankService.getExpiration(creditCreateDto.getCardNumber()))
                .build();

        Credit credit = CreditDtoConvertToPo.convertToPo(userId, creditDto);
        int insert = creditMapper.insert(credit);
        if (insert == 0){
            log.error("信用卡信息保存失败");
            throw new DatabaseException("信用卡信息保存失败", new ConcurrentModificationException());
        }

        return CreditConvertToVo.convertToVo(credit);
    }

    @Override
    public void deleteCredit(String cardNumber) throws UserException, SystemException {

        Long userId = UserContextUtil.getUserId();
        checkCreditPermission(userId, cardNumber);

        int delete = creditMapper.deleteById(cardNumber);
        if (delete == 0) {
            log.error("信用卡信息删除失败");
            throw new DatabaseException("信用卡信息删除失败", new ConcurrentModificationException());
        }
    }

    @Override
    public CreditVo updateCredit(CreditUpdateDto creditUpdateDto) throws UserException, SystemException {

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

        return CreditConvertToVo.convertToVo(credit);
    }

    @Override
    public CreditVo getCredit(Long userId, String cardNumber) throws UserException, SystemException {
        Credit credit = checkCreditPermission(userId, cardNumber);

        return CreditConvertToVo.convertToVo(credit);
    }

    // -------------------------------------------- 工具方法 --------------------------------------------

    @Override
    public Credit checkCreditPermission(Long userId, String cardNumber) throws UserException, SystemException {
        LambdaQueryWrapper<Credit> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Credit::getUserId, userId)
                    .eq(Credit::getCardNumber, cardNumber)
                    .eq(Credit::getStatus, CreditStatusEnum.NORMAL)
                    .eq(Credit::getDeleted, 0);
        Credit credit;
        try {
            credit = creditMapper.selectOne(queryWrapper);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
        if (credit == null) {
            log.info("信用卡不存在，cardNumber: {}", cardNumber);
            throw new NotFoundException("信用卡不存在");
        }
        // 检验有效期
        if(credit.getExpireDate().isBefore(LocalDate.now())) {
            // 更新数据库
            LambdaUpdateWrapper<Credit> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(Credit::getCardNumber, cardNumber)
                    .set(Credit::getStatus, CreditStatusEnum.EXPIRED);
            this.update(wrapper);
            // 抛出过期异常
            log.info("银行卡 {} 已经过期", cardNumber);
            throw new BadRequestException("指定银行卡已经过期");
        }

        return credit;
    }

    @Override
    @Transactional
    public void pay(Long userId, String cardNumber, Float amount) throws UserException, SystemException {
        Credit credit = checkCreditPermission(userId, cardNumber);
        if(credit.getBalance() < amount) {
            throw new BadRequestException("余额不足");
        }

        // 更新数据库余额
        credit.setBalance(credit.getBalance() - amount);
        this.updateById(credit);
    }

    @Override
    public IPage<CreditVo> getCreditListByUserId(Long userId, Integer pageNum, Integer pageSize) throws UserException, SystemException {
        IPage<CreditVo> page = new Page<>(pageNum, pageSize);
        return creditMapper.selectToVo(page, userId);
    }
}
