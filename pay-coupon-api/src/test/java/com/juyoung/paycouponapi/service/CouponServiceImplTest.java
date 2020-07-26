package com.juyoung.paycouponapi.service;

import com.juyoung.paycouponapi.exception.BusinessErrorCode;
import com.juyoung.paycouponapi.exception.BusinessException;
import com.juyoung.paycouponapi.model.entity.Coupon;
import com.juyoung.paycouponapi.model.entity.User;
import com.juyoung.paycouponapi.repository.CouponRepository;
import com.juyoung.paycouponapi.repository.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class CouponServiceImplTest {

    @Autowired
    private CouponServiceImpl couponService;

    @MockBean
    private CouponRepository couponRepository;

    @Mock
    Coupon mockCoupon = new Coupon();

    @Mock
    User mockUser = new User();

    @BeforeEach
    public void setUp() {
    }

    @Test
    void Should_ThrowBusinessException_For_InvalidUserId() {
        BusinessException exception;

        exception = assertThrows(BusinessException.class, () -> {
            couponService.offerCoupon(null);
        });
        assertEquals(BusinessErrorCode.USER_ID_NOT_EXIST, exception.getBusinessErrorCode());

        when(couponRepository.findFirstByUserNo(any(Long.class))).thenReturn(null);
        exception = assertThrows(BusinessException.class, () -> {
            couponService.offerCoupon(mockUser);
        });
        assertEquals(BusinessErrorCode.AVAILABLE_COUPON_NOT_EXIST, exception.getBusinessErrorCode());
    }

    @Test
    void Should_ThrowBusinessException_For_InvalidCouponId() {
        BusinessException exception;

        when(couponRepository.findById(any(String.class))).thenReturn(Optional.empty());
        exception = assertThrows(BusinessException.class, () -> {
            couponService.setCouponUsed("INVALID_COUPON_ID");
        });
        assertEquals(BusinessErrorCode.COUPON_NOT_EXIST, exception.getBusinessErrorCode());


        when(couponRepository.findById(any(String.class))).thenReturn(Optional.of(new Coupon()));
        exception = assertThrows(BusinessException.class, () -> {
            couponService.setCouponUsed("INVALID_COUPON_ID");
        });
        assertEquals(BusinessErrorCode.COUPON_NOT_OFFERED, exception.getBusinessErrorCode());

        when(couponRepository.findById(any(String.class))).thenReturn(Optional.of(mockCoupon));
        when(mockCoupon.getUserNo()).thenReturn(1L);
        when(mockCoupon.getExpireDate()).thenReturn(null);
        exception = assertThrows(BusinessException.class, () -> {
            couponService.setCouponUsed("INVALID_COUPON_ID");
        });
        assertEquals(BusinessErrorCode.INVALID_COUPON, exception.getBusinessErrorCode());

        when(mockCoupon.getExpireDate()).thenReturn(LocalDateTime.now().plusDays(1));
        when(mockCoupon.isUsed()).thenReturn(true);
        exception = assertThrows(BusinessException.class, () -> {
            couponService.setCouponUsed("INVALID_COUPON_ID");
        });
        assertEquals(BusinessErrorCode.USED_COUPON, exception.getBusinessErrorCode());

        when(mockCoupon.isUsed()).thenReturn(false);
        when(mockCoupon.getExpireDate()).thenReturn(LocalDateTime.now().plusDays(-1));
        exception = assertThrows(BusinessException.class, () -> {
            couponService.setCouponUsed("INVALID_COUPON_ID");
        });
        assertEquals(BusinessErrorCode.EXPIRED_COUPON, exception.getBusinessErrorCode());
    }
}