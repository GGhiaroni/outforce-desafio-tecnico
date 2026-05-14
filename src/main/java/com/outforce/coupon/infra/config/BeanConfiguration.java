package com.outforce.coupon.infra.config;

import com.outforce.coupon.core.gateways.CouponGateway;
import com.outforce.coupon.core.usecases.CreateCouponCase;
import com.outforce.coupon.core.usecases.CreateCouponCaseImpl;
import com.outforce.coupon.core.usecases.DeleteCouponCase;
import com.outforce.coupon.core.usecases.DeleteCouponCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Bean
    public CreateCouponCase createCouponCase(CouponGateway couponGateway) {
        return new CreateCouponCaseImpl(couponGateway);
    }

    @Bean
    public DeleteCouponCase deleteCouponCase(CouponGateway couponGateway) {
        return new DeleteCouponCaseImpl(couponGateway);
    }
}
