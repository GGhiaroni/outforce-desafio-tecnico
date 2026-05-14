package com.outforce.coupon.infra.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateCouponRequest(
        @Schema(description = "Código alfanumérico de 6 caracteres. Caracteres especiais são removidos automaticamente.", example = "ABC123")
        @NotBlank
        String code,
        @Schema(description = "Descrição do cupom", example = "Desconto Black Friday")
        @NotBlank
        String description,
        @Schema(description = "Valor de desconto. Mínimo de 0.5, sem máximo. Sem preocupações com moeda.", example = "10.00")
        @NotNull
        @DecimalMin("0.5")
        BigDecimal discountValue,
        @Schema(description = "Data de expiração no formato ISO-8601. Deve ser uma data futura.", example = "2026-12-31T23:59:59")
        @NotNull
        @Future
        LocalDateTime expirationDate,
        @Schema(description = "Define se o cupom já é publicado no momento da criação. Default: false.", example = "false")
        Boolean published
) {
}
