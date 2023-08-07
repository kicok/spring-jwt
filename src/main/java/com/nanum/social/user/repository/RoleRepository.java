package com.nanum.social.user.repository;

import com.nanum.social.user.model.RoleEntity;
import com.nanum.social.user.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {

    RoleEntity findByName(RoleType name);
}
