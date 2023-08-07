package com.nanum.social.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    // Security 에서 filter 등록을 해줘야 사용할수 있음.
    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // controller 상단 class 선언시 @CrossOrigin을 사용해도 되지만
        // 모든 controller에서 @CrossOrigin을  사용해야하고
        // 인증이 필요한 요청은 모두 거부 될수 있으므로 아래의 코드를 사용한다.
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 해당 서버에서 Authorization로 사용자 인증도 서비스할 것이라면 true (여기서는 json web token 을 받을지 여부. 즉 서버가 응답할때 Json을 자바스크립트에서 처리할수있게 할지를 설정함)
        config.addAllowedOrigin("*");   // 요청서버의 모든 도메인 및 ip와 포트에 응답할것을 허용
        config.addAllowedHeader("*"); // 요청서버의 모든 헤더 허용
        config.addAllowedMethod("*"); // 모든 메서드: post, get. put, delete, patch 허용

        source.registerCorsConfiguration("/api/", config);

        return new CorsFilter(source);


    }
}
