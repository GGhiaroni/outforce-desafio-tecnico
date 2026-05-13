package com.outforce.coupon.core.exceptions;

public class CouponNotFoundException extends RuntimeException {
    public CouponNotFoundException(Long id){
        super(String.format("Coupon with id %d not found", id));
    }
}
