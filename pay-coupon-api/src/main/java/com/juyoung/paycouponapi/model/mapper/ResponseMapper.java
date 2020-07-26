package com.juyoung.paycouponapi.model.mapper;

import com.juyoung.paycouponapi.model.entity.Coupon;
import com.juyoung.paycouponapi.model.projection.OfferedCoupon;
import com.juyoung.paycouponapi.model.response.CouponResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ResponseMapper {
    ResponseMapper RESPONSE_MAPPER = Mappers.getMapper(ResponseMapper.class);

    @Mapping(source = "id", target = "couponId")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "expireDate", target = "expireDate")
    @Mapping(source = "used", target = "used")
    CouponResponse convertOfferedCouponToCouponResponse(OfferedCoupon coupon);

    @Mapping(source = "id", target = "couponId")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(expression = "java(\"\")", target = "userId")
    @Mapping(expression = "java(null)", target = "expireDate")
    @Mapping(source = "used", target = "used")
    CouponResponse convertNotOfferedCouponToCouponResponse(Coupon coupon);
}
