package com.juyoung.paycouponapi.service;

import com.juyoung.paycouponapi.model.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserService {

    User createUser(String userId, String password);
    User getUserByUserId(String userId);
    User getUserByUserIdAndPassword(String userId, String password);
    PasswordEncoder passwordEncoder();
}
