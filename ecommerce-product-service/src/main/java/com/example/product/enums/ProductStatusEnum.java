package com.example.product.enums;

import com.example.common.domain.ResultCode;
import com.example.product.exception.BaseExceptionInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductStatusEnum implements BaseExceptionInterface {
    // =================================== 通用异常码 ===================================
    SYSTEM_ERROR(ResultCode.SERVER_ERROR, "出错啦，后台小哥正在努力修复中..."),

    //=================================== 业务异常状态码 ===================================
    PRODUCT_NOT_EXIST(ResultCode.NOT_FOUND, "该商品不存在"),
    PRODUCT_CATEGORY_NOT_EXIST(ResultCode.NOT_FOUND, "该商品的分类不存在"),
    ;

    // 异常码
    private Integer errorCode;

    //错误信息
    private String errorMessage;
}
