package com.jxau.util;



/**
 * @author cheng
 */

public  class JsonResult<T> {
    public static final String SUCCESS="SUCCESS";
    public static final String FALSE="FALSE";
    public static final String NO_MESSAGE="NO_MESSAGE";
    public static final String NO_DATE="NO_DATE";

    private String result;
    private String message;
    private T data;

    public static <E> JsonResult<E> successWithoutData(){

        return new JsonResult(SUCCESS,NO_MESSAGE,null);
    }
    public static <E> JsonResult successWithData(E data){

        return new JsonResult(SUCCESS,NO_MESSAGE,data);
    }

    public static <E> JsonResult<E> falseWithoutData(String message){

        return new JsonResult(FALSE,message,null);
    }



    public JsonResult() {
    }

    public JsonResult(String result, String message, T data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }

    public static void FALSE(String message) {
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "JsonResult{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                ", Date=" + data +
                '}';
    }
}
