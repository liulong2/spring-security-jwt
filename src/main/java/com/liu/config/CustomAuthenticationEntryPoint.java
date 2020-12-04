package com.liu.config;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//AuthenticationEntryPoint 用来解决匿名用户访问无权限资源时的异常
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setContentType("text/javascript;charset=utf-8");
        // TODO 2020-12-14 19:16 需要定义状态码进行返回值判断
        httpServletResponse.getWriter().print(JSONObject.toJSONString("您没有权限"));

    }
}
