package com.utopia.data.transfer.admin.aspect;

import java.lang.annotation.*;

/**
 * @author owen.cai
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Documented
public @interface EventCut {
    /**
     * 标识符
     * @return
     */
    String key();

    /**
     * 切入的事件
     * @return
     */
    int eventId() default 0;
    int subEventId() default 0;
}
