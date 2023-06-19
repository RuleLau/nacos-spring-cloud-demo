package com.rule.dao;

import com.rule.entity.UserBalance;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface UserBalanceRepository extends CrudRepository<UserBalance, Integer> {

    @Query(value = " UPDATE user_balance set balance = balance - :total  where  entry_id = :userId and balance - :total >= 0 ", nativeQuery = true)
    @Modifying
    int deduceUserBalance(@Param("userId") Integer userId, @Param("total") BigDecimal total);

}
