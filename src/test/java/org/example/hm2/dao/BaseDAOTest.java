package org.example.hm2.dao;

import jakarta.transaction.Transactional;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@Transactional
public abstract class BaseDAOTest {
    private static SessionFactory sessionFactory;

    @Container
    private final static PostgreSQLContainer<?> postgres =
        new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @BeforeAll
    static void beforeAll() {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate-test.cfg.xml");

        configuration.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", postgres.getUsername());
        configuration.setProperty("hibernate.connection.password", postgres.getPassword());

        sessionFactory = configuration.buildSessionFactory();
    }

    @AfterAll
    static void afterAll() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
