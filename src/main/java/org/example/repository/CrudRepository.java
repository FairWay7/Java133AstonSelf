package org.example.webapp.repository;

import org.example.webapp.model.entity.User;
import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.Optional;

public interface CrudRepository<T, ID> extends Repository<T, ID> {
    Optional<T> save(T entity);
    Optional<T> findById(ID id);
    boolean existsById(ID id);
    Collection<T> findAll();
    Optional<T> update(T user);
    boolean deleteById(ID id);
}
