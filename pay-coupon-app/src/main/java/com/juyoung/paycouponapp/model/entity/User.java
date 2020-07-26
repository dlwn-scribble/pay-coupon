package com.juyoung.paycouponapp.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@ToString
@NoArgsConstructor
@Table(name="USER")
public class User {

    @Id
    private Long userNo;
    @Column(name="USER_ID", nullable = false, unique = true, length = 30)
    private String userId;
    @Column(name="PASSWORD", nullable = false)
    private String password;

    public User(Long userNo, String userId, String pw){
        this.userNo = userNo;
        this.userId = userId;
        this.password = pw;
    }
}
