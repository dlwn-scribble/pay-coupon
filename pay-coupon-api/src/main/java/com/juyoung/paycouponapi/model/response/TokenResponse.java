package com.juyoung.paycouponapi.model.response;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class TokenResponse {

    private String tokenType;

    private String token;

    public TokenResponse(String tokenType, String token){
        this.tokenType = tokenType;
        this.token = token;
    }
}
