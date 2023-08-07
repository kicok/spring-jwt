package com.nanum.social.config.jwt;

public interface JwtProperties {
    String SECRET = "kicok"; // 서버만 알고 있는 비밀값
    int EXPIRATION_TIME = 60000*10; // 10분
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    String USERNAME = "username";
    String ID = "id";
}
