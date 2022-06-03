package com.cos.jwt.config;

import com.cos.jwt.config.jwt.JwtAuthenticationFilter;
import com.cos.jwt.config.jwt.JwtAuthorizationFilter;
import com.cos.jwt.filter.MyFilter3;
import com.cos.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 아래의 bean 들을 생성자를 만들지 않고 configure()에서 .addFilter(corsFilter) 하였음.
    private final CorsFilter corsFilter; // CorsConfig 에서 이미 bean이 설정되어있음,
    private final UserRepository userRepository;

    @Bean // 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
    public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.addFilterBefore(new MyFilter3(), SecurityContextPersistenceFilter.class); // MyFilter3 는 Security 가 동작하기 전에 실행됨.
//        http.addFilterBefore(new MyFilter3(), BasicAuthenticationFilter.class);
        http.csrf().disable() // rest api이므로 csrf 보안이 필요없으므로 disable처리.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않겠다는 의미
                .and()
                .addFilter(corsFilter) // 인증가능한 모든 요청에서 CORS 요청을 허용하도록 filter를 사용 // @CrossOrigin의 경우는 인증이 안됨.,
                .formLogin().disable() // form 로그인을 사용하지 않음
                .httpBasic().disable() // http 로그인을 사용하지 않음,  요청시 header 에 authorization:(id,pw)를 암호화 하지 않고 가지고 있으므로 보안에 취약함, 여기서는 jwt를 사용하므로 disable
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                // JwtAuthenticationFilter()가 상속하고 있는 UsernamePasswordAuthenticationFilter 는  AuthenticationManager 를 파라미터로 받아야 함
                // ==> AuthenticationManager 는 현재 객체서 상속받은 WebSecurityConfigurerAdapter에서 authenticationManager()를 호출하면 얻을수 있음
                // UsernamePasswordAuthenticationFilter는 로그인을 진행하는 클래스이고 AuthenticationManager 통해서 로그인을 진행할수있음.

                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))
                // AuthenticationManager : 상속받은 WebSecurityConfigurerAdapter에서 authenticationManager()를 호출

                .authorizeRequests()
                .antMatchers("/api/v1/user/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/manager/**")
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll();











    }
}
