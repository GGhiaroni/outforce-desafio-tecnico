package com.outforce.coupon.core.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("CouponNotFoundException")
class CouponNotFoundExceptionTest {

    @Test
    @DisplayName("Cria a exceção com a mensagem formatada incluindo o id")
    void criaExcecaoComMensagemFormatada() {
        UUID id = UUID.fromString("11111111-1111-1111-1111-111111111111");

        CouponNotFoundException exception = new CouponNotFoundException(id);

        assertEquals(
                "Coupon with id 11111111-1111-1111-1111-111111111111 not found",
                exception.getMessage()
        );
    }
}