package com.example.common.domain;

import com.example.common.exception.UserException;

/**
 * controller返回给前端的数据
 * @param <T> 返回的数据对象类型
 */
public class ResponseResult<T> {

    private int code;       // 请求状态码,使用ResuleCode类的定义
    private String msg;     // 请求消息
    private T data;         // 返回的数据

    public ResponseResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResponseResult() {
        
    }

    /**
     * 操作成功,并返回数据
     * @param data 要返回的数据
     */
    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(ResultCode.SUCCESS, "操作成功", data);
    }

    /**
     * 操作成功
     */
    public static <T> ResponseResult<T> success() {
        return new ResponseResult<>(ResultCode.SUCCESS, "操作成功", null);
    }

    /**
     * 操作失败,返回指定的状态码和消息
     * @param code 状态码
     * @param msg 消息
     */
    public static <T> ResponseResult<T> error(int code, String msg) {
        return new ResponseResult<>(code, msg, null);
    }

    /**
     * 操作失败，返回一个异常
     * @param cause 异常
     */
    public static <T> ResponseResult<T> error(int code, Throwable cause) {
        return new ResponseResult<>(code, cause.getMessage(), null);
    }

    /**
     * 远程服务调用失败
     * @param cause 异常
     */
    public static <T> ResponseResult<T> errorFeign(Throwable cause) {
        if(cause instanceof UserException) {
            return error(((UserException) cause).getCode(), cause.getMessage());
        } else {
            return error(ResultCode.SERVICE_UNAVAILABLE, cause.getMessage());
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
