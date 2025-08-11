package com.yourcompany.wallet;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers // Habilita o suporte a Testcontainers no JUnit 5
public abstract class AbstractIntegrationTest {

    // A anotação @Container cria e gerencia o ciclo de vida do container Docker.
    // 'static' garante que o container seja iniciado apenas uma vez para toda a classe de teste.
    @Container
    public static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    // Este método intercepta a configuração do Spring e injeta dinamicamente as
    // propriedades do container (URL, usuário, senha) que acabaram de ser iniciados.
    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }
}