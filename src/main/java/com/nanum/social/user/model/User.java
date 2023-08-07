package com.nanum.social.user.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 프로젝트에서 연결된 DB의 넘버링 전략을 따라간다.
    private long id;

    @Column(nullable = false, length = 30, unique = true)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 30)
    private String nickname;

    private String provider; // google
    private String provider_id; // google 고유 아이디 (109522245212489089761)


    /**
     * 권한 연계
     */
//    @Fetch(value = FetchMode.SELECT)
    // fetch = FetchType.LAZY 일 경우에 failed to lazily initialize a collection of role: com.cos.jwt.model.User.roles, could not initialize proxy - no Session
    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER) // { CascadeType.PERSIST, CascadeType.REMOVE }와 동일
    private List<UserRole> roles;
}
