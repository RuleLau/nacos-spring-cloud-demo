package com.rule.dao;

import com.rule.entity.EventMessage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<EventMessage, Integer> {


    @Query(value = " select em.* from event_message em limit 1000 ", nativeQuery = true)
    List<EventMessage> queryPrepareSendMessage();

}
