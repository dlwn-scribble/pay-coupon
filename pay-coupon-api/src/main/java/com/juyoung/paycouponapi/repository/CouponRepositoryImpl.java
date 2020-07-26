package com.juyoung.paycouponapi.repository;

import com.juyoung.paycouponapi.model.entity.Coupon;
import com.juyoung.paycouponapi.model.projection.OfferedCoupon;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.time.LocalDateTime;
import java.util.List;

import static com.juyoung.paycouponapi.model.entity.QCoupon.coupon;
import static com.juyoung.paycouponapi.model.entity.QUser.user;

public class CouponRepositoryImpl implements CouponRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    public CouponRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<Coupon> findAllByUserId(String userId){
        return jpaQueryFactory.selectFrom(coupon)
                .join(user).on(coupon.userNo.eq(user.userNo))
                .fetch();
    }

    @Override
    public OfferedCoupon findOfferedById(String id) {
        return jpaQueryFactory
                .from(coupon)
                .select(Projections.constructor(OfferedCoupon.class,
                        coupon.id, coupon.createdDate, coupon.expireDate,
                        user.userId, coupon.used))
                .leftJoin(user).on(coupon.userNo.eq(user.userNo))
                .fetchJoin()
                .where(coupon.id.eq(id))
                .fetchOne();
    }

    @Override
    public List<OfferedCoupon> findAllOffered(){
        return jpaQueryFactory
                .from(coupon)
                .select(Projections.constructor(OfferedCoupon.class,
                        coupon.id, coupon.createdDate, coupon.expireDate,
                        user.userId, coupon.used))
                .join(user).on(coupon.userNo.eq(user.userNo))
                .fetchJoin()
                .fetch();
    }

    @Override
    public List<OfferedCoupon> findAllExpectedExpiredByExpireDate(LocalDateTime expireDate){
        return jpaQueryFactory
                .from(coupon)
                .select(Projections.constructor(OfferedCoupon.class,
                        coupon.id, coupon.createdDate, coupon.expireDate,
                        user.userId, coupon.used))
                .join(user).on(coupon.userNo.eq(user.userNo))
                .fetchJoin()
                .where(coupon.userNo.ne(0L) //지급된 쿠폰 중
                        .and(coupon.used.eq(false)) //사용 되지 않은 쿠폰 중
                        .and(coupon.expireDate.eq(expireDate))) //만료일자에 해당 하는 경우
                .fetch();
    }


}
