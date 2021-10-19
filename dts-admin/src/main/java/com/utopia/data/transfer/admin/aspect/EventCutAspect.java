package com.utopia.data.transfer.admin.aspect;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 标识这是一个切面
 */
@Aspect
@Slf4j
@Service
public class EventCutAspect {

    public interface EventCutFunction{
        String getKey();

        Class getEventCutClass();

        void doInvoke(Object o, int eventId, int subEventId);
    }

    Map<String, Map<Class, EventCutFunction>> map = new HashMap<>();

    public EventCutAspect(){
    }

    public void register(EventCutFunction function){
        Map<Class, EventCutFunction> classEventCutFunctionMap = map.get(function.getKey());
        if(Objects.isNull(classEventCutFunctionMap)){
            classEventCutFunctionMap = Maps.newHashMap();
            map.put(function.getKey(), classEventCutFunctionMap);
        }
        classEventCutFunctionMap.put(function.getEventCutClass(), function);
    }

    // 以自定义 @EventCut 注解为切点
    @Pointcut("@annotation(com.utopia.data.transfer.admin.aspect.EventCut)")
    public void eventCut() {
    }

    /**
     * 环绕
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("eventCut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Signature signature = proceedingJoinPoint.getSignature();
        //获取参数名
        MethodSignature methodSignature = (MethodSignature) signature;
        EventCut annotation = methodSignature.getMethod().getAnnotation(EventCut.class);

        Object result = proceedingJoinPoint.proceed();

        Map<Class, EventCutFunction> classEventCutFunctionMap = map.get(annotation.key());
        if(classEventCutFunctionMap == null){
            log.error("EventCutAspect map is null!!! {}", annotation.key());
            return result;
        }
        EventCutFunction eventCutFunction = classEventCutFunctionMap.get(result.getClass());
        if(eventCutFunction == null){
            log.error("EventCutAspect class cut no find {}", result.getClass().getName());
            return result;
        }
        eventCutFunction.doInvoke(result, annotation.eventId(), annotation.subEventId());
        return result;
    }
}
