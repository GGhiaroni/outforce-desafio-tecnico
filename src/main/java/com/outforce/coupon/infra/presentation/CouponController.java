package com.outforce.coupon.infra.presentation;

import com.outforce.coupon.core.usecases.CreateCouponCase;
import com.outforce.coupon.core.usecases.DeleteCouponCase;
import com.outforce.coupon.infra.dto.request.CreateCouponRequest;
import com.outforce.coupon.infra.dto.response.CouponResponse;
import com.outforce.coupon.infra.mapper.CouponMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {
    private final CreateCouponCase createCouponCase;
    private final DeleteCouponCase deleteCouponCase;

    @PostMapping
    public ResponseEntity<CouponResponse> createCoupon(@Valid @RequestBody CreateCouponRequest dto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CouponMapper.toResponse(createCouponCase.execute(CouponMapper.toDomain(dto))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable UUID id){
        deleteCouponCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
