package com.outforce.coupon.core.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CouponNotFoundExceptionTest {

    @Test
    void shouldCreateExceptionWithCorrectMessage() {
        CouponNotFoundException exception =
                new CouponNotFoundException(10L);

        assertEquals(
                "Coupon with id 10 not found",
                exception.getMessage()
        );
    }
}
