package com.juyoung.paycouponapi.model.response;

import lombok.Getter;

import java.util.List;

@Getter
public class GetCouponsResponse {

    private List<CouponResponse> couponResponseList;

    public GetCouponsResponse(List<CouponResponse> couponResponseList){
        this.couponResponseList = couponResponseList;
    }

}
