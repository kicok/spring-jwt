package com.nanum.social.config.auth;

import com.nanum.social.user.repository.UserRepository;
import com.nanum.social.user.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// http://localhost:8080/login (post) => .formLogin().disable() 상태 이므로 동작 안함
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public PrincipalDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("유저가 없습니다."));
        System.out.println("PrincipalDetailsService 의  loadUserByUsername 실행");

        return new PrincipalDetails(userEntity);

//        if(userEntity!=null){
//            return new PrincipalDetails(userEntity);
//        }
//        return null;
//      null 을 리턴하면 :  org.springframework.security.authentication.InternalAuthenticationServiceException: UserDetailsService returned null, which is an interface contract violation
    }
}
