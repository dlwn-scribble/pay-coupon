package com.juyoung.paycouponapi.exception;

import com.juyoung.paycouponapi.model.response.SetCouponResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class BusinessExceptionHandler extends ResponseEntityExceptionHandler {

    static final Logger logger = LoggerFactory.getLogger(BusinessExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ExceptionResponse> handleBusinessException(final BusinessException e) {
        //logger.error("비즈니스 예외 발생", e); //비즈니스 에러 로깅 제거
        BusinessErrorCode businessErrorCode = e.getBusinessErrorCode();
        ExceptionResponse res = new ExceptionResponse(businessErrorCode.getCode(), businessErrorCode.getMessage());
        return new ResponseEntity<ExceptionResponse>(res, businessErrorCode.getHttpStatus());
    }
    //그 외 에러 처리
    @ExceptionHandler(Exception.class)
    protected ResponseEntity handleException(Exception e) {
        logger.error("예외 발생", e); //여기서는 로컬 로깅으로 처리
        ExceptionResponse res = new ExceptionResponse("", "시스템 관리자에게 문의 하십시오.");
        return new ResponseEntity<ExceptionResponse>(res, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
