package com.outforce.coupon.infra.presentation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outforce.coupon.infra.dto.request.CreateCouponRequest;
import com.outforce.coupon.infra.persistence.CouponEntity;
import com.outforce.coupon.infra.persistence.CouponRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Coupon end-to-end")
class CouponEndToEndTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CouponRepository repository;

    @AfterEach
    void cleanup() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("POST cria cupom no banco com id UUID gerado e campos persistidos corretamente")
    void postCriaCupomNoBanco() throws Exception {
        CreateCouponRequest request = new CreateCouponRequest(
                "ABC123",
                "Cupom end-to-end",
                new BigDecimal("15.00"),
                LocalDateTime.now().plusDays(60),
                true
        );

        MvcResult result = mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andReturn();

        JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
        UUID id = UUID.fromString(response.get("id").asText());

        Optional<CouponEntity> persistido = repository.findById(id);
        assertTrue(persistido.isPresent());
        assertEquals("ABC123", persistido.get().getCode());
        assertEquals("Cupom end-to-end", persistido.get().getDescription());
        assertEquals(0, new BigDecimal("15.00").compareTo(persistido.get().getDiscountValue()));
        assertTrue(persistido.get().isPublished());
        assertFalse(persistido.get().isRedeemed());
        assertFalse(persistido.get().isDeleted());
    }

    @Test
    @DisplayName("POST sanitiza caracteres especiais do code antes de persistir")
    void postSanitizaCode() throws Exception {
        CreateCouponRequest request = new CreateCouponRequest(
                "ABC-123",
                "Cupom com hifen no code",
                new BigDecimal("10.00"),
                LocalDateTime.now().plusDays(30),
                false
        );

        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("ABC123"));

        List<CouponEntity> all = repository.findAll();
        assertEquals(1, all.size());
        assertEquals("ABC123", all.get(0).getCode());
    }

    @Test
    @DisplayName("DELETE aplica soft delete preservando a linha no banco com deleted=true")
    void deleteAplicaSoftDelete() throws Exception {
        CreateCouponRequest request = new CreateCouponRequest(
                "DEL001",
                "Cupom a deletar",
                new BigDecimal("5.00"),
                LocalDateTime.now().plusDays(15),
                false
        );

        MvcResult postResult = mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode response = objectMapper.readTree(postResult.getResponse().getContentAsString());
        UUID id = UUID.fromString(response.get("id").asText());

        mockMvc.perform(delete("/coupon/{id}", id))
                .andExpect(status().isNoContent());

        Optional<CouponEntity> deletado = repository.findById(id);
        assertTrue(deletado.isPresent(), "Soft delete: a linha deve continuar no banco");
        assertTrue(deletado.get().isDeleted(), "Flag deleted=true deve estar persistida");
    }

    @Test
    @DisplayName("DELETE em id inexistente retorna 404 Not Found com ProblemDetail")
    void deleteIdInexistenteRetornaNotFound() throws Exception {
        UUID idInexistente = UUID.randomUUID();

        mockMvc.perform(delete("/coupon/{id}", idInexistente))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail")
                        .value("Coupon with id " + idInexistente + " not found"));
    }

    @Test
    @DisplayName("DELETE em cupom já deletado retorna 409 Conflict")
    void deleteCupomJaDeletadoRetornaConflict() throws Exception {
        CreateCouponRequest request = new CreateCouponRequest(
                "DBL001",
                "Cupom para deletar duas vezes",
                new BigDecimal("5.00"),
                LocalDateTime.now().plusDays(15),
                false
        );

        MvcResult postResult = mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode response = objectMapper.readTree(postResult.getResponse().getContentAsString());
        UUID id = UUID.fromString(response.get("id").asText());

        mockMvc.perform(delete("/coupon/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/coupon/{id}", id))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Coupon is already deleted"));
    }
}