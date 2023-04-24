package com.rule.repository;

import com.rule.po.Task;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Integer> {

    @Query("select t from Task t where t.taskNo =:taskNo")
    Task selectTaskNo(@Param("taskNo") Integer taskNo);

    @Query("select t.taskNo from Task t where t.taskStatus not in ('FINISHED', 'FAILED') ")
    List<Integer> selectUnFinishedTaskNos();


    @Query(" update Task set successCount = successCount + :successCount, "
            + " pendingCount = pendingCount + :pendingCount, "
            + " failCount = failCount + :failCount,  "
            + " taskStatus = (case when successCount + failCount = totalCount then 'FINISHED' else 'PROCESSING' end), "
            + " endDatetime = (case when taskStatus = 'FINISHED' then current_timestamp else endDatetime end) "
            + " where taskNo = :taskNo")
    @Modifying
    int updateTaskInfo(@Param("taskNo") Integer taskNo, @Param("pendingCount") Integer pendingCount,
                       @Param("successCount") Integer successCount, @Param("failCount") Integer failCount);
}
