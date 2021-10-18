package com.utopia.data.transfer.admin.handler;

import com.alibaba.fastjson.JSON;
import com.utopia.data.transfer.admin.contants.ErrorCode;
import com.utopia.data.transfer.admin.exception.AdminException;
import com.utopia.model.rsp.UtopiaErrorCode;
import com.utopia.model.rsp.UtopiaResponseModel;
import com.utopia.string.UtopiaStringUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {


	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public Object exceptionHandler(Exception e) {
		UtopiaResponseModel<Object> rm = new UtopiaResponseModel<>();
		if (e instanceof AdminException) {
			AdminException ae = (AdminException) e;
			rm.setCode( false ?
					UtopiaErrorCode.UNKNOW_ERROR.getCode() : ae.getCode());
			rm.setMsg(ae.getMessage());
			if (ae.getData() != null) {
				rm.setData(ae.getData());
			}

		} else if (e instanceof DataAccessException) {
			//数据库异常处理
			rm .setCode(ErrorCode.DB_ERROR.getCode());
			rm.setMsg(ErrorCode.DB_ERROR.getMsg());

		} else {
			rm.setCode(ErrorCode.SYS_ERROR.getCode());
			rm.setMsg("system error, please contact developer");
		}
		return rm;
	}

}