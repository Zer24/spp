package org.example.repository;

import java.util.List;
import java.util.Optional;
public interface GenericDao<T, ID> {
    T           save(T entity);
    Optional<T> findById(ID id);
    List<T>     findAll();
    T           update(T entity);
    void        deleteById(ID id);
    boolean     existsById(ID id);
}