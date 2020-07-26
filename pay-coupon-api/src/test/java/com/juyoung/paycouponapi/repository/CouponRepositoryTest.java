package com.juyoung.paycouponapi.repository;

import com.juyoung.paycouponapi.model.entity.Coupon;
import com.juyoung.paycouponapi.model.entity.User;
import com.juyoung.paycouponapi.model.projection.OfferedCoupon;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
class CouponRepositoryTest {
    static final Logger logger = LoggerFactory.getLogger(CouponRepositoryTest.class);

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    UserRepository userRepository;

    static String TEST_USER_ID = "COUPONREPO_TEST_USER";
    static Long TEST_USER_NO = 0L;
    static int COUPON_PERIOD = 3;
    static String OFFERED_COUPON_ID = "";
    static LocalDateTime OFFERED_DATE = LocalDateTime.now();
    static LocalDateTime EXPIRED_DATE = OFFERED_DATE.plusDays(COUPON_PERIOD).toLocalDate().atStartOfDay();

    @BeforeAll
    public static void setup(@Autowired CouponRepository couponRepository
        ,@Autowired UserRepository userRepository
    ){
        //user 생성
        userRepository.saveAll(Arrays.asList(
                new User(TEST_USER_ID, "1234"),
                new User("other", "1234")
        ));
        TEST_USER_NO = userRepository.findByUserId(TEST_USER_ID).getUserNo();

        //coupon 생성 및 미지급
        couponRepository.save(new Coupon(OFFERED_DATE, COUPON_PERIOD));

        //coupon 생성 및 지급
        Coupon coupon = new Coupon(OFFERED_DATE, COUPON_PERIOD);
        coupon.setUserNo(TEST_USER_NO);
        coupon.setExpireDate(EXPIRED_DATE);
        couponRepository.save(coupon);

        Coupon coupon1 = new Coupon(OFFERED_DATE, COUPON_PERIOD);
        coupon1.setUserNo(TEST_USER_NO);
        coupon1.setExpireDate(EXPIRED_DATE);
        couponRepository.save(coupon1);
        OFFERED_COUPON_ID = coupon1.getId();

        Coupon coupon2 = new Coupon(OFFERED_DATE, COUPON_PERIOD);
        coupon1.setUserNo(TEST_USER_NO);
        coupon1.setExpireDate(EXPIRED_DATE);
        couponRepository.save(coupon2);
    }

    @Test
    void Should_HaveSameUserNos_When_ItFindsByUserId() {
        List<Coupon> result = couponRepository.findAllByUserId(TEST_USER_ID);

        assertTrue(result.size() > 0);
        for (Coupon res: result) {
            assertEquals(TEST_USER_NO, res.getUserNo());
            logger.info(String.format(">> %s", res.toString()));
        }
    }

    @Test
    void Should_HaveSameUserId_When_ItFindsByCouponId() {
        OfferedCoupon result = couponRepository.findOfferedById(OFFERED_COUPON_ID);
        assertEquals(TEST_USER_ID, result.getUserId());
    }

    @Test
    void Should_HaveUserIds(){
        List<OfferedCoupon> coupons = couponRepository.findAllOffered();
        assertTrue(coupons.size() > 0);
        for (OfferedCoupon res: coupons) {
            assertNotNull(res.getUserId());
            assertTrue(res.getId().length() > 0);
            logger.info(String.format(">> %s", res.toString()));
        }
    }

    @Test
    void Should_HaveValidCoupons(){

        List<OfferedCoupon> coupons = couponRepository.findAllExpectedExpiredByExpireDate(EXPIRED_DATE);

        assertTrue(coupons.size() > 0);
        for (OfferedCoupon res: coupons) {
            assertNotNull(res.getUserId());
            assertTrue(res.getId().length() > 0);
            assertFalse(res.isUsed());
            assertEquals(EXPIRED_DATE, res.getExpireDate());
        }
    }


}