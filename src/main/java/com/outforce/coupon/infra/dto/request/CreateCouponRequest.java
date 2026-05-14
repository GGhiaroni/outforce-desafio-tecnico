package com.outforce.coupon.infra.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateCouponRequest(
        @NotBlank
        String code,
        @NotBlank
        String description,
        @NotNull
        @DecimalMin("0.5")
        BigDecimal discountValue,
        @NotNull
        @Future
        LocalDateTime expirationDate,
        Boolean published
) {
}
