package com.juyoung.paycouponapi.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private BusinessErrorCode businessErrorCode;

    public BusinessException(BusinessErrorCode businessErrorCode) {
        this.businessErrorCode = businessErrorCode;
    }
}
