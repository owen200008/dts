package com.utopia.data.transfer.web.archetype.config;

import com.utopia.data.transfer.model.archetype.BaseErrorCode;
import com.utopia.data.transfer.model.archetype.ErrorCode;
import com.utopia.data.transfer.model.archetype.ServiceException;
import com.utopia.model.rsp.UtopiaResponseModel;
import java.util.List;
import javax.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 全局异常捕获处理
 *
 * @author: czq
 * @date: 19/6/27
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public UtopiaResponseModel<String> processValidationError(MethodArgumentNotValidException ex) {
        return handleBindingResult(ex.getBindingResult());
    }

    @ExceptionHandler(BindException.class)
    public UtopiaResponseModel<String> processValidationError(BindException ex) {
        return handleBindingResult(ex.getBindingResult());
    }

    UtopiaResponseModel<String> handleBindingResult(BindingResult result) {
        List<FieldError> fieldErrors = result.getFieldErrors();
        StringBuilder stringBuilder = new StringBuilder();
        for (FieldError fieldError : fieldErrors) {
            stringBuilder.append(fieldError.getField()).append(":")
                    .append(fieldError.getDefaultMessage())
                    .append("|");
        }
        return ErrorCode.createResponse(BaseErrorCode.REQUEST_PARAM_INVALID, stringBuilder);
    }

    @ExceptionHandler(ServletException.class)
    public UtopiaResponseModel processSpringException(ServletException ex) {
        if (ex instanceof MissingServletRequestParameterException) {
            log.error("here comes error, handleException, error:{}", ex.getMessage());
            return ErrorCode.createResponse(BaseErrorCode.REQUEST_PARAM_MISS, ex.getMessage());
        } else {
            log.error("here comes error, handleException, ", ex);
            return new UtopiaResponseModel(BaseErrorCode.SYSTEM_ERROR);
        }
    }

    @ExceptionHandler(Exception.class)
    public UtopiaResponseModel processSpringException(Exception ex) {
        return exceptionToModel(log, ex);
    }

    public static UtopiaResponseModel exceptionToModel(Logger log, Throwable ex) {
        boolean printDetail = false;
        UtopiaResponseModel<String> em = null;
        if (ex instanceof IllegalArgumentException) {
            printDetail = true;
            em = new UtopiaResponseModel(BaseErrorCode.REQUEST_PARAM_INVALID);
        } else if (ex instanceof ServiceException) {
            em = new UtopiaResponseModel(((ServiceException) ex).getCode(),
                    ((ServiceException) ex).getMsg(), null);
        } else {
            printDetail = true;
            em = new UtopiaResponseModel(BaseErrorCode.SYSTEM_ERROR);
        }
        if (printDetail) {
            log.error("here comes error, handleException,", ex);
        } else {
            log.error("here comes error, handleException {}", ex.getMessage());
        }
        return em;
    }
}