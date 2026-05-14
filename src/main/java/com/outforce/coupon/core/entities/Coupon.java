package com.outforce.coupon.core.entities;

import com.outforce.coupon.core.valueobjects.CouponCode;
import com.outforce.coupon.core.valueobjects.DiscountValue;

import java.time.LocalDateTime;
import java.util.UUID;

public class Coupon {
    private final UUID id;
    private final CouponCode code;
    private final String description;
    private final DiscountValue discountValue;
    private final LocalDateTime expirationDate;
    private final boolean published;
    private boolean deleted;

    public Coupon(UUID id, CouponCode code, String description, DiscountValue discountValue, LocalDateTime expirationDate, boolean published, boolean deleted) {

        if (code == null) {
            throw new IllegalArgumentException("Coupon code is required");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Coupon description is required");
        }
        if (discountValue == null) {
            throw new IllegalArgumentException("Discount value is required");
        }
        if (expirationDate == null) {
            throw new IllegalArgumentException("Expiration date is required");
        }

        this.id = id;
        this.code = code;
        this.description = description;
        this.discountValue = discountValue;
        this.expirationDate = expirationDate;
        this.published = published;
        this.deleted = deleted;
    }

    public static Coupon create(CouponCode code,
                                String description,
                                DiscountValue discountValue,
                                LocalDateTime expirationDate,
                                boolean published) {
        if (expirationDate != null && expirationDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Expiration date cannot be in the past");
        }
        return new Coupon(null, code, description, discountValue, expirationDate, published, false);
    }

    public void delete() {
        if (deleted) {
            throw new IllegalStateException("Coupon is already deleted");
        }
        this.deleted = true;
    }

    public UUID getId() {
        return id;
    }

    public CouponCode getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public DiscountValue getDiscountValue() {
        return discountValue;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public boolean isPublished() {
        return published;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
