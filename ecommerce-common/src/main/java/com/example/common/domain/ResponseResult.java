package com.example.common.domain;

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
    public static ResponseResult<Object> success() {
        return new ResponseResult<>(ResultCode.SUCCESS, "操作成功", null);
    }

    /**
     * 操作失败,返回指定的状态码和消息
     * @param code 状态码
     * @param msg 消息
     */
    public static ResponseResult<Object> error(int code, String msg) {
        return new ResponseResult<>(code, msg, null);
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
