package com.nanum.social.user.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="role")
public class RoleEntity {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonBackReference
    @OneToMany(mappedBy = "user")
    private List<UserRole> userRoleId;

    // 데이터베이스에는 RoleType 이라는게 없으므로 EnumType.STRING 으로 타입을 지정해줌
    @Enumerated(EnumType.STRING)
    @Column(unique=true)
    private RoleType name;

}
