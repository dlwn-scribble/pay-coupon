package com.juyoung.paycouponapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BusinessErrorCode {

    // Coupon
    COUPON_NOT_EXIST("C001", HttpStatus.NOT_FOUND, "존재 하지 않는 쿠폰 입니다."),
    AVAILABLE_COUPON_NOT_EXIST("C002", HttpStatus.NOT_FOUND, "지급 가능한 쿠폰이 없습니다."),

    COUPON_NOT_OFFERED("C003",HttpStatus.NOT_ACCEPTABLE, "사용자 에게 지급 되지 않은 쿠폰 입니다."),
    INVALID_COUPON("C004",HttpStatus.NOT_ACCEPTABLE, "유효하지 않은 쿠폰입니다."),
    USED_COUPON("C005",HttpStatus.NOT_ACCEPTABLE, "이미 사용된 쿠폰입니다."),
    NOT_USED_COUPON("C006",HttpStatus.NOT_ACCEPTABLE, "사용 되지 않은 쿠폰입니다."),
    EXPIRED_COUPON("C007",HttpStatus.NOT_ACCEPTABLE, "만료된 쿠폰입니다."),

    // Member
    USER_ID_NOT_EXIST("M001", HttpStatus.NOT_FOUND,"존재 하지 않는 사용자 입니다."),
    USER_ID_EXIST("M002", HttpStatus.NOT_FOUND,"이미 존재하는 사용자 아이디 입니다."),

    ;

    private String code;
    private HttpStatus httpStatus;
    private String message;

    BusinessErrorCode(final String code, final HttpStatus httpStatus, final String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
