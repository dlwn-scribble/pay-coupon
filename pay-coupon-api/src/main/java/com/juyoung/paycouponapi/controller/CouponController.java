package com.juyoung.paycouponapi.controller;

import com.juyoung.paycouponapi.model.entity.User;
import com.juyoung.paycouponapi.model.response.CouponResponse;
import com.juyoung.paycouponapi.model.response.GetCouponsResponse;
import com.juyoung.paycouponapi.model.response.SetCouponResponse;
import com.juyoung.paycouponapi.service.CouponServiceImpl;
import com.juyoung.paycouponapi.service.UserServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api/v1")
@RestController
public class CouponController {

    @Autowired
    CouponServiceImpl couponService;

    @Autowired
    UserServiceImpl userService;

    //region 쿠폰 관리
    @ApiOperation(value = "쿠폰 생성")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "쿠폰 생성"),
            @ApiResponse(code = 401, message = "인증 실퍠"),
            @ApiResponse(code = 403, message = "접근 불가"),
            @ApiResponse(code = 500, message = "시스템 에러"),
    })
    @PostMapping("/coupons")
    public ResponseEntity<SetCouponResponse> createCoupons(
            @ApiParam(value = "쿠폰 수량", required = true, example = "1") @RequestParam("count") int count
            ,@ApiParam(value = "쿠폰 유효 기간 (일자) : 지급일 익일 부터 계산", required = false, example = "3", defaultValue = "3") @RequestParam(value ="period", defaultValue = "3") int period)
    {
        couponService.createCoupons(count, period);
        SetCouponResponse res = new SetCouponResponse(String.format("%d 개 쿠폰이 생성 되었습니다.", count));
        return new ResponseEntity<SetCouponResponse>(res, HttpStatus.CREATED);
    }

    @ApiOperation(value = "쿠폰 지급", notes = "사용자에게 쿠폰을 지급합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "쿠폰 지급"),
            @ApiResponse(code = 401, message = "인증 실퍠"),
            @ApiResponse(code = 403, message = "접근 불가"),
            @ApiResponse(code = 406, message = "처리 불가"),
            @ApiResponse(code = 500, message = "시스템 에러"),
    })
    @PostMapping("/coupons/offer")
    public ResponseEntity<SetCouponResponse> offerCoupon(
            @ApiParam(value = "사용자", required = true) @RequestParam("userId") String userId) throws Exception {

        User user = userService.getUserByUserId(userId);
        CouponResponse coupon = couponService.offerCoupon(user);
        coupon.setUserId(userId);
        List<CouponResponse> coupons = new ArrayList<CouponResponse>();
        coupons.add(coupon);
        SetCouponResponse res = new SetCouponResponse(String.format("쿠폰 번호 (%s)", coupon.getCouponId()),coupons);
        return new ResponseEntity<SetCouponResponse>(res, HttpStatus.ACCEPTED);
    }

    @ApiOperation(value = "쿠폰 사용")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "쿠폰 처리"),
            @ApiResponse(code = 401, message = "인증 실퍠"),
            @ApiResponse(code = 403, message = "접근 불가"),
            @ApiResponse(code = 406, message = "처리 불가"),
            @ApiResponse(code = 500, message = "시스템 에러"),
    })
    @PostMapping("/coupons/use")
    public ResponseEntity<SetCouponResponse> setCouponUsed(
            @ApiParam(value = "쿠폰번호", required = true) @RequestParam("couponId") String couponId) throws Exception {
        CouponResponse coupon = couponService.setCouponUsed(couponId);
        List<CouponResponse> coupons = new ArrayList<CouponResponse>();
        coupons.add(coupon);
        SetCouponResponse res = new SetCouponResponse(String.format("쿠폰 (%s) 사용 처리 되었습니다.", coupon.getCouponId()),coupons);
        return new ResponseEntity<SetCouponResponse>(res, HttpStatus.ACCEPTED);
    }

    @ApiOperation(value = "쿠폰 사용 취소")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "쿠폰 처리"),
            @ApiResponse(code = 401, message = "인증 실퍠"),
            @ApiResponse(code = 403, message = "접근 불가"),
            @ApiResponse(code = 406, message = "처리 불가"),
            @ApiResponse(code = 500, message = "시스템 에러"),
    })
    @PostMapping("/coupons/cancel")
    public ResponseEntity<SetCouponResponse> setCouponCanceled(
            @ApiParam(value = "쿠폰번호", required = true) @RequestParam("couponId") String couponId) throws Exception {
        CouponResponse coupon = couponService.setCouponCanceled(couponId);
        List<CouponResponse> coupons = new ArrayList<CouponResponse>();
        coupons.add(coupon);
        SetCouponResponse res = new SetCouponResponse(String.format("쿠폰 (%s) 사용 취소 처리 되었습니다.", coupon.getCouponId()),coupons);
        return new ResponseEntity<SetCouponResponse>(res, HttpStatus.ACCEPTED);
    }
    //endregion

    //region 쿠폰 조회
    @ApiOperation(value = "쿠폰 조회", notes = "지급 여부에 따른 쿠폰을 조회 합니다.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "쿠폰 조회 완료"),
            @ApiResponse(code = 401, message = "인증 실퍠"),
            @ApiResponse(code = 403, message = "접근 불가"),
            @ApiResponse(code = 500, message = "시스템 에러"),
    })
    @GetMapping("/coupons")
    public ResponseEntity<GetCouponsResponse> getCoupons(
            @ApiParam(value = "지급 여부", required = false, defaultValue = "true") @RequestParam(value = "offered") boolean offered,
            @ApiParam(value = "만료 일자", required = false, defaultValue = "") @RequestParam(value = "expireDate", defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate expireDate)
    {
        List<CouponResponse> responseList = null;

        if(!offered)
            responseList = couponService.getCouponsNotOffered();
        else{
            if(expireDate!=null)
                responseList = couponService.getCouponsByExpireDate(expireDate.atStartOfDay());
            else
                responseList = couponService.getCouponsOffered();
        }

        GetCouponsResponse res = new GetCouponsResponse(responseList);
        return new ResponseEntity<GetCouponsResponse>(res, HttpStatus.OK);
    }
    //endregion
}
