package com.fh.util;

//返回数据统一数据规范
public class JsonData {

    private Integer code;

    private String message;

    private Object data;


    private JsonData(Object data){
        this.data=data;
        this.message="success";
        this.code=200;
    }

    private JsonData(String message){
        this.code=500;
        this.message=message;
    }


    private JsonData(Integer code,String message){
        this.code=code;
        this.message=message;
    }


    public static JsonData getJsonError(Integer code,String message){
        return new JsonData(code,message);
    }


    //成功
    public static JsonData getJsonSuccess(Object data) {
        return new JsonData(data);
    }

    //失败
    public static JsonData getJsonError(String message) {
        return new JsonData(message);
    }






    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
