package com.moxi.blog.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;


//1. 测试两个切面的请求顺序：
// around ->before -> 目标输出-> AfterReturning ->doAfter ->around
// around ->before -> 目标输出-> afterThrowing1 ->doAfter ->around
// (1) around1(环绕前) -> before1 -> around2(环绕前)-> before2 ->目标输出-> doAfterRuturn2 -> doAfter2 ->around2(环绕后) -> doAfterRuturn1 -> doAfter1 ->around1(环绕后)
// (2) around1(环绕前) -> before1 -> around2(环绕前)-> before2 -> 目标输出-> afterThrowing2 -> doAfter2 -> afterThrowing1 -> doAfter1

//2. 从Spring5.2.7开始，在相同@Aspect类中，通知方法将根据其类型按照从高到低的优先级进行执行：
//@Around，@Before ，@After，@AfterReturning，@AfterThrowing。5.3之后after在后

//3. @Around的报错没有解决【待改】->改完：方法返回值要继续接收并返回


/**
 * 由于我想记录controller包下的：入参和返回值这类共有信息，所以通过切入点的方式做一个整体拦截
 * 有人说可以避免背锅，有道理，而且也方便调试把
 */
@Aspect
@Component
@Slf4j
@Order(1)
public class LogAspect {
    long start, end;

    @Pointcut("execution(* com.moxi.blog.controller.*.*(..))")
    public void log() {
    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("------------before-----------");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ip = request.getRemoteAddr();
        String url = request.getRequestURL().toString();
        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        RequestLog requestLog = new RequestLog(url, ip, classMethod, args);
        log.info("Before Request : {}", requestLog);
    }

    //最终通知
    @After("log()")
    public void doAfter() {
        end = System.currentTimeMillis();
        log.info("Time: {}ms", (end - start));
        log.info("-------------end------------");
    }

    @Around("log()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        start = System.currentTimeMillis();
        //被增强的方法执行,如果原方法有返回值，这里一定要返回，否则由于spring由于收到null就会报错【折磨人的大坑】
        Object result = proceedingJoinPoint.proceed();
        return result;
    }

    //抛出后会被CustomExException捕获，记录日志并跳转到error页面
    @AfterThrowing(pointcut = "log()", throwing = "e")
    public void afterThrowing(Exception e) throws Exception {
        log.error("Exception:{}", e.getMessage());
        throw e;
    }

    //后置通知（返回通知）
    @AfterReturning(returning = "result", pointcut = "log()")
    public void doAfterRuturn(Object result) {
        log.info("AfterReturning Result : {}", result);
    }

    private class RequestLog {
        private String url;
        private String ip;
        private String classMethod;
        private Object[] args;

        public RequestLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }

        @Override
        public String toString() {
            return "{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }
}
