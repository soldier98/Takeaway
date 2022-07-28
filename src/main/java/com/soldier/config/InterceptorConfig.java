package com.soldier.config;

import com.alibaba.fastjson2.support.spring.messaging.converter.MappingFastJsonMessageConverter;
import com.soldier.common.jacksonObjectMapper;
import com.soldier.controller.interceptor.LoginCheckInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 【登录拦截器配置】
 *
 * @author soldier97
 * @date 2022/7/21 10:03
 */
@Configuration
@ComponentScan("com.soldier.controller.interceptor")
@Slf4j
@EnableWebMvc
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private LoginCheckInterceptor interceptor;

    //  用 WebMvcConfigurer 配置类后，springboot的默认配置就会失效，需要重新配置静态资源路径等.......
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }

    //不拦截的请求
    private final List<String> EXCLUDE_URLS = Arrays.asList("/employee/login",
            "/employee/logout",
            "/backend/**",
            "/front/**");

    //
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).addPathPatterns("/**")
                .excludePathPatterns(EXCLUDE_URLS);
    }
/**
 *
 * @Author soldier
 * @Description //MVC 转换框架
 * @Date  2022/7/28
 * @param converters
 * @return void
 **/
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器");
        //新建消息转换器
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

        //设置对象转换器，底层使用Jackson将Java对象转为json
        messageConverter.setObjectMapper(new jacksonObjectMapper());
        //将上面的消息转换器对象追加到mvc框架的转换器集合中
        converters.add(0,messageConverter);
    }
}
