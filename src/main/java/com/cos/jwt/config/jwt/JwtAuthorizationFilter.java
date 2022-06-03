package com.cos.jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwt.config.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.cos.jwt.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 이 객체가 하는일 - JWT 토큰이 유효한지를 판단하는 필터(전자서명을 통해서 개인정보에 접근할수 있게함)
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
// security가 가지고 있는 필터중에 BasicAuthenticationFilter 라는 것이 있음
// 권한이나 인증이 필요한 특정 주소를 요청했을때 위 필터를 무조건 타게 되어있음
// 만약에 권한이나 인증이 필요한 주소가 아니라면 위 필터를 타지 않는다.

    private final UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
        System.out.println("인증이나 권한이 필요한 주소 요청이 됨1111");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        //super.doFilterInternal(request, response, chain); // 이걸 여기에 두면 필터를 두번 타므로 오동작한다. 그러므로 삭제하거나 제일 아래에 두면 됨.
        System.out.println("인증이나 권한이 필요한 주소 요청이 됨2222");

        // 요청 헤더에서 JWT를 받아온다. 즉 헤더의 Authorization 키 값을 받아옴
        String jwtHeader = request.getHeader(JwtProperties.HEADER_STRING); // Authorization
        System.out.println("jwtHeader : "+jwtHeader);

        // header가 정상이 아날때 처리, 헤더값이 Bearer 로 시작하지 않을때.
        if(jwtHeader == null || !jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)){ // "Bearer "
            chain.doFilter(request, response);
            return;
        }

        // JWT 토큰을 검증을 해서 정상적인 사용자인지 확인
        try {

            // Bearer를 제거하고 순수 JWT만 남긴다.
            String jwtToken = jwtHeader.replace(JwtProperties.TOKEN_PREFIX, "");

            // username 받아오기 : username이 정상적으로 나오면 서명이 정상이라는 뜻
            String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))// 시크릴키 값 입력
                            .build().verify(jwtToken) // 토큰 서명
                            .getClaim(JwtProperties.USERNAME).asString(); // null 이 아니면 정상, JwtProperties.USERNAME = "username"

            // 서명이 정상이면
            if(username != null){
                System.out.println("username  정상 : " +  username);
                User userEntity = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("유저가 없습니다."));;
                PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

                // 일반 form 로그인이 아니라 JWT 토큰서명으로 강제 로그인 하는것이므로 서명이 정상이면 Authentication 객체를 만든다.
                // username 대신에 principalDetails 를 넣고 , password 를 null 로 입력, principalDetails 는 이미 비번을 알고 있음.
                Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

                // 강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장함
                SecurityContextHolder.getContext().setAuthentication(authentication);

                chain.doFilter(request, response);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
      //  super.doFilterInternal(request, response, chain)
    }
}

