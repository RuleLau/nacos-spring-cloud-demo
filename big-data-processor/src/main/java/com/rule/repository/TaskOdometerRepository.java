package com.rule.repository;

import com.rule.po.Odometer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskOdometerRepository extends CrudRepository<Odometer, Integer> {

    @Query("update Odometer set id = id + 1 where name = :name ")
    @Modifying
    int updateTaskNo(String name);

    @Query("select id from Odometer where name = :name ")
    int selectTaskNo(String name);
}
