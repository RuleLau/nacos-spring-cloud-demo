package com.rule.repository;

import com.rule.po.Manager;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerRepository extends CrudRepository<Manager, Integer> {

    @Query(value = " select count(*) from manager m " +
            " where m.task_no=:taskNo and m.process_status = 'PENDING' ", nativeQuery = true)
    int getPendingManagerCount(@Param("taskNo") Integer taskNo);

    @Query(value = " select m.* from manager m " +
            " where m.task_no=:taskNo and m.process_status = 'PENDING' limit 1000 ", nativeQuery = true)
    List<Manager> selectTopManagers(Integer taskNo);

}
