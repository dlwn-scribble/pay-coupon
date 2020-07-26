package com.juyoung.paycouponapp;

import com.juyoung.paycouponapp.model.entity.Coupon;
import com.juyoung.paycouponapp.model.entity.User;
import com.juyoung.paycouponapp.repository.CouponRepository;
import com.juyoung.paycouponapp.repository.UserRepository;
import org.springframework.batch.item.ItemWriter;

import java.time.LocalDateTime;
import java.util.List;

public class CouponItemWriter implements ItemWriter<Coupon> {

    private final CouponRepository couponRepository;

    private final UserRepository userRepository;

    public CouponItemWriter(CouponRepository couponRepository, UserRepository userRepository) {
        this.couponRepository = couponRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void write(List<? extends Coupon> coupons) throws Exception {
        for (Coupon coupon: coupons)
        {
            //사용자 조회
            User user = userRepository.findById(coupon.getUserNo()).orElseGet(()-> null);

            if(user == null)
            {
                System.out.println(String.format(">>>[쿠폰 만료 알람] 사용자 정보가 없습니다., Coupon ID %s"
                        , coupon.getId()));
            }
            else{
                System.out.println(String.format(">>>[쿠폰 만료 알람] UserID : %s, Coupon ID (%s) 쿠폰이 3일 후 만료됩니다."
                        , user.getUserId(), coupon.getId()));
            }

            //알람 처리
            coupon.setAlertDate(LocalDateTime.now());
            couponRepository.save(coupon);

        }
    }
}
