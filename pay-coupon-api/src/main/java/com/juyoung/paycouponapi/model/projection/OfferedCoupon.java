package com.juyoung.paycouponapi.model.projection;

import lombok.Getter;
import lombok.ToString;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;

@ToString
@Getter
public class OfferedCoupon {

    private String id;
    private LocalDateTime createdDate;

    private String userId;
    private LocalDateTime expireDate;

    private boolean used;

    public OfferedCoupon(String id, LocalDateTime createdDate, LocalDateTime expireDate, String userId, boolean used){
        this.id = id;
        this.createdDate = createdDate;
        this.expireDate = expireDate;
        this.userId = userId;
        this.used = used;
    }
}
