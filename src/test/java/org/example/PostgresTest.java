package org.example.webapp;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PostgresTest {
    @Test
    public void testPostgresContainer() {
        try(PostgreSQLContainer postgres = new PostgreSQLContainer<>("postgres")) {
            postgres.start();
            assertNotNull(postgres.getJdbcUrl());
        }
    }
}
