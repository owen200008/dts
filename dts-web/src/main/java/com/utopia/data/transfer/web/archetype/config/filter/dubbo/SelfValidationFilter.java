package com.utopia.data.transfer.web.archetype.config.filter.dubbo;

import com.utopia.data.transfer.model.archetype.BaseErrorCode;
import com.utopia.model.rsp.UtopiaResponseModel;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.AppResponse;
import org.apache.dubbo.rpc.AsyncRpcResult;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;


@Slf4j
@Activate(order = 10010)
public class SelfValidationFilter implements Filter {

  private final Validator validator;

  public SelfValidationFilter() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @SuppressWarnings("rawtypes")
  @Override
  public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
    if (this.validator != null && !invocation.getMethodName().startsWith("$")) {
      try {
        StringBuilder stringBuilder = new StringBuilder();
        Object[] arguments = invocation.getArguments();
        for (Object arg : arguments) {
          Set<ConstraintViolation<Object>> violations = validator.validate(arg);
          if (!violations.isEmpty()) {
            for (ConstraintViolation violation : violations) {
              stringBuilder.append(violation.getPropertyPath()).append(":")
                      .append(violation.getMessage()).append("|");
            }
          }
        }

        if (StringUtils.isNotBlank(stringBuilder.toString())) {
          log.error("{} error, {}", this.getClass().getSimpleName(), stringBuilder.toString());
          UtopiaResponseModel<String> em = new UtopiaResponseModel<>(
                  BaseErrorCode.REQUEST_PARAM_INVALID);
          if (stringBuilder.toString().length() > 0) {
            em.setMsg(stringBuilder.substring(0, stringBuilder.toString().length() - 1));
          }
          return new AsyncRpcResult(
                  CompletableFuture.completedFuture(new AppResponse(em)), invocation);
        }
      } catch (RpcException var4) {
        throw var4;
      } catch (Throwable var5) {
        return new AsyncRpcResult(CompletableFuture.completedFuture(
                new AppResponse(new UtopiaResponseModel<>(BaseErrorCode.SYSTEM_ERROR))),
                invocation);
      }
    }
    return invoker.invoke(invocation);
  }
}
