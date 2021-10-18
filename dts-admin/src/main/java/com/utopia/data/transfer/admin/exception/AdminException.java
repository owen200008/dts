package com.utopia.data.transfer.admin.exception;

import com.utopia.data.transfer.admin.contants.ErrorCode;

/**
 * describe:
 *
 * @author niuyaze
 * @date 2021/10/15
 */
public class AdminException extends RuntimeException {
    private int code;
    private String msg;
    private Object data;

    public AdminException(ErrorCode code) {
        super(code.getMsg());
        this.code = code.getCode();
        this.msg = code.getMsg();
    }

    public AdminException(ErrorCode code, Throwable cause) {
        super(code.getMsg(), cause);
        this.code = code.getCode();
        this.msg = code.getMsg();
    }

    public AdminException(ErrorCode code, Object data) {
        super(code.getMsg());
        this.code = code.getCode();
        this.msg = code.getMsg();
        this.data = data;
    }

    public AdminException(ErrorCode code, Object data, Throwable cause) {
        super(code.getMsg(), cause);
        this.code = code.getCode();
        this.msg = code.getMsg();
        this.data = data;
    }

    public AdminException(int code, String msg, Object data) {
        super(msg);
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public AdminException(int code, String msg, Object data, Throwable cause) {
        super(msg, cause);
        this.code = code;
        this.msg = msg;
        this.data = data;
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
