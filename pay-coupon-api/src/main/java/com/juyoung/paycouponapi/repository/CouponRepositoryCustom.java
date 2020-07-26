package com.juyoung.paycouponapi.repository;

import com.juyoung.paycouponapi.model.entity.Coupon;
import com.juyoung.paycouponapi.model.projection.OfferedCoupon;

import java.time.LocalDateTime;
import java.util.List;

public interface CouponRepositoryCustom {
    List<Coupon> findAllByUserId(String userId);
    OfferedCoupon findOfferedById(String id);
    List<OfferedCoupon> findAllOffered();
    List<OfferedCoupon> findAllExpectedExpiredByExpireDate(LocalDateTime expireDate);
}
