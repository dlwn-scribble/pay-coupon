package com.juyoung.paycouponapp;

import com.juyoung.paycouponapp.model.entity.Coupon;
import com.juyoung.paycouponapp.repository.CouponRepository;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CouponItemReader extends AbstractPagingItemReader<Coupon> {

    private final CouponRepository couponRepository;
    private int days = 3;

    public CouponItemReader(CouponRepository couponRepository,
                            int days, int chunk) {
        this.days = days;
        this.couponRepository = couponRepository;
        setPageSize(chunk);
    }

    @Override // 직접 페이지 읽기 부분 구현
    protected void doReadPage() {
        if (results == null) {
            results = new ArrayList<>();
        } else {
            results.clear();
        }
        Pageable pageable = PageRequest.of(0, this.getPageSize());
        List<Coupon> coupons = couponRepository
                .findAllByExpireDateAndUsedAndAlertDate(LocalDate.now().atStartOfDay().plusDays(this.days)
                        , false
                        , null
                        , pageable);
        System.out.println(String.format(">>>[만료 예정 쿠폰] %d 개 데이터", coupons.size()));
        results.addAll(coupons);
    }

    @Override
    protected void doJumpToPage(int itemIndex) {
    }
}
