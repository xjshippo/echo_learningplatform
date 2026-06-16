package com.jxau.util;


public class ResultEntity<T> {

    public static final String SUCCESS="SUCCESS";
    public static final String FALSE="FALSE";
    public static final String NO_MESSAGE="NO_MESSAGE";
    public static final String NO_DATE="NO_DATE";

    private String status;
    private String result;
    private String message;
    private T data;

    public static <E> ResultEntity<E> successWithoutData(){

        return new ResultEntity("200",SUCCESS,NO_MESSAGE,null);
    }
    public static <E> ResultEntity<E> successWithData(E data){

        return new ResultEntity("200",SUCCESS,NO_MESSAGE,data);
    }

    public static <E> ResultEntity<E> falseWithoutData(String message){

        return new ResultEntity(FALSE,message,null);
    }
    public static <E> ResultEntity<E> falseWithoutData(String message,String code){

        return new ResultEntity(code,FALSE,message,null);
    }



    public ResultEntity() {
    }

    public ResultEntity(String result, String message, T data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }

    public ResultEntity(String code ,String result, String message, T data) {
        this.status = code;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    @Override
    public String toString() {
        return "ResultEntity{" +
                "code='" + status + '\'' +
                ", result='" + result + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
