package com.rule.dao;

import com.rule.entity.EventMessage;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<EventMessage, String> {


    @Query(value = " select em.* from event_message em limit 1000 ", nativeQuery = true)
    List<EventMessage> queryPrepareSendMessage();

    @Query(value = " UPDATE event_message SET message_status = '已消费' where message_id = :messageId ", nativeQuery = true)
    @Modifying
    int updateMessageToSuccess(String messageId);
}
