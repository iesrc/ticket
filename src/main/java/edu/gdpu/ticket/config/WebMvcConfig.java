package edu.gdpu.ticket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:iesrc
 * @Crate:2022/11/18 15:03
 * 这段代码是注册了一个拦截器并配置了拦截器拦截的路径和不拦截的路径。
 *
 * 首先定义了一个 List 类型的 paths 变量，其中存储了一些不需要拦截的路径，如登录相关的路径、静态资源路径等。
 *
 * 然后通过 registry.addInterceptor() 方法注册了一个 UserInterceptor 拦截器，
 * 该拦截器实现了 HandlerInterceptor 接口，可以在请求处理之前或之后进行一些额外的处理。
 *
 * .addPathPatterns("/**") 表示该拦截器拦截所有的请求路径。
 *
 * .excludePathPatterns(paths) 表示将 paths 中存储的路径排除在拦截范围之外，也就是这些路径不会被该拦截器拦截。
 *
 * 综合起来，这段代码的作用是在 Spring Boot 项目中注册了一个拦截器，拦截除了 paths 中存储的路径之外的所有请求，
 * 用来实现用户登录验证、权限校验等功能。
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> paths=new ArrayList<>();
        paths.add("/login");
        paths.add("/doLogin");
        paths.add("/register");
        paths.add("/doRegister");
        paths.add("/defaultKaptcha");
        paths.add("/css/**");
        paths.add("/images/**");
        paths.add("/js/**");
        paths.add("/layui/**");
        registry.
          addInterceptor(new UserInterceptor()).
            addPathPatterns("/**").excludePathPatterns(paths);
    }
}

