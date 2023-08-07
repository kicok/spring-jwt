package com.nanum.social.user.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
public class UserRole {
// User 와 RoleEntity 의 N:M 관계를 UserRole 을 통해 OneToMany 로 해결

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

//    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;


//  @JsonManagedReference // 이 코드의 주석을 풀면 순환참조가 발생한다.
    @ManyToOne
    @JoinColumn(name = "roleId")
    private RoleEntity role;
}
