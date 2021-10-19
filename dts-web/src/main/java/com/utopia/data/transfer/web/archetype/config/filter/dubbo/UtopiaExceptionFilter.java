package com.utopia.data.transfer.web.archetype.config.filter.dubbo;

import static org.apache.dubbo.common.constants.CommonConstants.PROVIDER;

import com.utopia.exception.UtopiaException;
import com.utopia.model.rsp.UtopiaResponseModel;
import com.utopia.data.transfer.model.archetype.ServiceException;
import com.utopia.data.transfer.model.archetype.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.service.GenericService;

/**
 * RequestFilter
 *
 * @author baoyuliang
 */
@Slf4j
@Activate(group = PROVIDER, order = 10020)
public class UtopiaExceptionFilter implements Filter, Filter.Listener {

    /**
     * exception handler
     *
     * @param invoker
     * @param invocation
     * @return
     * @throws RpcException
     */
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        return invoker.invoke(invocation);
    }

    @Override
    public void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation) {
        if (appResponse.hasException() && GenericService.class != invoker.getInterface()) {
            try {
                Throwable exception = appResponse.getException();
                log.error("An error occurred: {}", exception.getMessage(), exception);
                appResponse.setException(null);
                if (exception instanceof ServiceException) {
                    ServiceException serviceException = (ServiceException) exception;
                    appResponse.setValue(new UtopiaResponseModel<>(serviceException.getCode(),
                            serviceException.getMsg(), null));
                    return;
                }
                if (exception instanceof UtopiaException) {
                    appResponse.setValue(new UtopiaResponseModel<>(
                            ((UtopiaException) exception).getUtopiaErrorCodeClass()));
                    return;
                }
                appResponse.setValue(new UtopiaResponseModel<>(ErrorCode.SYSTEM_ERROR));
            } catch (Throwable e) {
                log.warn("Fail to UtopiaExceptionFilter when called by " + RpcContext.getContext()
                        .getRemoteHost() + ". service: " + invoker.getInterface().getName()
                        + ", method: " + invocation.getMethodName() + ", exception: " + e.getClass()
                        .getName() + ": " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void onError(Throwable t, Invoker<?> invoker, Invocation invocation) {
    }
}