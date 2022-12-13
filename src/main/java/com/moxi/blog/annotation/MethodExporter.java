package com.moxi.blog.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//@interface 标识 MethodExporter为自定义的注解，MethodExporter是一个标识符
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)//这个注解保留的时间
public @interface MethodExporter {
}
