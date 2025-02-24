package com.example.payment.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import com.example.payment.enums.CreditStatusEnum;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("credit")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Credit implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "card_number", type = IdType.INPUT)
    private String cardNumber;

    // 卡验证值
    private Integer cardCvv;

    private Long userId;

    // 余额
    private Float balance;

    // 乐观锁
    @Version
    private Integer version = 0;

    // 过期日期
    private LocalDate expireDate;

    // 状态（0正常，1禁用，2过期）
    @TableField(fill = FieldFill.INSERT)
    private CreditStatusEnum status = CreditStatusEnum.NORMAL;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    // 逻辑删除
    @TableLogic
    private Integer deleted = 0;


}
