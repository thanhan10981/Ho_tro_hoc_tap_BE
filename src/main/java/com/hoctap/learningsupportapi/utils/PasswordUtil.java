package com.hoctap.learningsupportapi.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String hashPassword(String rawPassword) {
        return encoder.encode(rawPassword);
    }
    public static boolean matches(String raw, String hashed) {
        return encoder.matches(raw, hashed);
    }

}
