package com.jxau.util;


public  class JsonResult<T> {
    private static final String SUCCEED_MESSAGE = "succeed";
    private static final String FAILED_MESSAGE = "failed";
    public static final String NO_MESSAGE="NO_MESSAGE";
    public static final String SUCCEED="SUCCEED";
    public static final String FAILED="FAILED";
    private T data;
    private String result;
    private String msg;

    /**
     * 若没有数据返回，默认状态码为 0，提示信息为“操作成功！”
     */
    public JsonResult() {
        this.result = "0";
        this.msg = "操作成功！";
    }

    /**
     * 若没有数据返回，可以人为指定状态码和提示信息
     * @param code
     * @param msg
     */
    public JsonResult(String code, String msg) {
        this.result = code;
        this.msg = msg;
    }

    /**
     * 有数据返回时，状态码为 0，默认提示信息为“操作成功！”
     * @param data
     */
    public JsonResult(T data) {
        this.data = data;
        this.result = "0";
        this.msg = "操作成功！";
    }

    /**
     * 有数据返回，状态码为 0，人为指定提示信息
     * @param data
     * @param msg
     */
    public JsonResult(T data, String msg) {
        this.data = data;
        this.result = "0";
        this.msg = msg;
    }
    // 省略 get 和 set 方法

    public JsonResult(String result, String message, T data) {
        this.result = result;
        this.msg = message;
        this.data = data;
    }

    /**
     * 成功并且没有数据要返回
     * @param <E>
     * @return
     */
    public static <E> JsonResult<E> successWithoutData(){

        return new JsonResult(SUCCEED,NO_MESSAGE,null);
    }

    /**
     * 成功并且有数据要返回
     * @param data
     * @param <E>
     * @return
     */
    public static <E> JsonResult<E> successWithData(E data){

        return new JsonResult(SUCCEED,NO_MESSAGE,data);
    }

    /**
     * 失败返回指定错误信息
     * @param message
     * @param <E>
     * @return
     */
    public static <E> JsonResult<E> falseWithoutData(String message){

        return new JsonResult(FAILED,message,null);
    }



}
