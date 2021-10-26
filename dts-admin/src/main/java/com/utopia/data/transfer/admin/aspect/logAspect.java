package com.utopia.data.transfer.admin.aspect;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;

/**
 * describe:
 *
 * @author niuyaze
 * @date 2021/10/22
 */
@Slf4j
@Aspect
@Component
public class logAspect {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void controllerCut() {
    }

    @Pointcut("execution(* com.utopia.data.transfer.admin.service.impl..*(..))")
    public void serviceCut() {
    }

    @Around(value = "controllerCut()")
    public Object controllerAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Map<String, Object> paramsMap = Maps.newHashMap();
        MethodSignature methodSignature = getMethodParams(joinPoint, paramsMap);
        log.info("api_start, className:{}, methodName:{}, params:{}", className, methodSignature.getName(), JSON.toJSONString(paramsMap));
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        log.info("api_end, className:{}, methodName:{}, result:{}, cost:{}ms", className, methodSignature.getName(), JSON.toJSONString(result), System.currentTimeMillis() - startTime);
        return result;
    }

    @Around(value = "serviceCut()")
    public Object methodAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Map<String, Object> paramsMap = Maps.newHashMap();
        MethodSignature methodSignature = getMethodParams(joinPoint, paramsMap);
        long startTime = System.currentTimeMillis();
        log.info("api_going, className:{}, methodName:{}, params:{}", className, methodSignature.getName(), JSON.toJSONString(paramsMap));
        Object result = joinPoint.proceed();
        log.info("api_end, className:{}, methodName:{}, result:{}, cost:{}ms", className, methodSignature.getName(), JSON.toJSONString(result), System.currentTimeMillis() - startTime);
        return result;
    }


    private MethodSignature getMethodParams(JoinPoint joinPoint, Map<String, Object> paramsMap) {
        Object[] args = joinPoint.getArgs();
        Signature sig = joinPoint.getSignature();
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        MethodSignature methodSignature = (MethodSignature) sig;
        String[] parameterNames = methodSignature.getParameterNames();
        for (int i = 0; i < parameterNames.length; i++) {
            if (args[i] instanceof File || args[i] instanceof MultipartFile) {
                continue;
            }
            paramsMap.put(parameterNames[i], String.valueOf(args[i]));
        }
        paramsMap.remove("request");
        paramsMap.remove("parameters");
        return methodSignature;
    }
}
