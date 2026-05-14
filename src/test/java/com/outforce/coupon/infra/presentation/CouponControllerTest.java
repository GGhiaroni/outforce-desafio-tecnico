package com.outforce.coupon.infra.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outforce.coupon.core.entities.Coupon;
import com.outforce.coupon.core.exceptions.CouponNotFoundException;
import com.outforce.coupon.core.usecases.CreateCouponCase;
import com.outforce.coupon.core.usecases.DeleteCouponCase;
import com.outforce.coupon.core.valueobjects.CouponCode;
import com.outforce.coupon.core.valueobjects.DiscountValue;
import com.outforce.coupon.infra.dto.request.CreateCouponRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CouponController.class)
@Import(GlobalExceptionHandler.class)
@DisplayName("CouponController (slice)")
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateCouponCase createCouponCase;

    @MockBean
    private DeleteCouponCase deleteCouponCase;

    private static CreateCouponRequest requestValido() {
        return new CreateCouponRequest(
                "ABC123",
                "Cupom de teste",
                new BigDecimal("10.00"),
                LocalDateTime.now().plusDays(30),
                false
        );
    }

    private static Coupon cupomSalvo() {
        return new Coupon(
                UUID.fromString("11111111-1111-1111-1111-111111111111"),
                new CouponCode("ABC123"),
                "Cupom de teste",
                new DiscountValue(new BigDecimal("10.00")),
                LocalDateTime.now().plusDays(30),
                false,
                false,
                false
        );
    }

    @Nested
    @DisplayName("POST /coupon")
    class PostCoupon {

        @Test
        @DisplayName("Retorna 201 Created com CouponResponse no body quando request é válido")
        void retornaCriadoComCouponResponse() throws Exception {
            when(createCouponCase.execute(any(Coupon.class))).thenReturn(cupomSalvo());

            mockMvc.perform(post("/coupon")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestValido())))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value("11111111-1111-1111-1111-111111111111"))
                    .andExpect(jsonPath("$.code").value("ABC123"))
                    .andExpect(jsonPath("$.status").value("ACTIVE"))
                    .andExpect(jsonPath("$.redeemed").value(false));
        }

        @Test
        @DisplayName("Retorna 400 Bad Request com detalhes dos campos quando validação Jakarta falha")
        void retornaBadRequestQuandoValidacaoJakartaFalha() throws Exception {
            CreateCouponRequest requestInvalido = new CreateCouponRequest(
                    "",
                    "",
                    new BigDecimal("0.1"),
                    LocalDateTime.now().minusDays(1),
                    false
            );

            mockMvc.perform(post("/coupon")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestInvalido)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("Validation failed"))
                    .andExpect(jsonPath("$.errors.code").exists())
                    .andExpect(jsonPath("$.errors.description").exists())
                    .andExpect(jsonPath("$.errors.discountValue").exists())
                    .andExpect(jsonPath("$.errors.expirationDate").exists());
        }

        @Test
        @DisplayName("Retorna 400 Bad Request quando o domínio rejeita a criação")
        void retornaBadRequestQuandoDominioRejeita() throws Exception {
            when(createCouponCase.execute(any(Coupon.class)))
                    .thenThrow(new IllegalArgumentException("Coupon code is required"));

            mockMvc.perform(post("/coupon")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestValido())))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("Coupon code is required"));
        }
    }

    @Nested
    @DisplayName("DELETE /coupon/{id}")
    class DeleteCoupon {

        @Test
        @DisplayName("Retorna 204 No Content quando o cupom existe")
        void retornaNoContentQuandoCupomExiste() throws Exception {
            UUID id = UUID.randomUUID();
            doNothing().when(deleteCouponCase).execute(id);

            mockMvc.perform(delete("/coupon/{id}", id))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Retorna 404 Not Found quando o cupom não existe")
        void retornaNotFoundQuandoCupomNaoExiste() throws Exception {
            UUID id = UUID.fromString("99999999-9999-9999-9999-999999999999");
            doThrow(new CouponNotFoundException(id)).when(deleteCouponCase).execute(id);

            mockMvc.perform(delete("/coupon/{id}", id))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail")
                            .value("Coupon with id 99999999-9999-9999-9999-999999999999 not found"));
        }

        @Test
        @DisplayName("Retorna 409 Conflict quando o cupom já está deletado")
        void retornaConflictQuandoCupomJaDeletado() throws Exception {
            UUID id = UUID.randomUUID();
            doThrow(new IllegalStateException("Coupon is already deleted"))
                    .when(deleteCouponCase).execute(id);

            mockMvc.perform(delete("/coupon/{id}", id))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.detail").value("Coupon is already deleted"));
        }
    }
}