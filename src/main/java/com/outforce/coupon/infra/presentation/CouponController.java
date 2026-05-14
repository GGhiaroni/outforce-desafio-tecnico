package com.outforce.coupon.infra.presentation;

import com.outforce.coupon.core.usecases.CreateCouponCase;
import com.outforce.coupon.core.usecases.DeleteCouponCase;
import com.outforce.coupon.infra.dto.request.CreateCouponRequest;
import com.outforce.coupon.infra.dto.response.CouponResponse;
import com.outforce.coupon.infra.mapper.CouponMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
@Tag(name = "Coupon API", description = "Operações sobre cupons promocionais")
public class CouponController {
    private final CreateCouponCase createCouponCase;
    private final DeleteCouponCase deleteCouponCase;

    @Operation(summary = "Cria um novo cupom")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cupom criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos no corpo da requisição")
    })
    @PostMapping
    public ResponseEntity<CouponResponse> createCoupon(@Valid @RequestBody CreateCouponRequest dto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CouponMapper.toResponse(createCouponCase.execute(CouponMapper.toDomain(dto))));
    }

    @Operation(summary = "Aplica soft delete em um cupom pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cupom deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cupom não encontrado"),
            @ApiResponse(responseCode = "409", description = "Cupom já está deletado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable UUID id){
        deleteCouponCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
