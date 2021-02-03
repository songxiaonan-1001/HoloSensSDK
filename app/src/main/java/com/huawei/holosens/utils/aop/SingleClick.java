package com.huawei.holosens.utils.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ProjectName: HoloSens
 * @Package: com.huawei.holosens.utils.aop
 * @ClassName: SingleClick
 * @Description: java类作用描述
 * @CreateDate: 2020-02-17 16:23
 * @Version: 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SingleClick {
    /* 点击间隔时间 */
    long value() default 1000;
}
