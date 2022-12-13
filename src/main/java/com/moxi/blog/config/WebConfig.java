package com.moxi.blog.config;

import com.moxi.blog.intercepter.LoginIntercepter;
import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public LoginIntercepter loginIntercepter() {
        LoginIntercepter loginIntercepter = new LoginIntercepter();
        return loginIntercepter;
    }

    //注册指定的拦截器来拦截路径，拦截器的配置部分
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注意这里的**
        registry.addInterceptor(loginIntercepter()).addPathPatterns("/api/v1/pri/admin/**")
        .excludePathPatterns("/api/v1/pri/admin/login","/api/v1/pri/admin");
    }

    @Bean
    public Validator validator() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                // 快速失败模式
                .failFast(true)
                .buildValidatorFactory();
        return validatorFactory.getValidator();
    }


}
