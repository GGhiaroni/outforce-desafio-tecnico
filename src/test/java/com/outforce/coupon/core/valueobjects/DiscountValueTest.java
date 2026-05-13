package com.outforce.coupon.core.valueobjects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("DiscountValue")
class DiscountValueTest {

    @Nested
    @DisplayName("Quando o valor é válido")
    class QuandoValorEhValido {

        @Test
        @DisplayName("Aceita exatamente 0.5 (limite inferior inclusivo)")
        void aceitaValorIgualAoMinimo() {
            DiscountValue dv = new DiscountValue(new BigDecimal("0.5"));
            assertEquals(0, dv.value().compareTo(new BigDecimal("0.5")));
        }

        @Test
        @DisplayName("Aceita valor maior que 0.5")
        void aceitaValorMaiorQueMinimo() {
            DiscountValue dv = new DiscountValue(new BigDecimal("1.0"));
            assertEquals(0, dv.value().compareTo(new BigDecimal("1.0")));
        }

        @Test
        @DisplayName("Aceita valores muito altos (sem máximo predeterminado)")
        void aceitaValoresMuitoAltos() {
            DiscountValue dv = new DiscountValue(new BigDecimal("999999.99"));
            assertEquals(0, dv.value().compareTo(new BigDecimal("999999.99")));
        }

        @Test
        @DisplayName("Dois DiscountValues com escalas diferentes mas mesmo valor são iguais")
        void doisDiscountValuesComEscalasDiferentesSaoIguais() {
            DiscountValue a = new DiscountValue(new BigDecimal("0.5"));
            DiscountValue b = new DiscountValue(new BigDecimal("0.50"));

            assertEquals(a, b);
            assertEquals(a.hashCode(), b.hashCode());
        }
    }

    @Nested
    @DisplayName("Quando o valor é inválido")
    class QuandoValorEhInvalido {

        @Test
        @DisplayName("Lança exceção para valor abaixo de 0.5")
        void lancaExcecaoParaValorAbaixoDoMinimo() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> new DiscountValue(new BigDecimal("0.49"))
            );

            assertEquals("Discount value must be at least 0.5", ex.getMessage());
        }

        @Test
        @DisplayName("Lança exceção para valor zero")
        void lancaExcecaoParaValorZero() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new DiscountValue(BigDecimal.ZERO)
            );
        }

        @Test
        @DisplayName("Lança exceção para valor negativo")
        void lancaExcecaoParaValorNegativo() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new DiscountValue(new BigDecimal("-1"))
            );
        }

        @Test
        @DisplayName("Lança exceção para valor null")
        void lancaExcecaoParaValorNull() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> new DiscountValue(null)
            );

            assertEquals("Discount value cannot be null", ex.getMessage());
        }
    }
}