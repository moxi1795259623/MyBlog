package com.moxi.blog.exception;

import com.moxi.blog.utils.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class CustomExException {

    /*
     * 有关参数校验：
     * 1. 方法：引入hibernate-validator依赖，结合@Valid或者@Validation注解
     * 2. 参数校验不通过时，抛出以下异常
     * （1）POST、PUT请求使用requestBody会抛出MethodArgumentNotValidException异常
     *      MethodArgumentNotValidException extends BindException
     * （2）requestParam/PathVariable参数校验会抛出ConstraintViolationException异常
     *     ConstraintViolationException extends ValidationException
     * 3. 在实际项目开发中，通常会用统一异常处理来返回一个更友好的提示。
     * 比如我们系统要求无论发送什么异常，http的状态码必须返回200，由业务码去区分系统的异常情况。
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK) //状态码OK
    @ResponseBody
    public JsonData handleMethodArgumentNotValidException(BindException ex, HttpServletRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder sb = new StringBuilder("校验失败-");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getField()).append("-").append(fieldError.getDefaultMessage()).append(", ");
        }
        //记录日志
        log.error("MethodArgumentNotValidException -> Request URL:{},Exception:{}", request.getRequestURI(), ex.getMessage());
        //返回前端
        return JsonData.buildError(sb.toString());
    }


    @ExceptionHandler(Exception.class)
    public ModelAndView handle(HttpServletRequest request, Exception e) throws Exception {
        //记录日志
        log.error("Exception -> Request URL:{},Exception:{}", request.getRequestURI(), e.getMessage());
        //标注了ResponseStatus标签的自定义异常，就抛出异常给springboot管理，springboot的BeanName视图解析器根据状态码404,相应到对应的页面
        if (null != AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class)) {
            throw e;
        }
        //封装到JsonData或者返回页面 JsonData.buildError(oneException.getCode(), oneException.getMessage());
        ModelAndView mv = new ModelAndView();
        mv.addObject("url", request.getRequestURL());
        mv.addObject("exception", e);
        mv.setViewName("error/error");
        return mv;
    }
}
