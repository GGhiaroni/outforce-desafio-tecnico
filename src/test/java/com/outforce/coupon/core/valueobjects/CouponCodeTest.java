package com.outforce.coupon.core.valueobjects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("CouponCode")
class CouponCodeTest {

    @Nested
    @DisplayName("Quando o código é válido")
    class QuandoCodigoEhValido {

        @Test
        @DisplayName("Aceita código alfanumérico de exatamente 6 caracteres")
        void aceitaCodigoAlfanumericoDeSeisCaracteres() {
            CouponCode code = new CouponCode("ABC123");
            assertEquals("ABC123", code.value());
        }

        @Test
        @DisplayName("Remove caracteres especiais e mantém os 6 alfanuméricos")
        void removeCaracteresEspeciaisEMantemSeisAlfanumericos() {
            CouponCode code = new CouponCode("AB-C1$23");
            assertEquals("ABC123", code.value());
        }

        @Test
        @DisplayName("Dois CouponCodes com mesmo valor normalizado são iguais")
        void doisCouponCodesComMesmoValorNormalizadoSaoIguais() {
            CouponCode code1 = new CouponCode("ABC123");
            CouponCode code2 = new CouponCode("AB-C1$23");

            assertEquals(code1, code2);
            assertEquals(code1.hashCode(), code2.hashCode());
        }
    }

    @Nested
    @DisplayName("Quando o código é inválido")
    class QuandoCodigoEhInvalido {

        @Test
        @DisplayName("Lança exceção quando sobram menos de 6 caracteres após remoção")
        void lancaExcecaoQuandoSobramMenosDeSeisCaracteres() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> new CouponCode("AB-12#3")
            );

            assertEquals("Coupon code must contain 6 alphanumeric characters", ex.getMessage());
        }

        @Test
        @DisplayName("Lança exceção quando há mais de 6 caracteres alfanuméricos")
        void lancaExcecaoQuandoHaMaisDeSeisCaracteresAlfanumericos() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new CouponCode("ABC1234")
            );
        }

        @Test
        @DisplayName("Lança exceção quando o valor é null")
        void lancaExcecaoQuandoValorEhNull() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> new CouponCode(null)
            );

            assertEquals("Coupon code cannot be null or blank", ex.getMessage());
        }

        @Test
        @DisplayName("Lança exceção quando o valor é vazio")
        void lancaExcecaoQuandoValorEhVazio() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new CouponCode("")
            );
        }
    }
}