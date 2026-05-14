package com.outforce.coupon.core.exceptions;

import java.util.UUID;

public class CouponNotFoundException extends RuntimeException {
    public CouponNotFoundException(UUID id){
        super(String.format("Coupon with id %s not found", id));
    }
}
