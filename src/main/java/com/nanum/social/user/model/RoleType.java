package com.nanum.social.user.model;


import lombok.Getter;

@Getter
public enum RoleType {

    ADMIN("ROLE_ADMIN", 	"관리자"),
    USER("ROLE_USER", 	"일반회원"),
    ;

    private final String value;
    private final String message;

    RoleType(String value, String message) {
        this.value = value;
        this.message = message;
    }
}
