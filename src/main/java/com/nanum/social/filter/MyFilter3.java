package com.nanum.social.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter3 implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        System.out.println("필터3");

        // Get http://localhost:8080/api/v1/home 의 헤더에 키값 'Authroization' 에 value값을 'hellow' 로 POST 메서드로 보내며 테스트함
        // GET만 허용한 주소이지만 post 메서드가 필터를 통과하는걸 알수 있음.
        // 그러나 value 값을 'cos'로 변경하면  filterChain.doFilter(req, res); 를 통과할수는 없음(org.springframework.web.HttpRequestMethodNotSupportedException: Request method 'POST' not supported)

        // POST http://localhost:8080/api/v1/home 에서 위와같은 방식으로 다시 테스트하면
        //  value 값을 'cos'로 변경했을때 filterChain.doFilter(req, res); 를 통과하는걸 확인할수 있음.



        if(req.getMethod().equals("POST")){
            System.out.println("POST 요청됨");
            String headerAuth = req.getHeader("Authroization");
            System.out.println(headerAuth);

            // 토큰 :  cos 이걸 만들어줘야함.
            // 실제 jwt 인증을 구축하려면 jwt토큰을 받는 역할을 이러한 필터가 하게됨.
            // id. pw 정상적으로 들어와서 로그인이 완료되면 토큰을 만들어주고 그걸 응답을 해준다.
            // 요청할때마다 header에 Authroization의 value값으로 토큰을 전송함
            // 그때 토큰이 넘어오면 그 토큰이 내가 만든 토큰이 맞는지만 검증하면 됨. (RSA, HS256)

            // RSA는 전자서명으로 사용되어 서버에서 시크릿키(개인키)로 암호화된 값을 생성하여 토큰으로 보내고
            // 클라이언트는 이 토큰을 Authroization 에 담아 서버에 보내 인증요청
            // 서버는 토큰을 확인하여 내가 보낸 토큰과 같은지 확인하고
            // 공개키로 복호화하여 사용자를 인증함.

            if(headerAuth.equals("cos")){ // 한글은 안됨.
                System.out.println("인증됨");
                filterChain.doFilter(req, res);
            }else{
                PrintWriter outPrintWriter = res.getWriter();
                outPrintWriter.println("인증안됨"); // 화면에 이걸 보여주고 끝남
            }
        }
    }
}
