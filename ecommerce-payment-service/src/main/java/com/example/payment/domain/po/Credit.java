package com.example.payment.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import com.example.payment.enums.CreditStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "银行卡信息实体类")
public class Credit implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "card_number", type = IdType.INPUT)
    @Schema(description = "银行卡号")
    private String cardNumber;

    // 卡验证值
    @Schema(description = "卡验证值")
    private Integer cardCvv;

    @Schema(description = "所属用户ID")
    private Long userId;

    // 余额
    @Schema(description = "余额")
    private Float balance;

    // 乐观锁
    @Version
    private Integer version = 0;

    // 过期日期
    @Schema(description = "过期时间")
    private LocalDate expireDate;

    // 状态（0正常，1禁用，2过期）
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "银行卡状态")
    private CreditStatusEnum status = CreditStatusEnum.NORMAL;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    private Integer deleted = 0;


}
