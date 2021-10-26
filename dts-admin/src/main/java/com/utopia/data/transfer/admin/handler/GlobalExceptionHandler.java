package com.utopia.data.transfer.admin.handler;

import com.utopia.data.transfer.model.archetype.ErrorCode;
import com.utopia.data.transfer.model.archetype.ServiceException;
import com.utopia.exception.UtopiaRunTimeException;
import com.utopia.model.rsp.UtopiaErrorCode;
import com.utopia.model.rsp.UtopiaResponseModel;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {


	@ExceptionHandler(value = Throwable.class)
	@ResponseBody
	public Object exceptionHandler(Throwable e) {
		UtopiaResponseModel<Object> rm = new UtopiaResponseModel<>();
		if (e instanceof DataAccessException) {
			//数据库异常处理
			rm.setCode(ErrorCode.DB_ERROR.getCode());
			rm.setMsg(ErrorCode.DB_ERROR.getMessage());
		} else if (e instanceof UtopiaRunTimeException) {
			UtopiaRunTimeException exception = ((UtopiaRunTimeException) e);
			//数据库异常处理
			rm.setCode(exception.getUtopiaErrorCodeClass().getCode());
			rm.setMsg(exception.getUtopiaErrorCodeClass().getMessage());
		} else if (e instanceof ServiceException) {
			ServiceException exception = ((ServiceException) e);
			//数据库异常处理
			rm.setCode(exception.getCode());
			rm.setMsg(exception.getMsg());
		} else {
			rm.setCode(ErrorCode.UNKNOW_ERROR.getCode());
			rm.setMsg("system error, please contact developer");
		}
		return rm;
	}

}