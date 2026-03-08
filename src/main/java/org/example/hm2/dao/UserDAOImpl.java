package org.example.hm2.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.hm2.entity.User;
import org.example.hm2.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {
    private static final Logger logger = LogManager.getLogger(UserDAOImpl.class);

    @Override
    public Optional<User> create(User user) {
        Transaction transaction = null;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
                logger.info("User is saved: {}", user.getUsername());
            return Optional.of(user);
        }
        catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error saving user: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> cq = cb.createQuery(User.class);
            Root<User> rootEntry = cq.from(User.class);
            CriteriaQuery<User> all = cq.select(rootEntry);

            Query<User> query = session.createQuery(all);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error getting all users: {}", e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public Optional<User> getById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.get(User.class, id);
            return Optional.ofNullable(user);
        }
        catch (Exception e) {
            logger.error("Error finding user by id {}: {}", id, e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM User WHERE email = :email", User.class);
            query.setParameter("email", email);
            return query.uniqueResultOptional();
        }
        catch (Exception e) {
            logger.error("Error finding user by email {}: {}", email, e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("FROM User WHERE username LIKE :username", User.class);
            query.setParameter("username", username);
            return query.uniqueResultOptional();
        }
        catch (Exception e) {
            logger.error("Error finding users by name {}: {}", username, e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> update(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(user);
            transaction.commit();
            logger.info("User updated successfully: {}", user.getId());
            return Optional.of(user);
        }
        catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error updating user: {}", e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
                logger.info("User deleted successfully by id: {}", id);
            }
            transaction.commit();
            return true;
        }
        catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Error deleting user by id {}: {}", id, e.getMessage());
            return false;
        }
    }
}
