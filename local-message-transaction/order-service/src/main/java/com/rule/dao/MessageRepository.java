package com.rule.dao;

import com.rule.entity.EventMessage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends CrudRepository<EventMessage, Integer> {
}
