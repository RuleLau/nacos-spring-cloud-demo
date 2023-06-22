package com.rule.dao;

import com.rule.entity.PartOrder;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<PartOrder, Integer> {


    @Query(value = " UPDATE part_order SET order_status = :status where oid = :orderNo ", nativeQuery = true)
    @Modifying
    int updateOrderStatus(@Param("orderNo") Integer orderNo, @Param("status") String status);
}
