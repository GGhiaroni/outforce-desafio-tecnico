# Coupon API — Desafio Técnico Outforce

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)
![H2](https://img.shields.io/badge/H2-Database-0000bb?style=for-the-badge&logo=databricks&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-pink?style=for-the-badge&logo=java&logoColor=white)
![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![Mockito](https://img.shields.io/badge/Mockito-78A641?style=for-the-badge&logo=java&logoColor=white)
![JaCoCo](https://img.shields.io/badge/JaCoCo-brightgreen?style=for-the-badge)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

API REST para gerenciamento de cupons de desconto, desenvolvida como desafio técnico para a **Outforce**.

## Sobre o projeto

API que expõe endpoints para cadastro e remoção de cupons, seguindo regras de negócio específicas (formato do código, valor mínimo de desconto, controle de expiração e soft delete). Construída com foco em testabilidade, isolamento de domínio e práticas profissionais de versionamento.

## Decisão de projeto

Optei por **Clean Architecture**, separando:

- **core:** apenas o domínio, regras de negócio encapsuladas em objetos de domínio, use cases e gateways.
- **infra:** — presentation, gateways de saída, etc.
- **config:** — beans e demais configurações.

**Observação:** Decidi por Clean Architecture porque as regras de negócio listadas no enunciado (validação do código, valor mínimo de desconto, data de expiração, soft delete) vivem dentro do objeto **Coupon** e não no controller, service ou entidade JPA. 
Futuramente, se eu trocar H2 por Postgres, por exemplo, o domínio se manteria intocável.

## Stack

- Java 17
- Spring Boot 3.5 (Web, Data JPA, Validation)
- H2 Database in-memory
- Lombok
- JUnit 5 e Mockito
- JaCoCo
- Springdoc OpenAPI (Swagger UI)
- Docker + Docker Compose

## Rodar com Docker

```bash
docker compose up --build
```

API disponível em: http://localhost:8080  
Swagger UI: http://localhost:8080/swagger-ui.html

## Localmente
```bash
./mvnw spring-boot:run
```

## Rodar os testes
```bash
./mvnw test
```

## Relatório de cobertura com JaCoCo
```bash
./mvnw clean verify
# Relatório gerado em: target/site/jacoco/index.html
```

## Endpoints

| Método | Rota | Status | Descrição |
|--------|------|--------|-----------|
| `POST` | `/coupon` | 201 / 400 | Cria um novo cupom |
| `DELETE` | `/coupon/{id}` | 204 / 404 / 409 | Soft delete de um cupom |

**Desenvolvido por Gabriel Tiziano**

[Linkedin](https://linkedin.com/in/gabrieltiziano)