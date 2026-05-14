package com.outforce.coupon.core.gateways;

import com.outforce.coupon.core.entities.Coupon;

import java.util.Optional;
import java.util.UUID;

public interface CouponGateway {
    Coupon save(Coupon coupon);
    Optional<Coupon> findById(UUID id);
}
