package com.outforce.coupon.infra.dto.response;

import com.outforce.coupon.core.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CouponResponse(
        @Schema(description = "ID único do cupom (UUID gerado pelo banco)", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID id,
        @Schema(description = "Código do cupom sanitizado (apenas alfanuméricos, 6 caracteres)", example = "ABC123")
        String code,
        @Schema(description = "Descrição do cupom", example = "Desconto Black Friday")
        String description,
        @Schema(description = "Valor de desconto", example = "10.00")
        BigDecimal discountValue,
        @Schema(description = "Data de expiração", example = "2026-12-31T23:59:59")
        LocalDateTime expirationDate,
        @Schema(description = "Status derivado: ACTIVE (válido), EXPIRED (vencido), DELETED (soft delete aplicado)")
        Status status,
        @Schema(description = "Indica se o cupom está publicado", example = "false")
        boolean published,
        @Schema(description = "Indica se o cupom foi resgatado", example = "false")
        boolean redeemed
) {
}
