package com.outforce.coupon.core.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("CouponNotFoundException")
class CouponNotFoundExceptionTest {

    @Test
    @DisplayName("Cria a exceção com a mensagem formatada incluindo o id")
    void criaExcecaoComMensagemFormatada() {
        CouponNotFoundException exception = new CouponNotFoundException(10L);

        assertEquals(
                "Coupon with id 10 not found",
                exception.getMessage()
        );
    }
}