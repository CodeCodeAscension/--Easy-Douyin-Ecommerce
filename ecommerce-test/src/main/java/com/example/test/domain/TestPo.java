package com.example.test.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("test")
@Data
public class TestPo {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String pwd;
}
