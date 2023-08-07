package com.nanum.social.user.repository;

import com.nanum.social.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// CRUD 함수를 JpaRepository가 들고 있음
// JpaRepository를 상속했으므로 @Repository 라는 어노테이션이 없어도 IoC가 된다. 즉 Bean으로 등록이 된다.
public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByUsername(String username);

}
