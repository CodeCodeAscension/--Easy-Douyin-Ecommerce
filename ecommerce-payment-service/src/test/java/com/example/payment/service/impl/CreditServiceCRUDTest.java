package com.example.payment.service.impl;

import com.example.common.domain.ResponseResult;
import com.example.common.exception.BadRequestException;
import com.example.common.exception.DatabaseException;
import com.example.common.exception.NotFoundException;
import com.example.common.util.UserContextUtil;
import com.example.payment.config.RabbitMQTimeoutConfig;
import com.example.payment.domain.dto.CreditCreateDto;
import com.example.payment.domain.dto.CreditDto;
import com.example.payment.domain.dto.CreditUpdateDto;
import com.example.payment.domain.po.Credit;
import com.example.payment.domain.vo.CreditVo;
import com.example.payment.mapper.CreditMapper;
import com.example.payment.service.CreditService;
import jakarta.annotation.Resource;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.reactivestreams.Publisher;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

//import static org.junit.Assert.assertThat;
// 使用 AssertJ 的静态导入
import static org.assertj.core.api.Assertions.assertThat;

// 或使用 JUnit 5 原生断言
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RequiredArgsConstructor
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CreditServiceCRUDTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testRabbit() {
        rabbitTemplate.convertAndSend(
                RabbitMQTimeoutConfig.EXCHANGE_NAME,
                RabbitMQTimeoutConfig.ROUTING_KEY,
                "Hello World",
                message -> {
                    message.getMessageProperties().setExpiration(String.valueOf(1 * 60 * 1000L));
                    return message;
                });
    }

    @Mock
    private CreditMapper creditMapper;

    @InjectMocks
    private CreditServiceImpl creditService;

    // 测试数据
    private final Long validUserId = 1L;
    private final String validCardNumber = "4111111111111111";
    private final Credit validCredit = Credit.builder()
            .cardNumber(validCardNumber)
            .userId(validUserId)
            .deleted(0)
            .build();


    @BeforeEach
    void setup() {
        // 模拟用户登录
        UserContextTestUtils.mockLogin(validUserId);
    }

    @AfterEach
    void tearDown() {
        // 清理登录状态，避免测试间干扰
        UserContextTestUtils.clear();
    }


    @Test
    void createCredit_Success() {

        // 构建合法DTO
        CreditCreateDto dto = new CreditCreateDto();
        dto.setCardNumber(validCardNumber);
//        dto.setBalance(1000.0f);
//        dto.setExpireDate(LocalDate.now().plusYears(1));

        // Mock Mapper行为
        when(creditMapper.selectById(validCardNumber)).thenReturn(null);
        when(creditMapper.insert(any(Credit.class))).thenReturn(1);

        CreditVo result = creditService.createCredit(UserContextUtil.getUserId(), dto);

        // 验证结果
//        assertThat(result.getCode()).isEqualTo(200);
        assertThat(result.getCardNumber()).isEqualTo(validCardNumber);
    }

//    @Test
//    void createCredit_DuplicateCardNumber() {
//        when(creditMapper.selectById(validCardNumber)).thenReturn(validCredit);
//
//        CreditDto dto = new CreditDto();
//        dto.setCardNumber(validCardNumber);
//
//        assertThatThrownBy(() -> creditService.createCredit(UserContextUtil.getUserId(), dto))
//                .isInstanceOf(BadRequestException.class)
//                .hasMessage("信用卡信息已录入");
//    }

    @Test
    void deleteCredit_Success() {
        // Mock权限校验返回有效信用卡
        when(creditMapper.selectById(validCardNumber)).thenReturn(validCredit);
        when(creditMapper.deleteById(validCardNumber)).thenReturn(1);

        creditService.deleteCredit(validCardNumber);

//        assertThat(result.getCode()).isEqualTo(200);
//        verify(creditMapper).updateById(argThat(credit -> credit.getDeleted() == 1));
    }

    @Test
    void deleteCredit_NotFound() {
        when(creditMapper.selectById(validCardNumber)).thenReturn(null);

        assertThatThrownBy(() -> creditService.deleteCredit(validCardNumber))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("信用卡不存在");
    }

    @Test
    void deleteCredit_AlreadyDeleted() {
        Credit deletedCredit = Credit.builder().userId(validUserId).deleted(1).build();
        when(creditMapper.selectById(validCardNumber)).thenReturn(deletedCredit);

        assertThatThrownBy(() -> creditService.deleteCredit(validCardNumber))
                .isInstanceOf(DatabaseException.class)
                .hasMessage("信用卡信息删除失败");
    }

}
