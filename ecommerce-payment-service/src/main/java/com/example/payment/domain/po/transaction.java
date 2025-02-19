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
public class transaction implements Serializable{

    private static final long serialVersionUID = 1L;

    @TableId(value = "transaction_id", type = IdType.ASSIGN_UUID)
    private String transId;

    private Long userId;
    
    private String orderId;

    private String creditId;

    private Float amount;

    private Integer status;

    private String reason;

    private LocalDateTime createTime;

    private Integer deleted;
}
