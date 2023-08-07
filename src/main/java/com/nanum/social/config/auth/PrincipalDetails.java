package com.nanum.social.config.auth;

import com.nanum.social.user.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class PrincipalDetails implements UserDetails {

    private final User user;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collects = new ArrayList<>();
        if(user.getRoles() == null) return collects;
        user.getRoles().forEach(userRole->

//            collects.add(new GrantedAuthority() {
//                @Override
//                public String getAuthority() {
//                    return userRole.getRole().getName().getValue();
//                }
//            })

                        // 위의 코드를 아래의 람다식으로 변경할수 있다
                        // 이유는 Collection<GrantedAuthority> collects 로 선언하였으므로
                        // 어차피 collects.add 안에 들어갈수 있는 Object는 GrantedAuthority 뿐이므로 추론하여 알수 있다.
                        collects.add(
                                ()->userRole
                                        .getRole()
                                        .getName()
                                        .getValue()
                        )

        );
        return collects;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
