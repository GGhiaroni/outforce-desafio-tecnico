package com.outforce.coupon.core.usecases;

import com.outforce.coupon.core.entities.Coupon;
import com.outforce.coupon.core.gateways.CouponGateway;
import com.outforce.coupon.core.valueobjects.CouponCode;
import com.outforce.coupon.core.valueobjects.DiscountValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateCouponCaseImpl")
class CreateCouponCaseImplTest {

    @Mock
    private CouponGateway gateway;

    @InjectMocks
    private CreateCouponCaseImpl useCase;

    private static Coupon novoCupomValido() {
        return Coupon.create(
                new CouponCode("ABC123"),
                "Cupom de teste",
                new DiscountValue(new BigDecimal("10.00")),
                LocalDate.now().plusDays(30),
                false
        );
    }

    @Test
    @DisplayName("Delega a persistência ao gateway e retorna o cupom salvo")
    void delegaPersistenciaAoGateway() {

        Coupon entrada = novoCupomValido();
        Coupon saida = novoCupomValido();
        when(gateway.save(entrada)).thenReturn(saida);

        Coupon resultado = useCase.execute(entrada);

        assertSame(saida, resultado);
        verify(gateway, times(1)).save(entrada);
    }
}