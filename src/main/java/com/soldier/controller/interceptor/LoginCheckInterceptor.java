package com.soldier.controller.interceptor;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.core.JsonGenerator;
import com.soldier.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 【用户未登录时，拦截接口请求，不向前端展示数据】
 *
 * @author soldier97
 * @date 2022/7/20 18:11
 */
@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    //如果会话中无用户信息，拦截
        if (request.getSession().getAttribute("employee") == null) {
            log.info("URL => {},session为空，不执行controller -- :{}",request.getRequestURL(),handler.getClass().getName());
            //配合前端代码，返回code为0，和msg为NOLOGIN 则跳转登陆界面
//            response.getWriter().write(JSONUtils.toJSONString(new Result<String>(0,"NOTLOGIN",null)));
            response.getWriter().write(JSON.toJSONString(new Result<String>(0,"NOTLOGIN",null)));
            return false;
        } else {
            log.info("session中employee是{}，执行controller -- :{}",request.getSession().getAttribute("employee"),handler.getClass().getName());
            return true;    //如果会话中有用户信息，则已经登录，返回true，不做拦截
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
