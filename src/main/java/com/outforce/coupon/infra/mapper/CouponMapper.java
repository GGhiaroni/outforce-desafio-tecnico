package com.outforce.coupon.infra.mapper;

import com.outforce.coupon.core.entities.Coupon;
import com.outforce.coupon.core.valueobjects.CouponCode;
import com.outforce.coupon.core.valueobjects.DiscountValue;
import com.outforce.coupon.infra.dto.request.CreateCouponRequest;
import com.outforce.coupon.infra.dto.response.CouponResponse;
import com.outforce.coupon.infra.persistence.CouponEntity;

public class CouponMapper {

    private CouponMapper() {
    }

    public static Coupon toDomain(CreateCouponRequest dto) {
        return Coupon.create(
                new CouponCode(dto.code()),
                dto.description(),
                new DiscountValue(dto.discountValue()),
                dto.expirationDate(),
                dto.published() != null && dto.published()
        );
    }

    public static Coupon toDomain(CouponEntity entity) {
        return new Coupon(
                entity.getId(),
                new CouponCode(entity.getCode()),
                entity.getDescription(),
                new DiscountValue(entity.getDiscountValue()),
                entity.getExpirationDate(),
                entity.isPublished(),
                entity.isRedeemed(),
                entity.isDeleted()
        );
    }

    public static CouponEntity toEntity(Coupon coupon) {
        return new CouponEntity(
                coupon.getId(),
                coupon.getCode().value(),
                coupon.getDescription(),
                coupon.getDiscountValue().value(),
                coupon.getExpirationDate(),
                coupon.isPublished(),
                coupon.isRedeemed(),
                coupon.isDeleted()
        );
    }

    public static CouponResponse toResponse(Coupon coupon) {
        return new CouponResponse(
                coupon.getId(),
                coupon.getCode().value(),
                coupon.getDescription(),
                coupon.getDiscountValue().value(),
                coupon.getExpirationDate(),
                coupon.getStatus(),
                coupon.isPublished(),
                coupon.isRedeemed()
        );
    }
}