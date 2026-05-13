package com.outforce.coupon.core.usecases;

import com.outforce.coupon.core.entities.Coupon;
import com.outforce.coupon.core.exceptions.CouponNotFoundException;
import com.outforce.coupon.core.gateways.CouponGateway;
import com.outforce.coupon.core.valueobjects.CouponCode;
import com.outforce.coupon.core.valueobjects.DiscountValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteCouponCaseImpl")
class DeleteCouponCaseImplTest {

    @Mock
    private CouponGateway gateway;

    @InjectMocks
    private DeleteCouponCaseImpl useCase;

    private static Coupon novoCupomValido() {
        return Coupon.create(
                new CouponCode("ABC123"),
                "Cupom de teste",
                new DiscountValue(new BigDecimal("10.00")),
                LocalDate.now().plusDays(30),
                false
        );
    }

    @Nested
    @DisplayName("Quando o cupom existe e ainda não foi deletado")
    class QuandoCupomExisteENaoFoiDeletado {

        @Test
        @DisplayName("Busca, aplica delete no dominio e persiste o cupom atualizado")
        void buscaAplicaDeleteEPersiste() {
            Long id = 1L;
            Coupon cupom = novoCupomValido();
            when(gateway.findById(id)).thenReturn(Optional.of(cupom));

            useCase.execute(id);

            assertTrue(cupom.isDeleted(),
                    "O cupom deve estar marcado como deletado após o use case rodar");

            InOrder inOrder = inOrder(gateway);
            inOrder.verify(gateway).findById(id);
            inOrder.verify(gateway).save(cupom);
        }
    }

    @Nested
    @DisplayName("Quando o cupom não existe")
    class QuandoCupomNaoExiste {

        @Test
        @DisplayName("Lança CouponNotFoundException com a mensagem correta e não persiste nada")
        void lancaCouponNotFoundException() {
            Long id = 99L;
            when(gateway.findById(id)).thenReturn(Optional.empty());

            CouponNotFoundException ex = assertThrows(
                    CouponNotFoundException.class,
                    () -> useCase.execute(id)
            );
            assertEquals("Coupon with id 99 not found", ex.getMessage());

            verify(gateway, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Quando o cupom já está deletado")
    class QuandoCupomJaEstaDeletado {

        @Test
        @DisplayName("Propaga IllegalStateException do domínio e não persiste novamente")
        void propagaIllegalStateException() {
            Long id = 1L;
            Coupon cupomJaDeletado = novoCupomValido();
            cupomJaDeletado.delete();
            when(gateway.findById(id)).thenReturn(Optional.of(cupomJaDeletado));

            assertThrows(
                    IllegalStateException.class,
                    () -> useCase.execute(id)
            );

            verify(gateway, never()).save(any());
        }
    }
}