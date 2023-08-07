package com.nanum.social.util;

import java.util.function.Function;

public class UserUtil {

    private UserUtil() throws InstantiationException {
        throw new InstantiationException();
    }

    public  static Function<String, String> passwordTest = (rawPassword)->{

        if(rawPassword.length() < 4) throw new RuntimeException("password는 4자 이상이어야합니다.");
        else return rawPassword;
    };
}
