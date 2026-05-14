package com.outforce.coupon.infra.mapper;

import com.outforce.coupon.core.entities.Coupon;
import com.outforce.coupon.core.enums.Status;
import com.outforce.coupon.core.valueobjects.CouponCode;
import com.outforce.coupon.core.valueobjects.DiscountValue;
import com.outforce.coupon.infra.dto.request.CreateCouponRequest;
import com.outforce.coupon.infra.dto.response.CouponResponse;
import com.outforce.coupon.infra.persistence.CouponEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CouponMapper")
class CouponMapperTest {

    private static final String CODE = "ABC123";
    private static final String DESCRIPTION = "Cupom de teste";
    private static final BigDecimal DISCOUNT = new BigDecimal("10.00");
    private static final LocalDateTime FUTURE_DATE = LocalDateTime.now().plusDays(30);
    private static final LocalDateTime PAST_DATE = LocalDateTime.now().minusDays(1);

    @Nested
    @DisplayName("Quando converte CreateCouponRequest para domain")
    class QuandoConverteRequestParaDomain {

        @Test
        @DisplayName("Preserva todos os campos e aplica defaults da factory")
        void preservaCamposEAplicaDefaults() {
            CreateCouponRequest request = new CreateCouponRequest(CODE, DESCRIPTION, DISCOUNT, FUTURE_DATE, true);

            Coupon coupon = CouponMapper.toDomain(request);

            assertNull(coupon.getId());
            assertEquals(CODE, coupon.getCode().value());
            assertEquals(DESCRIPTION, coupon.getDescription());
            assertEquals(0, DISCOUNT.compareTo(coupon.getDiscountValue().value()));
            assertEquals(FUTURE_DATE, coupon.getExpirationDate());
            assertTrue(coupon.isPublished());
            assertFalse(coupon.isRedeemed());
            assertFalse(coupon.isDeleted());
        }

        @Test
        @DisplayName("Trata published null como false")
        void trataPublishedNullComoFalse() {
            CreateCouponRequest request = new CreateCouponRequest(CODE, DESCRIPTION, DISCOUNT, FUTURE_DATE, null);

            Coupon coupon = CouponMapper.toDomain(request);

            assertFalse(coupon.isPublished());
        }

        @Test
        @DisplayName("Propaga IllegalArgumentException quando expirationDate está no passado")
        void propagaExcecaoQuandoDataNoPassado() {
            CreateCouponRequest request = new CreateCouponRequest(CODE, DESCRIPTION, DISCOUNT, PAST_DATE, false);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> CouponMapper.toDomain(request)
            );
        }
    }

    @Nested
    @DisplayName("Quando converte CouponEntity para domain (reidratação do banco)")
    class QuandoConverteEntityParaDomain {

        @Test
        @DisplayName("Preserva todos os campos incluindo id, deleted e redeemed")
        void preservaTodosOsCampos() {
            UUID id = UUID.randomUUID();
            CouponEntity entity = new CouponEntity(id, CODE, DESCRIPTION, DISCOUNT, FUTURE_DATE, true, true, true);

            Coupon coupon = CouponMapper.toDomain(entity);

            assertEquals(id, coupon.getId());
            assertEquals(CODE, coupon.getCode().value());
            assertEquals(DESCRIPTION, coupon.getDescription());
            assertEquals(0, DISCOUNT.compareTo(coupon.getDiscountValue().value()));
            assertEquals(FUTURE_DATE, coupon.getExpirationDate());
            assertTrue(coupon.isPublished());
            assertTrue(coupon.isRedeemed());
            assertTrue(coupon.isDeleted());
        }

        @Test
        @DisplayName("Aceita expirationDate no passado (não revalida regras de criação)")
        void aceitaExpirationDateNoPassado() {
            CouponEntity entityExpirada = new CouponEntity(
                    UUID.randomUUID(), CODE, DESCRIPTION, DISCOUNT, PAST_DATE, false, false, false
            );

            assertDoesNotThrow(() -> CouponMapper.toDomain(entityExpirada));
        }
    }

    @Nested
    @DisplayName("Quando converte domain para CouponEntity")
    class QuandoConverteDomainParaEntity {

        @Test
        @DisplayName("Preserva todos os campos extraindo valores dos value objects")
        void preservaTodosOsCampos() {
            UUID id = UUID.randomUUID();
            Coupon coupon = new Coupon(
                    id,
                    new CouponCode(CODE),
                    DESCRIPTION,
                    new DiscountValue(DISCOUNT),
                    FUTURE_DATE,
                    true,
                    true,
                    true
            );

            CouponEntity entity = CouponMapper.toEntity(coupon);

            assertEquals(id, entity.getId());
            assertEquals(CODE, entity.getCode());
            assertEquals(DESCRIPTION, entity.getDescription());
            assertEquals(0, DISCOUNT.compareTo(entity.getDiscountValue()));
            assertEquals(FUTURE_DATE, entity.getExpirationDate());
            assertTrue(entity.isPublished());
            assertTrue(entity.isRedeemed());
            assertTrue(entity.isDeleted());
        }
    }

    @Nested
    @DisplayName("Quando converte domain para CouponResponse")
    class QuandoConverteDomainParaResponse {

        @Test
        @DisplayName("Preserva campos e mapeia status ACTIVE para cupom ativo")
        void mapeiaStatusActive() {
            Coupon coupon = Coupon.create(
                    new CouponCode(CODE), DESCRIPTION, new DiscountValue(DISCOUNT), FUTURE_DATE, false
            );

            CouponResponse response = CouponMapper.toResponse(coupon);

            assertNull(response.id());
            assertEquals(CODE, response.code());
            assertEquals(DESCRIPTION, response.description());
            assertEquals(0, DISCOUNT.compareTo(response.discountValue()));
            assertEquals(FUTURE_DATE, response.expirationDate());
            assertEquals(Status.ACTIVE, response.status());
            assertFalse(response.published());
            assertFalse(response.redeemed());
        }

        @Test
        @DisplayName("Mapeia status DELETED quando cupom está soft-deletado")
        void mapeiaStatusDeleted() {
            Coupon coupon = Coupon.create(
                    new CouponCode(CODE), DESCRIPTION, new DiscountValue(DISCOUNT), FUTURE_DATE, false
            );
            coupon.delete();

            CouponResponse response = CouponMapper.toResponse(coupon);

            assertEquals(Status.DELETED, response.status());
        }

        @Test
        @DisplayName("Mapeia status EXPIRED quando expirationDate está no passado")
        void mapeiaStatusExpired() {
            Coupon cupomExpirado = new Coupon(
                    UUID.randomUUID(),
                    new CouponCode(CODE),
                    DESCRIPTION,
                    new DiscountValue(DISCOUNT),
                    PAST_DATE,
                    false,
                    false,
                    false
            );

            CouponResponse response = CouponMapper.toResponse(cupomExpirado);

            assertEquals(Status.EXPIRED, response.status());
        }
    }

    @Nested
    @DisplayName("Round-trip entity → domain → entity")
    class RoundTrip {

        @Test
        @DisplayName("Preserva todos os campos sem perda de informação")
        void preservaTodosOsCamposAposIdaEVolta() {
            UUID id = UUID.randomUUID();
            CouponEntity original = new CouponEntity(
                    id, CODE, DESCRIPTION, DISCOUNT, FUTURE_DATE, true, true, true
            );

            CouponEntity resultado = CouponMapper.toEntity(CouponMapper.toDomain(original));

            assertEquals(original.getId(), resultado.getId());
            assertEquals(original.getCode(), resultado.getCode());
            assertEquals(original.getDescription(), resultado.getDescription());
            assertEquals(0, original.getDiscountValue().compareTo(resultado.getDiscountValue()));
            assertEquals(original.getExpirationDate(), resultado.getExpirationDate());
            assertEquals(original.isPublished(), resultado.isPublished());
            assertEquals(original.isRedeemed(), resultado.isRedeemed());
            assertEquals(original.isDeleted(), resultado.isDeleted());
        }
    }
}