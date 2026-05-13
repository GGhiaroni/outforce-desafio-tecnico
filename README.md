# Coupon API — Desafio Técnico Outforce

[![Java](https://img.shields.io/badge/Java-17-007396?logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-Build-C71A36?logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![Architecture](https://img.shields.io/badge/Architecture-Clean-blue)]()

API REST para gerenciamento de cupons de desconto, desenvolvida como desafio técnico para a **Outforce**.

## Sobre o projeto

API que expõe endpoints para cadastro e remoção de cupons, seguindo regras de negócio específicas (formato do código, valor mínimo de desconto, controle de expiração e soft delete). Construída com foco em testabilidade, isolamento de domínio e práticas profissionais de versionamento.

## Decisão de projeto

O projeto adota **Clean Architecture** (também chamada de Hexagonal/Ports & Adapters), com separação rígida entre:

- **core:** apenas o domínio, regras de negócio encapsuladas em objetos de domínio, use cases e gateways.
- **infra:** — presentation, gateways de saída, etc.
- **config:** — beans e demais configurações.

**Observação:** Decidi por Clean Architecture porque as regras de negócio listadas no enunciado (validação do código, valor mínimo de desconto, data de expiração, soft delete) vive dentro do objeto **Coupon** e não no controller, service ou entidade JPA. 
Futuramente, se eu trocar H2 por Postgres, por exemplo, o domínio se manteria intocável.

## Stack

- Java 17
- Spring Boot 3.5 (Web, Data JPA, Validation)
- H2 Database in-memory
- Lombok
- JUnit 5 + Mockito
- JaCoCo (cobertura ≥ 80%)
- Springdoc OpenAPI (Swagger UI)
- Docker + Docker Compose

## Como rodar

### Pré-requisitos
- Java 17+
- Maven 3.8+ 

### Localmente
```bash
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8080`.

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **Console H2:** http://localhost:8080/h2-console — JDBC URL: `jdbc:h2:mem:coupondb`, user: `sa`, senha em branco

### Via Docker
```bash
docker compose up --build
```

## Testes

```bash
./mvnw test                  # roda os testes
./mvnw verify                # testes + JaCoCo (relatório em target/site/jacoco)
```

## Roadmap

Desenvoli o desafio me organizando em milestones, cada uma com suas issues:

- **S1** — Domínio e regras de negócio
- **S2** — Use Cases e portas
- **S3** — Persistência JPA
- **S4** — Camada Web (REST)
- **S5** — Swagger / OpenAPI
- **S6** — Testes de integração e cobertura 80%
- **S7** — Docker e Docker Compose
- **S8** — README final e revisão


**Desenvolvido por Gabriel Tiziano**

[Linkedin](https://linkedin.com/in/gabrieltiziano)