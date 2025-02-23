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
    PARAM_NOT_NULL(ResultCode.BAD_REQUEST, "参数不能为空"),
    CATEGORY_PRODUCT_NOT_EXIST(ResultCode.NOT_FOUND, "该分类下的商品不存在"),
    USER_NOT_LOGIN(ResultCode.UNAUTHORIZED, "用户未登录"),
    PRODUCT_STOCK_NOT_ENOUGH(ResultCode.BAD_REQUEST, "商品库存不足"),
    PRODUCT_STOCK_UPDATE_FAIL(ResultCode.CONFLICT, "可能由于并发冲突导致商品库存更新失败"),
    PRODUCT_SOLD_NOT_ENOUGH(ResultCode.BAD_REQUEST, "商品销量不足, 无法扣减");

    // 异常码
    private Integer errorCode;

    //错误信息
    private String errorMessage;
}
