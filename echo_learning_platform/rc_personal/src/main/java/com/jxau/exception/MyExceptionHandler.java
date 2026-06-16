package com.jxau.exception;

import com.jxau.util.ResultEntity;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一异常处理类
 */
@ControllerAdvice
public class MyExceptionHandler {
 
    private static Logger logger = LoggerFactory.getLogger(MyExceptionHandler.class);
 
    /**
     * token过期异常处理
     */
    @ResponseStatus(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED)
    @ExceptionHandler(TokenExpireException.class)
    @ResponseBody
    public ResultEntity TokenExpireException(Exception e) {

        return ResultEntity.falseWithoutData("身份信息已过期，请重新登录！",String.valueOf(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value()));
    }

    @ResponseStatus(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED)
    @ExceptionHandler(TokenIsNoneException.class)
    @ResponseBody
    public ResultEntity TokenIsNoneException(Exception e) {

        return ResultEntity.falseWithoutData("身份信息缺失，登录异常！",String.valueOf(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value()));
    }

    @ResponseStatus(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED)
    @ExceptionHandler(TokenErrorException.class)
    @ResponseBody
    public ResultEntity TokenErrorException(Exception e) {

        return ResultEntity.falseWithoutData("身份信息异常，登录失败！",String.valueOf(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value()));
    }
}