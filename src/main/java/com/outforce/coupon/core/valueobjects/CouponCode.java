package com.outforce.coupon.core.valueobjects;

public record CouponCode(String value) {

    public CouponCode {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Coupon code cannot be null or blank");
        }

        String sanitized = sanitize(value);

        if (sanitized.length() != 6) {
            throw new IllegalArgumentException("Coupon code must contain 6 alphanumeric characters");
        }

        value = sanitized;
    }

    private static String sanitize(String value) {
        return value.replaceAll("[^a-zA-Z0-9]", "");
    }
}
