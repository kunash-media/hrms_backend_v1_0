package com.hrms.config;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;  // ? ADD THIS IMPORT
import org.springframework.stereotype.Component;

@Component
public class BcryptEncoderConfig implements PasswordEncoder {  // ? ADD "implements PasswordEncoder"

    @Override  // ? ADD THIS
    public String encode(CharSequence rawPassword) {
        return BCrypt.hashpw(rawPassword.toString(), BCrypt.gensalt());
    }

    @Override  // ? ADD THIS
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
    }
}