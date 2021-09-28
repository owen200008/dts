package com.utopia.data.transfer.core.archetype.base;

import com.utopia.model.rsp.UtopiaErrorCodeClass;

public class ServiceException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private int code;
  private String msg;

  public ServiceException(UtopiaErrorCodeClass errorCode) {
    super(errorCode.getMessage());
    this.code = errorCode.getCode();
    this.msg = errorCode.getMessage();
  }

  public ServiceException(UtopiaErrorCodeClass errorCode, Throwable cause) {
    super(errorCode.getMessage(), cause);
    this.code = errorCode.getCode();
    this.msg = errorCode.getMessage();
  }

  public ServiceException(int code, String msg) {
    super(msg);
    this.code = code;
    this.msg = msg;
  }

  public ServiceException(int code, String msg, Throwable cause) {
    super(msg, cause);
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

  @Override
  public synchronized Throwable fillInStackTrace() {
    return this;
  }
}