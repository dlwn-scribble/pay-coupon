package com.juyoung.paycouponapi.service;

import com.juyoung.paycouponapi.exception.BusinessErrorCode;
import com.juyoung.paycouponapi.exception.BusinessException;
import com.juyoung.paycouponapi.model.entity.Coupon;
import com.juyoung.paycouponapi.model.entity.User;
import com.juyoung.paycouponapi.model.mapper.ResponseMapper;
import com.juyoung.paycouponapi.model.projection.OfferedCoupon;
import com.juyoung.paycouponapi.model.response.CouponResponse;
import com.juyoung.paycouponapi.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class CouponServiceImpl implements CouponService{

    @Autowired
    CouponRepository couponRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private static int BATCH_SIZE = 5; // memory load 방지를 위한 주기적 으로 flush!

    /**
     * 쿠폰 생성
     */
    @Transactional
    @Override
    public void createCoupons(int count, int period) {
        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < count; i++) {
            if (i > 0 && i % BATCH_SIZE == 0) {
                entityManager.flush();
                entityManager.clear();
            }
            Coupon coupon = new Coupon(now, period);
            entityManager.persist(coupon);
        }
    }

    /**
     * 쿠폰 지급
     */
    @Transactional
    @Override
    public CouponResponse offerCoupon(User user) throws Exception {
        if(user == null)
            throw new BusinessException(BusinessErrorCode.USER_ID_NOT_EXIST);

        //지급 되지 않은 쿠폰 1개
        Coupon coupon = couponRepository.findFirstByUserNo(0L);
        if(coupon == null)
            throw new BusinessException(BusinessErrorCode.AVAILABLE_COUPON_NOT_EXIST);

        LocalDateTime expireDate = this.calculateExpireDate(LocalDate.now(),coupon.getPeriod());

        coupon.setUserNo(user.getUserNo());
        coupon.setExpireDate(expireDate);
        OfferedCoupon offeredCoupon = couponRepository.findOfferedById(coupon.getId());
        CouponResponse response = ResponseMapper.RESPONSE_MAPPER.convertOfferedCouponToCouponResponse(offeredCoupon);
        return response;
    }

    /**
     * 만료 날짜 계산
     * 발급일 익일 부터 계산
     */
    public LocalDateTime calculateExpireDate(LocalDate date, int period){
        LocalDateTime startDate = date.atStartOfDay().plusDays(1);
        return startDate.plusDays(period);//+n days
    }

    /**
     * 쿠폰 사용
     */
    @Transactional
    public CouponResponse setCouponUsed(String couponId) throws Exception {
        //validation
        Coupon coupon = couponRepository.findById(couponId).orElseGet(()-> null);
        if(coupon == null)
            throw new BusinessException(BusinessErrorCode.COUPON_NOT_EXIST);

        if(coupon.getUserNo() ==0)
            throw new BusinessException(BusinessErrorCode.COUPON_NOT_OFFERED);

        if(coupon.getExpireDate()==null) //지급 시 생성 되는 값
            throw new BusinessException(BusinessErrorCode.INVALID_COUPON);

        if(coupon.isUsed())
            throw new BusinessException(BusinessErrorCode.USED_COUPON);

        LocalDateTime now = LocalDateTime.now();
        if(coupon.getExpireDate().isBefore(now))
            throw new BusinessException(BusinessErrorCode.EXPIRED_COUPON);

        coupon.setUsed(true); //사용 처리

        OfferedCoupon offeredCoupon = couponRepository.findOfferedById(couponId);
        return ResponseMapper.RESPONSE_MAPPER.convertOfferedCouponToCouponResponse(offeredCoupon);
    }

    /**
     * 쿠폰 사용 취소
     */
    @Transactional
    public CouponResponse setCouponCanceled(String couponId) throws Exception {
        //validation
        Coupon coupon = couponRepository.findById(couponId).orElseGet(()-> null);

        if(coupon == null)
            throw new BusinessException(BusinessErrorCode.COUPON_NOT_EXIST);

        if(coupon.getUserNo() ==0)
            throw new BusinessException(BusinessErrorCode.COUPON_NOT_OFFERED);

        if(coupon.getExpireDate()==null) //지급 시 생성 되는 값
            throw new BusinessException(BusinessErrorCode.INVALID_COUPON);

        if(!coupon.isUsed())
            throw new BusinessException(BusinessErrorCode.NOT_USED_COUPON);

        LocalDateTime now = LocalDateTime.now();
        if(coupon.getExpireDate().isBefore(now))
            throw new BusinessException(BusinessErrorCode.EXPIRED_COUPON);

        coupon.setUsed(false); //사용 처리 취소

        OfferedCoupon offeredCoupon = couponRepository.findOfferedById(couponId);
        return ResponseMapper.RESPONSE_MAPPER.convertOfferedCouponToCouponResponse(offeredCoupon);
    }

    /**
     * 지급 쿠폰 조회
     */
    public List<CouponResponse> getCouponsOffered(){
        List<OfferedCoupon> coupons = couponRepository.findAllOffered();

        List<CouponResponse> responses = new ArrayList<CouponResponse>();
        for (OfferedCoupon coupon:coupons) {
            CouponResponse response = ResponseMapper.RESPONSE_MAPPER.convertOfferedCouponToCouponResponse(coupon);
            responses.add(response);
        }
        return responses;
    }
    /**
     * 미지급 쿠폰 조회
     */
    public List<CouponResponse> getCouponsNotOffered(){
        //지급 되지 않은 쿠폰 1개
        List<Coupon> coupons = couponRepository.findAllByUserNo(0L);

        List<CouponResponse> responses = new ArrayList<CouponResponse>();
        for (Coupon coupon:coupons) {
            CouponResponse response = ResponseMapper.RESPONSE_MAPPER.convertNotOfferedCouponToCouponResponse(coupon);
            responses.add(response);
        }
        return responses;
    }

    /**
     * 만료 쿠폰 조회
     */
    public List<CouponResponse> getCouponsByExpireDate(LocalDateTime expireDate){
        List<OfferedCoupon> coupons = couponRepository.findAllExpectedExpiredByExpireDate(expireDate);

        List<CouponResponse> responses = new ArrayList<CouponResponse>();
        for (OfferedCoupon coupon:coupons) {
            CouponResponse response = ResponseMapper.RESPONSE_MAPPER.convertOfferedCouponToCouponResponse(coupon);
            responses.add(response);
        }
        return responses;
    }

}
