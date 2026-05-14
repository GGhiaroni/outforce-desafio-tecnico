package com.outforce.coupon.core.entities;

import com.outforce.coupon.core.valueobjects.CouponCode;
import com.outforce.coupon.core.valueobjects.DiscountValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Coupon")
class CouponTest {

    private static final CouponCode VALID_CODE = new CouponCode("ABC123");
    private static final String VALID_DESCRIPTION = "Desconto Black Friday";
    private static final DiscountValue VALID_DISCOUNT = new DiscountValue(new BigDecimal("10.0"));
    private static final LocalDateTime FUTURE_DATE = LocalDateTime.now().plusDays(30);
    private static final LocalDateTime PAST_DATE = LocalDateTime.now().minusDays(1);

    @Nested
    @DisplayName("Quando criado com dados válidos")
    class QuandoCriadoComDadosValidos {

        @Test
        @DisplayName("Cria um cupom com todos os campos preenchidos corretamente")
        void criaCupomComCamposPreenchidos() {
            Coupon coupon = Coupon.create(VALID_CODE, VALID_DESCRIPTION, VALID_DISCOUNT, FUTURE_DATE, false);

            assertNull(coupon.getId());
            assertEquals(VALID_CODE, coupon.getCode());
            assertEquals(VALID_DESCRIPTION, coupon.getDescription());
            assertEquals(VALID_DISCOUNT, coupon.getDiscountValue());
            assertEquals(FUTURE_DATE, coupon.getExpirationDate());
            assertFalse(coupon.isPublished());
            assertFalse(coupon.isDeleted());
        }

        @Test
        @DisplayName("Cria um cupom como já publicado quando informado")
        void criaCupomJaPublicado() {
            Coupon coupon = Coupon.create(VALID_CODE, VALID_DESCRIPTION, VALID_DISCOUNT, FUTURE_DATE, true);

            assertTrue(coupon.isPublished());
        }

        @Test
        @DisplayName("Aceita data de expiração no futuro próximo")
        void aceitaDataDeExpiracaoNoFuturoProximo() {
            assertDoesNotThrow(
                    () -> Coupon.create(VALID_CODE, VALID_DESCRIPTION, VALID_DISCOUNT, LocalDateTime.now().plusSeconds(1), false)
            );
        }
    }

    @Nested
    @DisplayName("Quando criado com dados inválidos")
    class QuandoCriadoComDadosInvalidos {

        @Test
        @DisplayName("Lança exceção quando code é null")
        void lancaExcecaoQuandoCodeEhNull() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> Coupon.create(null, VALID_DESCRIPTION, VALID_DISCOUNT, FUTURE_DATE, false)
            );
            assertEquals("Coupon code is required", ex.getMessage());
        }

        @Test
        @DisplayName("Lança exceção quando description é null")
        void lancaExcecaoQuandoDescriptionEhNull() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> Coupon.create(VALID_CODE, null, VALID_DISCOUNT, FUTURE_DATE, false)
            );
            assertEquals("Coupon description is required", ex.getMessage());
        }

        @Test
        @DisplayName("Lança exceção quando description está em branco")
        void lancaExcecaoQuandoDescriptionEhBranca() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Coupon.create(VALID_CODE, "   ", VALID_DISCOUNT, FUTURE_DATE, false)
            );
        }

        @Test
        @DisplayName("Lança exceção quando discountValue é null")
        void lancaExcecaoQuandoDiscountValueEhNull() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> Coupon.create(VALID_CODE, VALID_DESCRIPTION, null, FUTURE_DATE, false)
            );
            assertEquals("Discount value is required", ex.getMessage());
        }

        @Test
        @DisplayName("Lança exceção quando expirationDate é null")
        void lancaExcecaoQuandoExpirationDateEhNull() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> Coupon.create(VALID_CODE, VALID_DESCRIPTION, VALID_DISCOUNT, null, false)
            );
            assertEquals("Expiration date is required", ex.getMessage());
        }

        @Test
        @DisplayName("Lança exceção quando expirationDate está no passado")
        void lancaExcecaoQuandoExpirationDateEstaNoPassado() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> Coupon.create(VALID_CODE, VALID_DESCRIPTION, VALID_DISCOUNT, PAST_DATE, false)
            );
            assertEquals("Expiration date cannot be in the past", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("Quando deletado")
    class QuandoDeletado {

        @Test
        @DisplayName("Cupom recém-criado não está deletado")
        void cupomRecemCriadoNaoEstaDeletado() {
            Coupon coupon = Coupon.create(VALID_CODE, VALID_DESCRIPTION, VALID_DISCOUNT, FUTURE_DATE, false);
            assertFalse(coupon.isDeleted());
        }

        @Test
        @DisplayName("Aplica soft delete em cupom ativo")
        void aplicaSoftDeleteEmCupomAtivo() {
            Coupon coupon = Coupon.create(VALID_CODE, VALID_DESCRIPTION, VALID_DISCOUNT, FUTURE_DATE, false);

            coupon.delete();

            assertTrue(coupon.isDeleted());
        }

        @Test
        @DisplayName("Lança exceção ao tentar deletar cupom já deletado")
        void lancaExcecaoAoDeletarCupomJaDeletado() {
            Coupon coupon = Coupon.create(VALID_CODE, VALID_DESCRIPTION, VALID_DISCOUNT, FUTURE_DATE, false);
            coupon.delete();

            IllegalStateException ex = assertThrows(
                    IllegalStateException.class,
                    coupon::delete
            );
            assertEquals("Coupon is already deleted", ex.getMessage());
        }
    }
}