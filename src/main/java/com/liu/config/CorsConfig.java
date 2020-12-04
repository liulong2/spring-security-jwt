package com.liu.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liu.filter.MyInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.DateFormat;
import java.util.List;

/**
 * 实现跨域请求
 * 想要让自己的拦截器生效需要进行webmvc配置，自定义一个配置类实现WebMvcConfigurer接口，
 * 然后重写addInterceptors方法添加需要拦截的路径,JDK1.8以前都是继承于WebMvcConfigurerAdapter然后重新其中的方法，
 * 但是JDK1.8支持接口默认方法，官方就定义了可以不用继承WebMvcConfigurerAdapter直接实现WebMvcConfigurer即可。
 *
 * 自定义自己的配置类也可以继承WebMvcConfigurationSupport类。这个类有许多默认的配置，
 * 但是如果继承了WebMvcConfigurationSupport这个类，SpringBoot的mvc自动装配就好失效，
 * 默认配置都需要自己定义，如静态文件地址 /**,需要重新addResourceHandlers方法添加静态文件地址
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Autowired
    private WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter webMvcAutoConfigurationAdapter;


    @Autowired
    private Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder;


    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 1允许任何域名使用
        corsConfiguration.addAllowedOrigin("*");
        // 2允许任何头
        corsConfiguration.addAllowedHeader("*");
        // 3允许任何方法（post、get等）
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addExposedHeader("Authorization");
        return corsConfiguration;
    }

    /**
     * spring boot默认使用的json解析框架是jackson，但是功能不如fastjson好用，以下是将jackjson改为fastjson
     * 该方法使返回的对象转成json串
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        webMvcAutoConfigurationAdapter.configureMessageConverters(converters);
        /**
         * 1、需要先定义一个convert转换消息的对象
         * 2、添加fastJson的配置信息，比如：是否要格式化返回的消息
         * 3、在convert中添加配置信息
         * 4、将convert添加到时converts当中
         */

        //1、定义一个convert转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        //2、添加fastJson的配置信息，比如：是否要格式化返回的json数据
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                // 配置:要格式化返回的json数据
                SerializerFeature.PrettyFormat
        );
        //3、将convert中添加的配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);
        //4、将convert添加到converts当中
        converters.add(fastConverter);
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJsonpHttpMessageConverter() {

        ObjectMapper mapper = jackson2ObjectMapperBuilder.build();

        // ObjectMapper为了保障线程安全性，里面的配置类都是一个不可变的对象
        // 所以这里的setDateFormat的内部原理其实是创建了一个新的配置类
        DateFormat dateFormat = mapper.getDateFormat();
        // TODO: 2020/5/29 此处可能会出BUG 代码未经测试
        mapper.setDateFormat(dateFormat);

        return new MappingJackson2HttpMessageConverter(mapper);
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .maxAge(3600);
    }

    //调用拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyInterceptor()).addPathPatterns("/*");
    }
}
