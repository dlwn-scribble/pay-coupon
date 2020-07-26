package com.juyoung.paycouponapi.model.response;

import lombok.Getter;

import java.util.List;

@Getter
public class SetCouponResponse {

    private String message ;

    private List<CouponResponse> couponResponseList;

    public SetCouponResponse(String message){
        this.message = message;

    }

    public SetCouponResponse(String message, List<CouponResponse> couponResponseList){
        this.message = message;
        this.couponResponseList = couponResponseList;
    }
}
