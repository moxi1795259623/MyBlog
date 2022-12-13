package com.moxi.blog.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/*
异常部分的源码
从自动配置类的包里error文件夹的配置类入手，看容器中配置了哪些组件
* @RequestMapping("${server.error.path:${error.path:/error}}")
public class BasicErrorController extends AbstractErrorController {
}  处理/error请求，页面响应ModelAndView("view"),BeanNameViewResolver根据id去容器中找到一个id=error的view，
* 并渲染StatView-》白页
*
* DefaultErrorViewResolver处理4xx 5xx
* 	public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
		ModelAndView modelAndView = resolve(String.valueOf(status.value()), model);
		if (modelAndView == null && SERIES_VIEWS.containsKey(status.series())) {
			modelAndView = resolve(SERIES_VIEWS.get(status.series()), model);
		}
		return modelAndView;
	}
	private ModelAndView resolve(String viewName, Map<String, Object> model) {
	String errorViewName = "error/" + viewName; error目录在这里就拼接上了，viewName是状态码
* */

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BlogNotFoundException extends RuntimeException {
    public BlogNotFoundException() {
    }

    public BlogNotFoundException(String message) {
        super(message);
    }

    public BlogNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
