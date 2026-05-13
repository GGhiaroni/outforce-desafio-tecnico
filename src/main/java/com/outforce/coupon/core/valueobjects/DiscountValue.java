package com.outforce.coupon.core.valueobjects;

import java.math.BigDecimal;

public record DiscountValue(BigDecimal value) {

    private static final BigDecimal MIN_VALUE = new BigDecimal("0.5");

    public DiscountValue {
        if (value == null) {
            throw new IllegalArgumentException("Discount value cannot be null");
        }
        if (value.compareTo(MIN_VALUE) < 0) {
            throw new IllegalArgumentException("Discount value must be at least 0.5");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof DiscountValue other)) return false;
        return value.compareTo(other.value) == 0;
    }

    @Override
    public int hashCode() {
        return value.stripTrailingZeros().toPlainString().hashCode();
    }
}