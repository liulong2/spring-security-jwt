package com.liu.config;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liu.entity.CheckUserEntity;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;


/**
 * 验证用户名密码正确后，生成一个token，并将token返回给客户端
 * 该类继承自UsernamePasswordAuthenticationFilter，重写了其中的2个方法 ,
 * attemptAuthentication：接收并解析用户凭证。
 * successfulAuthentication：用户成功登录后，这个方法会被调用，我们在这个方法里生成token并返回。
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        super.setFilterProcessesUrl("/auth/login");
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        // 从输入流中获取到登录的信息
        try {

            //通过过滤器获得相应的数据,可以在此处理相应的逻辑(暂时预留出来的)
            CheckUserEntity loginUser= JSON.parseObject(JSON.toJSONString(request.getParameterMap()), CheckUserEntity.class);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(loginUser.getUserName(), loginUser.getPassword());
            return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (Exception e) {
            throw new Exception("用户信息异常");
        }
    }
    // 成功验证后调用的方法 reason authentication failed
    // 如果验证成功，就生成token并返回
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        System.out.println("jwtUser:" + user.toString());

        String role = "";
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        for (GrantedAuthority authority : authorities){
            role = authority.getAuthority();
        }
        //根据权限获得token
        String token = TestJwtUtils.createToken(user.getUsername(), role);
        //也可以不传权限
        //String token = TestJwtUtils.createToken(jwtUser.getUsername(), false);
        // 返回创建成功的token
        // 但是这里创建的token只是单纯的token
        // 按照jwt的规定，最后请求的时候应该是 `Bearer token`
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        String tokenStr = TestJwtUtils.TOKEN_PREFIX + token;
        //待修改请求头
        response.setHeader(TestJwtUtils.TOKEN_HEADEA,tokenStr);
    }
    //认证失败
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.getWriter().write("authentication failed, reason: " + failed.getMessage());
    }
}
