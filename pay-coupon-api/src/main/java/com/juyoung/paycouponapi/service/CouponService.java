package com.juyoung.paycouponapi.service;

import com.juyoung.paycouponapi.model.entity.User;
import com.juyoung.paycouponapi.model.response.CouponResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface CouponService {

    void createCoupons(int count ,int durationDay);
    CouponResponse offerCoupon(User user) throws Exception;
    CouponResponse setCouponUsed(String couponId) throws Exception;
    CouponResponse setCouponCanceled(String couponId) throws Exception;
    List<CouponResponse> getCouponsOffered();
    List<CouponResponse> getCouponsByExpireDate(LocalDateTime expireDate);

}
