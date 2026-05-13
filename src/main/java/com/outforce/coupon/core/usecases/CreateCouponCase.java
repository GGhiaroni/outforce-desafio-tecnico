package com.outforce.coupon.core.usecases;

import com.outforce.coupon.core.entities.Coupon;

public interface CreateCouponCase {
    Coupon execute(Coupon coupon);
}
