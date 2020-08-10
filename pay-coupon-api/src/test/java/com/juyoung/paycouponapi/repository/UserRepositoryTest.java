package com.juyoung.paycouponapi.repository;

import com.juyoung.paycouponapi.config.QuerydslConfig;
import com.juyoung.paycouponapi.model.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QuerydslConfig.class)
class UserRepositoryTest {
    static final Logger logger = LoggerFactory.getLogger(UserRepositoryTest.class);

    @Autowired
    UserRepository userRepository;

    static String TEST_USER_ID = "USERREPO_TEST_USER";
    static Long TEST_USER_NO = 0L;

    @Test
    void Should_ThrowException_When_Insert_Duplicated_UeerId() {

        //user 생성
        userRepository.save(new User(TEST_USER_ID, "1234"));
        TEST_USER_NO = userRepository.findByUserId(TEST_USER_ID).getUserNo();

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userRepository.save(new User(TEST_USER_ID, "5678"));
        });
        logger.info(String.format(">> Exception MSG : %s", exception.getMessage()));

    }
}