package com.rule.dao;

import com.rule.entity.TransactionStatement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionStatementRepository extends CrudRepository<TransactionStatement, Integer> {

    List<TransactionStatement> findAllByOrderNo(Integer orderNo);
}
