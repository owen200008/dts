package com.utopia.data.transfer.admin.contants;

import lombok.Data;

/**
 * describe:
 *
 * @author niuyaze
 * @date 2021/10/15
 */

public enum ErrorCode {



    SYS_ERROR(10003,"system error!"),
    DB_ERROR(10004,"db error"),




    PARSE_OBJECT_FAIL(10005,"parse object error!");

    private int code;
    private String msg;

    ErrorCode() {
    }

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
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
}
