package com.outforce.coupon.core.usecases;

import com.outforce.coupon.core.entities.Coupon;
import com.outforce.coupon.core.exceptions.CouponNotFoundException;
import com.outforce.coupon.core.gateways.CouponGateway;

public class DeleteCouponCaseImpl implements DeleteCouponCase {
    private final CouponGateway couponGateway;

    public DeleteCouponCaseImpl(CouponGateway couponGateway) {
        this.couponGateway = couponGateway;
    }

    @Override
    public void execute(Long id) {
        Coupon coupon = couponGateway.findById(id)
                .orElseThrow(() -> new CouponNotFoundException(id));

        coupon.delete();
        couponGateway.save(coupon);
    }
}
