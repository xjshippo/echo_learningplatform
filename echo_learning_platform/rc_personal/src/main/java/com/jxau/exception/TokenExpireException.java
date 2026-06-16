package com.jxau.exception;

public class TokenExpireException extends RuntimeException {

    //错误吗
    private Integer code;

    //错误信息
    private String msg;

    public TokenExpireException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}