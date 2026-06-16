package com.jxau.annotations;


import java.lang.annotation.*;

@Target({ElementType.PARAMETER,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemControllerLog {

    // 描述操作
    String descrption() default "";
    // 操作的类型
    String actionType() default "";
}
