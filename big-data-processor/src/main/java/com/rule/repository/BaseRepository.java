package com.rule.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class BaseRepository<T, ID> {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public boolean save(T entity) {
        entityManager.persist(entity);
        return true;
    }

    @Transactional
    public T findById(T o, ID id) {
        return (T) entityManager.find(o.getClass(), id);
    }

    @Transactional
    public boolean update(T entity) {
        entityManager.merge(entity);
        return true;
    }
}
