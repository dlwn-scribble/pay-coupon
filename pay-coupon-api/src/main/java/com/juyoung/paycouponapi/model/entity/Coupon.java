package com.juyoung.paycouponapi.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@NoArgsConstructor
@Table(name="COUPON")
public class Coupon {
    @Id
    @GenericGenerator(name = "UuidGenerator", strategy = "com.juyoung.paycouponapi.model.entity.generator.UuidGenerator")
    @GeneratedValue(generator = "UuidGenerator")
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

    public Coupon(LocalDateTime createdDate, int period){
        this.createdDate = createdDate;
        this.period = period;
        this.used = false;
    }

    public void setUserNo(Long userNo){
        this.userNo = userNo;
    }

    public void setExpireDate(LocalDateTime expireDate){
        this.expireDate = expireDate;
    }

    public void setUsed(boolean used){
        this.used = used;
    }
}
