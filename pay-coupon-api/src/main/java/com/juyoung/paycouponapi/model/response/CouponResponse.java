package com.juyoung.paycouponapi.model.response;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Getter
public class CouponResponse {

    private String couponId;
    private LocalDateTime createdDate;

    private String userId;
    private LocalDateTime expireDate;
    private boolean used;

    public void setCouponId(String couponId){
        this.couponId = couponId;
    }

    public void setCreatedDate(LocalDateTime createdDate){
        this.createdDate = createdDate;
    }
    public void setUserId(String userId){
        this.userId = userId;
    }

    public void setExpireDate(LocalDateTime expireDate){
        this.expireDate = expireDate;
    }

    public void setUsed(Boolean used){
        this.used = used;
    }


}
