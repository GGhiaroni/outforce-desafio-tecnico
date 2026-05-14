package com.outforce.coupon.infra.gateways;

import com.outforce.coupon.core.entities.Coupon;
import com.outforce.coupon.core.enums.Status;
import com.outforce.coupon.core.valueobjects.CouponCode;
import com.outforce.coupon.core.valueobjects.DiscountValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(CouponRepositoryGateway.class)
@DisplayName("CouponRepositoryGateway (integração)")
class CouponRepositoryGatewayIntegrationTest {

    @Autowired
    private CouponRepositoryGateway gateway;

    private static Coupon novoCupom(String code, boolean published) {
        return Coupon.create(
                new CouponCode(code),
                "Cupom de teste",
                new DiscountValue(new BigDecimal("10.00")),
                LocalDateTime.now().plusDays(30),
                published
        );
    }

    @Nested
    @DisplayName("Quando salva um cupom novo")
    class QuandoSalvaCupomNovo {

        @Test
        @DisplayName("Atribui id UUID gerado pelo Hibernate e retorna o cupom persistido")
        void atribuiIdGerado() {
            Coupon novo = novoCupom("ABC123", false);
            assertNull(novo.getId());

            Coupon salvo = gateway.save(novo);

            assertNotNull(salvo.getId());
            assertEquals("ABC123", salvo.getCode().value());
        }
    }

    @Nested
    @DisplayName("Quando busca por id")
    class QuandoBuscaPorId {

        @Test
        @DisplayName("Retorna Optional com o cupom quando id existe")
        void retornaCupomQuandoIdExiste() {
            Coupon salvo = gateway.save(novoCupom("XYZ789", true));

            Optional<Coupon> encontrado = gateway.findById(salvo.getId());

            assertTrue(encontrado.isPresent());
            assertEquals(salvo.getId(), encontrado.get().getId());
            assertEquals("XYZ789", encontrado.get().getCode().value());
            assertTrue(encontrado.get().isPublished());
        }

        @Test
        @DisplayName("Retorna Optional.empty() quando id não existe")
        void retornaVazioQuandoIdNaoExiste() {
            Optional<Coupon> encontrado = gateway.findById(UUID.randomUUID());

            assertTrue(encontrado.isEmpty());
        }
    }

    @Nested
    @DisplayName("Quando aplica soft delete")
    class QuandoAplicaSoftDelete {

        @Test
        @DisplayName("Persiste flag deleted=true e status DELETED é derivado corretamente")
        void persisteFlagDeletedECalculaStatus() {
            Coupon salvo = gateway.save(novoCupom("DEL001", false));
            salvo.delete();
            gateway.save(salvo);

            Optional<Coupon> encontrado = gateway.findById(salvo.getId());

            assertTrue(encontrado.isPresent());
            assertTrue(encontrado.get().isDeleted());
            assertEquals(Status.DELETED, encontrado.get().getStatus());
        }
    }

    @Nested
    @DisplayName("Quando reidrata cupom do banco")
    class QuandoReidrataCupom {

        @Test
        @DisplayName("Preserva campo redeemed=true persistido via construtor canônico")
        void preservaRedeemedTrue() {
            Coupon cupomComRedeemed = new Coupon(
                    null,
                    new CouponCode("RDM001"),
                    "Cupom com redeemed true",
                    new DiscountValue(new BigDecimal("8.00")),
                    LocalDateTime.now().plusDays(30),
                    false,
                    true,
                    false
            );

            Coupon salvo = gateway.save(cupomComRedeemed);
            Optional<Coupon> encontrado = gateway.findById(salvo.getId());

            assertTrue(encontrado.isPresent());
            assertTrue(encontrado.get().isRedeemed());
        }
    }
}