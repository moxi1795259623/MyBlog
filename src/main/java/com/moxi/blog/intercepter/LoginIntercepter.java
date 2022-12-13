package com.moxi.blog.intercepter;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//主要做验证逻辑
public class LoginIntercepter implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getSession().getAttribute("username") == null || request.getSession().getAttribute("pwd") == null) {
            //没有登录的话，就没有权限范围指定页面
            response.sendRedirect("/api/v1/pri/admin");
            return false;
        }
        return true;
    }
}
