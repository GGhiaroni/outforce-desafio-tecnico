package com.outforce.coupon.infra.gateways;

import com.outforce.coupon.core.entities.Coupon;
import com.outforce.coupon.core.gateways.CouponGateway;
import com.outforce.coupon.infra.mapper.CouponMapper;
import com.outforce.coupon.infra.persistence.CouponRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CouponRepositoryGateway implements CouponGateway {
    private final CouponRepository couponRepository;

    public CouponRepositoryGateway(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Override
    public Coupon save(Coupon coupon) {
        return CouponMapper.toDomain(couponRepository.save(CouponMapper.toEntity(coupon)));
    }

    @Override
    public Optional<Coupon> findById(UUID id) {
        return couponRepository.findById(id)
                .map(CouponMapper::toDomain);
    }
}
