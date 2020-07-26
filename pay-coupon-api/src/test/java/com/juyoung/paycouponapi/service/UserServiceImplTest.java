package com.juyoung.paycouponapi.service;

import com.juyoung.paycouponapi.security.TokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceImplTest {

    static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    @Autowired
    UserServiceImpl userService;

    @Test
    void Should_Match_PasswordWithEncryptedPassword() {
        String password = "1234";
        String encrypted = userService.passwordEncoder().encode(password);
        assertTrue(userService.passwordEncoder().matches(password, encrypted));
        logger.info(String.format(">> password : %s, encrypted : %s", password, encrypted));
    }
}