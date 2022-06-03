package com.cos.jwt.config;

import com.cos.jwt.filter.MyFilter1;
import com.cos.jwt.filter.MyFilter2;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    // 필터를 SecurtyConfig 에서 호출하지 않아도.. 즉, secruityFilterChain 에 등록하지 않아도
    // 아래의 코드처럼 적으면 filter 가 알아서 실행된다.
    // secruityFilterChain 에 등록된 필터가 먼저 실행되고 여기에 등록된 필터가 이후 실행된다.
    // https://velog.io/@sa833591/Spring-Security-5

    @Bean
    public FilterRegistrationBean<MyFilter1> filter1(){
        FilterRegistrationBean<MyFilter1> bean = new FilterRegistrationBean<>(new MyFilter1());
        bean.addUrlPatterns("/*"); // /* 로 해야함 (/** 이거 안됨)
        bean.setOrder(1); // 번호가 낮을수록 필터중 가장 먼저 실행됨

        return bean;
    }

    @Bean
    public FilterRegistrationBean<MyFilter2> filter2(){
        FilterRegistrationBean<MyFilter2> bean = new FilterRegistrationBean<>(new MyFilter2());
        bean.addUrlPatterns("/*"); // /* 로 해야함 (/** 이거 안됨)
        bean.setOrder(0); // 번호가 낮을수록 필터중 가장 먼저 실행됨

        return bean;
    }
}
