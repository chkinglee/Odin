package org.chkinglee.norn.odin.controller.dto;

/**
 * TODO
 *
 * @Author: lilinzhen
 * @Version: 2020/5/30
 **/
public class ResultResponse<T> {

    private int code;
    private String message;
    private T detail;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getDetail() {
        return detail;
    }

    public void setDetail(T detail) {
        this.detail = detail;
    }

    public ResultResponse(int code, String message, T detail) {
        this.code = code;
        this.message = message;
        this.detail = detail;
    }
}
