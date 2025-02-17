package com.example.user.domain.vo;

//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
//@ApiModel(description = "用户注册返回信息")
public class LoginVo {
//    @ApiModelProperty("用户ID")
    private Long userId;
//    @ApiModelProperty("token")
    private String token;
}
