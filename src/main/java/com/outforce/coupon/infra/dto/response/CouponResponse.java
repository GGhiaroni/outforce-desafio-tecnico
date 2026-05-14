package com.outforce.coupon.infra.dto.response;

import com.outforce.coupon.core.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CouponResponse(
        UUID id,
        String code,
        String description,
        BigDecimal discountValue,
        LocalDateTime expirationDate,
        Status status,
        boolean published,
        boolean redeemed
) {
}
