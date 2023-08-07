package com.nanum.social.util;

import org.springframework.util.StringUtils;

import java.util.function.Function;
import java.util.function.Predicate;

public class StringUtil {
    private StringUtil() throws InstantiationException {
        throw new InstantiationException();
    }

    public static Function<String, String>  spaceRemove =  StringUtils::trimAllWhitespace;
    public static Predicate<String> isEmpty = s -> s == null || spaceRemove.apply(s).isEmpty();
    public static Predicate<String> notEmpty = isEmpty.negate(); // s-> s != null && s.length() > 0;


}
