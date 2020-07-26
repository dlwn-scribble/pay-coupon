package com.juyoung.paycouponapp.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@NoArgsConstructor
@Table(name="COUPON")
public class Coupon {
    @Id
    @Column(name="ID")
    private String id;

    @Column(name="CREATED_DATE", nullable = false)
    private LocalDateTime createdDate;

    @Column(name="PERIOD", nullable = false)
    private int period;

    @Column(name="USER_NO", nullable = true)
    private long userNo;

    @Column(name="EXPIRE_DATE", nullable = true)
    private LocalDateTime expireDate;

    @Column(name="USED", nullable = false)
    private boolean used;

    @Column(name="ALERT_DATE", nullable = true)
    private LocalDateTime alertDate;

    public Coupon(String id, LocalDateTime createdDate, int period
            , long userNo, LocalDateTime expireDate, boolean used){
        this.id = id;
        this.createdDate = createdDate;
        this.period = period;
        this.userNo = userNo;
        this.expireDate = expireDate;
        this.used = used;
    }

    public void setAlertDate(LocalDateTime alertDate) {
        this.alertDate = alertDate;
    }
}
