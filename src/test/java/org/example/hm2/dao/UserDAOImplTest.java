package org.example.hm2.dao;

import org.example.hm2.config.TestHibernateConfig;
import org.example.hm2.entity.User;
import org.example.hm2.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class UserDAOImplTest {
    private UserDAO userDAO;
    private static SessionFactory sessionFactory;

    @Container
    private static final PostgreSQLContainer<?> postgres =
        new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @BeforeAll
    static void beforeAll() {
        sessionFactory = TestHibernateConfig.createSessionFactory(postgres);
        HibernateUtil.setSessionFactory(sessionFactory);
    }

    @AfterAll
    static void afterAll() {
//        HibernateUtil.shutdown();
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @BeforeEach
    void setUp() {
        userDAO = new UserDAOImpl();
//        session = sessionFactory.openSession();
        try (Session session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            transaction.commit();
        }
    }

//    @AfterEach
//    void tearDown() {
//        if (session.getTransaction() != null && session.getTransaction().isActive()) {
//            session.getTransaction().rollback();
//        }
//        session.close();
//    }

    @Test
    public void createSimpleUser() {
        User user = new User("IvaNov", "ivanov@g.ru", 30);
        User savedUser = userDAO.create(user).get();

        assertNotNull(savedUser.getId());
        assertEquals("IvaNov", savedUser.getUsername());
        assertEquals("ivanov@g.ru", savedUser.getEmail());
        assertEquals(30, savedUser.getAge());
        assertNotNull(savedUser.getCreated_at());
    }

    @Test
    public void findByIdExistingUser() {
        User user = new User("IvaNov", "ivanov@g.ru", 30);
        User savedUser = userDAO.create(user).get();

        Optional<User> foundUser = userDAO.getById(savedUser.getId());
        assertNotNull(foundUser.get().getId());
    }

    @Test
    public void findByIdNonExistingUser() {
        Optional<User> foundUser = userDAO.getById(1L);
        assertFalse(foundUser.isPresent());
    }

    @Test
    public void findByEmailExistingUser() {
        User user = new User("IvaNova", "ivanova@g.ru", 30);
        User savedUser = userDAO.create(user).get();

        Optional<User> foundUser = userDAO.getByEmail("ivanova@g.ru");

        assertTrue(foundUser.isPresent());
        assertEquals("IvaNova", foundUser.get().getUsername());
        assertEquals("ivanova@g.ru", foundUser.get().getEmail());
    }

    @Test
    public void findByUsername() {
        User user = new User("IvaN", "IvaN@g.ru", 30);
        userDAO.create(user).get();

        Optional<User> foundUser = userDAO.getByUsername("IvaN");

        assertTrue(foundUser.isPresent());
        assertEquals("IvaN", foundUser.get().getUsername());
        assertEquals("IvaN@g.ru", foundUser.get().getEmail());
    }

    @Test
    void findAll() {
        userDAO.create(new User("user1", "user1@example.com", 20));
        userDAO.create(new User("user1", "user2@example.com", 25));
        userDAO.create(new User("user1", "user3@example.com", 30));

        List<User> users = userDAO.findAll();

        assertEquals(3, users.size());
    }

    @Test
    void update() {
        User user = new User("original", "user1@example.com", 40);
        User savedUser = userDAO.create(user).get();

        savedUser.setUsername("updated");
        savedUser.setAge(41);
        userDAO.update(savedUser).get();

        Optional<User> foundUser = userDAO.getById(savedUser.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("updated", foundUser.get().getUsername());
        assertEquals(41, foundUser.get().getAge());
    }

    @Test
    void delete() {
        User user = new User("user1", "user1@example.com", 20);
        User savedUser = userDAO.create(user).get();

        userDAO.deleteById(savedUser.getId());

        Optional<User> foundUser = userDAO.getById(savedUser.getId());
        assertFalse(foundUser.isPresent());
    }

    @Test
    void deleteAll() {
        User user1 = new User("user1", "user1@example.com", 20);
        User user2 = new User("user2", "user2@example.com", 20);

        User savedUser1 = userDAO.create(user1).get();
        User savedUser2 = userDAO.create(user2).get();
        assertEquals(2, userDAO.findAll().size());

        userDAO.deleteById(savedUser1.getId());
        userDAO.deleteById(savedUser2.getId());

        assertEquals(0, userDAO.findAll().size());
    }
}
