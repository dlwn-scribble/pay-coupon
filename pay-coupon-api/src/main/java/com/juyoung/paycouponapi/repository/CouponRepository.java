package com.juyoung.paycouponapi.repository;

import com.juyoung.paycouponapi.model.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, String>, CouponRepositoryCustom {
    Optional<Coupon> findById(String id);

    Coupon findFirstByUserNo(Long userNo);

    List<Coupon> findAllByUserNo(long l);
}
