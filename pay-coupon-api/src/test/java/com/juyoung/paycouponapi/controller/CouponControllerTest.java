package com.juyoung.paycouponapi.controller;

import com.juyoung.paycouponapi.exception.BusinessErrorCode;
import com.juyoung.paycouponapi.exception.BusinessException;
import com.juyoung.paycouponapi.model.entity.User;
import com.juyoung.paycouponapi.service.CouponServiceImpl;
import com.juyoung.paycouponapi.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CouponControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CouponController couponController;

    @MockBean
    CouponServiceImpl couponService;

    @MockBean
    UserServiceImpl userService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    //region 쿠론 생성
    @WithMockUser(username="spring")
    @Test
    void Should_Be_Created() throws Exception {
        mockMvc.perform(post("/api/v1/coupons")
                .param("count", "10")
                .param("period", "3")).andExpect(status().isCreated());

    }

    @WithMockUser(username="spring")
    @Test
    void Should_Be_Created_Without_Period() throws Exception {

        mockMvc.perform(post("/api/v1/coupons")
                .param("count", "10")).andExpect(status().isCreated());

    }
    //endregion

    //region 쿠폰 지급
    @WithMockUser(username="spring")
    @Test
    void Should_Be_Not_Acceptable_For_Offering_With_Invalid_User() throws Exception {
        when(userService.getUserByUserId(any(String.class))).thenReturn(null);
        when(couponService.offerCoupon(null))
                .thenThrow(new BusinessException(BusinessErrorCode.USER_ID_NOT_EXIST));
        mockMvc.perform(post("/api/v1/coupons/offer")
                .param("userId", "test")).andExpect(status().isNotFound());

    }

    @WithMockUser(username="spring")
    @Test
    void Should_Be_Not_Acceptable_For_Offering_Nothing() throws Exception {
        when(userService.getUserByUserId(any(String.class))).thenReturn(null);
        when(couponService.offerCoupon(null))
                .thenThrow(new BusinessException(BusinessErrorCode.AVAILABLE_COUPON_NOT_EXIST));
        mockMvc.perform(post("/api/v1/coupons/offer")
                .param("userId", "test")).andExpect(status().isNotFound());

    }
    //endregion

    //region 쿠폰 사용
    @WithMockUser(username="spring")
    @Test
    void Should_Be_Not_Found_For_Using_Invalid_Coupon() throws Exception {
        when(couponService.setCouponUsed(any(String.class)))
                .thenThrow(new BusinessException(BusinessErrorCode.COUPON_NOT_EXIST));
        mockMvc.perform(post("/api/v1/coupons/use")
                .param("couponId", "test")).andExpect(status().isNotFound());
    }

    @WithMockUser(username="spring")
    @Test
    void Should_Be_Not_Acceptable_For_Using_Not_Offered_Coupon() throws Exception {
        when(couponService.setCouponUsed(any(String.class)))
                .thenThrow(new BusinessException(BusinessErrorCode.COUPON_NOT_OFFERED));
        mockMvc.perform(post("/api/v1/coupons/use")
                .param("couponId", "test")).andExpect(status().isNotAcceptable());
    }
    //endregion

    //region 쿠폰 사용 취소
    @WithMockUser(username="spring")
    @Test
    void Should_Be_Not_Found_For_Cancelling_Invalid_Coupon() throws Exception {
        when(couponService.setCouponCanceled(any(String.class)))
                .thenThrow(new BusinessException(BusinessErrorCode.COUPON_NOT_EXIST));
        mockMvc.perform(post("/api/v1/coupons/cancel")
                .param("couponId", "test")).andExpect(status().isNotFound());
    }

    @WithMockUser(username="spring")
    @Test
    void Should_Be_Not_Acceptable_For_Cancelling_Not_Offered_Coupon() throws Exception {
        when(couponService.setCouponCanceled(any(String.class)))
                .thenThrow(new BusinessException(BusinessErrorCode.COUPON_NOT_OFFERED));
        mockMvc.perform(post("/api/v1/coupons/cancel")
                .param("couponId", "test")).andExpect(status().isNotAcceptable());
    }
    //endregion
}