package com.rule.dao;

import com.rule.entity.PartOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<PartOrder, Integer> {
}
