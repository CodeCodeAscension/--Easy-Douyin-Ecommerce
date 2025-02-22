package com.example.payment.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("credit")
public class Credit implements Serializable{

    private static final long serialVersionUID = 1L;

    @TableId(value = "card_number", type = IdType.ASSIGN_UUID)
    private String cardNumber;

    // 卡验证值
    private Integer cvv;

    private Long userId;

    // 余额
    private Float balance;

    // 乐观锁
    @Version
    private Integer version;

    // 过期日期
    private LocalDate expireDate;

    // 状态（0正常，1禁用，2过期）
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    // 逻辑删除
    private Integer deleted;

    public Credit(String creditId, Long userId, float v, int i) {
        this.cardNumber = creditId;
        this.userId = userId;
        this.balance = v;
        this.status = i;
    }
}
