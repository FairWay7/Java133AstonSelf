package com.example.user;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PostgresTest {
    @Test
    @Disabled
    public void testPostgresContainer() {
        try(PostgreSQLContainer postgres = new PostgreSQLContainer<>("postgres")) {
            postgres.start();
            assertNotNull(postgres.getJdbcUrl());
        }
    }
}
