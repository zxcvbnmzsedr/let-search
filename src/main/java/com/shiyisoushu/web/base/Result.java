/*
 * Copyright © 2017 - 2020 Cnabc. All Rights Reserved.
 */
package com.shiyisoushu.web.base;


import java.io.Serializable;

/**
 * @author zhouqilin on 2018/7/27
 * @since v1.0.0
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -3915155489196431489L;
    private String msg;
    private T data;
    private Integer code;

    protected Result() {
    }

    private Result(String msg, T t, Integer code) {
        this.setMsg(msg);
        this.setData(t);
        this.setCode(code);
    }


    public static <T> Result<T> error() {
        return error(null);
    }

    public static <T> Result<T> error(String msg) {
        return new Result(msg == null ? "error" : msg, null, -1);
    }


    public static <T> Result<T> ok(String msg, T t) {
        return Result.buildResult(msg, t, 0);
    }

    public static <T> Result<T> ok(T t) {
        return Result.buildResult("success", t, 0);
    }

    public static <T> Result ok() {
        return Result.buildResult(0);
    }

    public static <T> Result<T> buildResult(Integer code) {
        return Result.buildResult(null, code);
    }


    public static <T> Result<T> buildResult(String msg, Integer code) {

        return new Result(msg == null ? "处理成功" : msg, null, code);
    }

    public static <T> Result<T> buildResult(String msg, T t, Integer code) {
        return new Result<>(msg, t, code);
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

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
