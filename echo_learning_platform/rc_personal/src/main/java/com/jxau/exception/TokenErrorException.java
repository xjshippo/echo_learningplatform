package com.jxau.exception;

public class TokenErrorException extends RuntimeException {

    //错误吗
    private Integer code;

    //错误信息
    private String msg;

    public TokenErrorException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}