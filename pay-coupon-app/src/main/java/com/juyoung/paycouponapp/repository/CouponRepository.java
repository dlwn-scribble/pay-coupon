package com.juyoung.paycouponapp.repository;

import com.juyoung.paycouponapp.model.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, String> {
    List<Coupon> findByExpireDateAndUsedAndAlertDate(LocalDateTime dateTime, boolean b, LocalDateTime o);
}