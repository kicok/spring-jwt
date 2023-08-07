package com.nanum.social.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.nanum.social.config.auth.PrincipalDetails;
import com.nanum.social.user.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

// 스프링 시큐리티의 UsernamePasswordAuthenticationFilter를 상속
// 이 객체가 하는일 - username, password 이 모두 맞으면 JWT토큰을 생성하고 클라이언트 쪽으로 JWT토큰을 응답한다.
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
// 원래는  http://localhost:8080/login (post) 요청이 오면
// login 요청해서 username, password 전송하면 (post)
// UsernamePasswordAuthenticationFilter 가 낚아채서 attemptAuthentication() 함수가 실행됨
// 하지만 SecurityConfi에서 formLogin().disable() 했으므로 login 페이지를 사용할수 없으므로 (404 Not Found)
// UsernamePasswordAuthenticationFilter 가 작동을 안하게 됨.
//
// 그러므로 UsernamePasswordAuthenticationFilter를 상속하는 객체를 만들어서
// addFilter() 를 사용해야 login을 사용가능함

    // 풀어서 생각해보면
// 우리가 security의 formlogin을 사용하지 않는데
// UsernamePasswordAuthenticationFilter을 상속받은 녀석을 강제로 호출하여 AuthenticationManager을 호출 -> 시큐리티에게 로그인을 위임하는 이유는
// 시큐리티에게 권한 관리를 맡기기 위해서이다.
// 만약 JWT의 특성을 완벽히 구현하기 위해서, 즉 세션없는 stateful + 권한을 완벽히 구현하기 위해서는 권한 관리 로직을 따로 구현해야 한다.
// 그렇게 한다면 로그인을 스프링 시큐리티에게 위임하지 않기 때문에, (세션을 사용하지 않기 때문에)
// UserDetails, UserDetailsService를 구현하지 않아도 되고
// 그렇다면 로그인 로직까지 따로 구현해야 한다.
// 그러므로 완전히 세션을 이용하지 않으려면
// JWT + 로그인 + 권한관리 로직이 따로 들어가야한다.
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    // 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter: 로그인 시도중");


        try {
            // https://rlakuku-program.tistory.com/33
//            BufferedReader br = request.getReader();
//            String input = null;
//            while((input = br.readLine()) !=null ){
//                System.out.println(input);;
//            }

            System.out.println("11===================================");

            // 1. username, password 받아서
                // 위의 BufferedReader 방식보다 ObjectMapper 가 훨씬 효율적이다.
//            System.out.println(request.getInputStream().toString());
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);
                // 넘어온 데이터를 자동으로 User 객체에 담아준다.
            System.out.println(user);
            System.out.println("22+++++++++++++++++++++++++++++++++++++");

                // 위의 User 객체를 이용해서 토큰 만들기 => 토큰을 이용해 (authenticationManager.authenticate(authenticationToken)에서 로그인 시도를 하면됨
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            // 2. 정상인지 로그인 시도 : authenticationManager.authenticate(authenticationToken) 로 로그인을 시도한다.
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
                //  a. 위 코드를 실행하면 PrincipalDetailsService가 자동 호출되어 loadUserByUsername 이 실행됨
                //  b. (PrincipalDetailsService의 loadUserByUsername 이 실행된후 정상
                //  c. 당연히 자동으로 BCryptPasswordEncoder 를 이용해 DB에 있는 username과 password 가 일치하는지를 확인하고 일치하면 authentication 이 반환됨
                //  d. authentication 에는 내 로그인 정보가 담겨있음


            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

            System.out.println(principalDetails.getUser());
                // 위의 값이 나오면 로그인이 정상적으로 되었다는 뜻

            System.out.println("33===================================");

            // 3. PrinciaplDetails를 세션에 담고 (권한관리(ROLE) 때문에 세션이 필요 -> 권한 관리 안하려면 세션에 담을 필요없음)
            // 4. JWT 토큰을 만들어서 응답해주면 됨.

            return authentication;
            // return 하면 authentication 객체가 session영역에 저장됨 => 로그인이 되었다는 뜻
            // return 의 이유는 권한 관리를 security가 대신 해주기 때문에 편하려고 하는것임
            // 굳이 JWT 토근을 사용하면서 세션을 만들 이유가 없지만 단지 권한 처리 때문에 session 에 넣어줌.

        } catch (IOException e) {
            e.printStackTrace();
        }
            System.out.println("44=====================================");
        return null;
    }

    // 위의 attemptAuthentication 실행후 인증이 정승적으로 되었으면 successfulAuthentication 함수가 실행됨.
    // JWT 토근을 만들어서 request 요청한 사용자에게 JWT 토큰을 response 해주면 됨.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication 실행됨 : 인증이 완료 되었다는 뜻임 ");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject(principalDetails.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME)) // 토큰 완료시간 10분
                                .withClaim(JwtProperties.ID, principalDetails.getUser().getId()) // JwtProperties.ID = "id";
                                        .withClaim(JwtProperties.USERNAME, principalDetails.getUsername()) // JwtProperties.USERNAME = "username"
                                                .sign(Algorithm.HMAC512(JwtProperties.SECRET)); // RSA 방식이 아니라 Hash암호방식 (jwt에서 hash방식을 더 많이 사용함)

        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken); // 클라이언트 쪽으로 jwt 토큰을 응답함

        // 다음부터 요청할때 마다 JWT토큰을 가지고 요청
        // 서버는 JWT토큰이 유효한지를 판단하는 필터를 만들어야함
        // Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzc2FyaSIsImlkIjoxLCJleHAiOjE2NDk4MzU2OTUsInVzZXJuYW1lIjoic3NhcmkifQ.s7Tmx5-6irbxP3xJMjK000AA7s2cpkBO40AB_ukKCuFHdfHITGLLfN8lQV1E4SdLyGjMqAomyadRq9GKrnNyBw
        // Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzc2FyaSIsImlkIjoxLCJleHAiOjE2NTQyMjQ3MzcsInVzZXJuYW1lIjoic3NhcmkifQ.2GqPKqj-d-g65y9iW50_Ls6LtRMGAgFbc9BQc5dXED2b2rSW9m9ut1ihgKQqYbGPnL5DlYzK-esc-8wZw8qAnQ
    }
}

