package com.example.payment.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
public class credit implements Serializable{

    private static final long serialVersionUID = 1L;

    @TableId(value = "card_number", type = IdType.ASSIGN_UUID)
    private String cardNumber;

    private Integer cvv;

    private Long userId;

    private Integer balance;

    private Integer version;

    private LocalDate expireDate;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer deleted;
}
