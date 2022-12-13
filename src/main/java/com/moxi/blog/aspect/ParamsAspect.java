package com.moxi.blog.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 这个用于初始的测试学习。之后如果有一些专门要记录个别信息的【关键方法】时，再修改这个类的逻辑，然后
 * 在关键方法上标注这个注解。至此，这两个切面类分工完成
 */
@Aspect
@Component
@Slf4j
@Order(2)
public class ParamsAspect {
    //Around是功能最强大的注解，可以控制入参，执行和返回结果
    //@annotation表明：哪个方法标注了里面的注解，就执行@Around方法
    @Around("@annotation(com.moxi.blog.annotation.MethodExporter)")
    public Object methodExporter(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long end = System.currentTimeMillis();

        //将参数和结果包装，并转成json序列化，模拟上报
        ObjectMapper mapper = new ObjectMapper();
        String jsonParam = mapper.writeValueAsString(joinPoint.getArgs());
        String jsonResult = null;
        if (proceed != null) {
            jsonResult = mapper.writeValueAsString(proceed);
        } else {
            jsonResult = null;
        }
        //模拟上传
        log.info("{} 执行时间为：{}ms , JsonParam:{} , JsonResult:{}", joinPoint.getTarget().getClass().getName(), (end - start), jsonParam, jsonResult);
        return proceed;
    }
}
