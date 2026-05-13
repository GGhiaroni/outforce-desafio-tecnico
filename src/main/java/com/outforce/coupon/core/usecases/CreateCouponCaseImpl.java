package com.outforce.coupon.core.usecases;

import com.outforce.coupon.core.entities.Coupon;
import com.outforce.coupon.core.gateways.CouponGateway;

public class CreateCouponCaseImpl implements CreateCouponCase {
    private final CouponGateway couponGateway;

    public CreateCouponCaseImpl(CouponGateway couponGateway) {
        this.couponGateway = couponGateway;
    }

    @Override
    public Coupon execute(Coupon coupon) {
       return couponGateway.save(coupon);
    }
}
