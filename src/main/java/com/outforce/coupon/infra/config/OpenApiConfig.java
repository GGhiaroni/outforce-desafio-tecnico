package com.outforce.coupon.infra.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI couponOpenAPI() {

        Contact contact = new Contact();
        contact.setUrl("https://github.com/GGhiaroni/outforce-desafio-tecnico");
        contact.setName("Gabriel Tiziano");
        contact.setEmail("gghiaronitiziano@gmail.com");

        Info info = new Info();
        info.title("Desafio técnico Outforce | Coupon API");
        info.version("1.0.0");
        info.description("API de gerenciamento de cupons promocionais | desafio técnico Outforce | Gabriel Tiziano");
        info.contact(contact);

        return new OpenAPI().info(info);
    }
}