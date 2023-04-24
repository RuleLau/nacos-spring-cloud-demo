package com.rule.service;

import com.rule.event.TaskEvent;
import com.rule.po.Manager;
import com.rule.po.Task;
import com.rule.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

import static com.rule.constant.TaskConstant.MANAGER_PARTITION_SIZE;
import static com.rule.constant.TaskConstant.TASK_ODOMETER_NAME;

@Service
@Slf4j
public class StoreService {

    @Resource
    private TaskOdometerService taskOdometerService;

    @Resource
    private TaskRepository taskRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ApplicationEventPublisher eventPublisher;


    @Transactional(rollbackFor = Exception.class)
    public Integer getTaskNo() {
        return taskOdometerService.getTaskNo(TASK_ODOMETER_NAME);
    }

    /**
     * 单线程 entity manager load 数据 105769 ms
     * 单线程 repository load 数据 107720 ms
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer loadToDbByEm(List<Manager> managerList, Integer taskNo) {

        log.info("start load managers, taskNo: {}", taskNo);

        saveManagersAndTask(managerList, taskNo);

        TaskEvent taskEvent = new TaskEvent(this, taskNo);

        log.info("taskNo: {}, load managers finished.", taskNo);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                eventPublisher.publishEvent(taskEvent);
                ;
            }
        });

        return taskNo;
    }

    /**
     * 单线程 jdbc load 数据 541271 ms
     */
    public void loadToDbByJdbc(List<Manager> managerList) throws Exception{
        Integer taskNo = taskOdometerService.getTaskNo(TASK_ODOMETER_NAME);
        String sql = "INSERT INTO manager (task_no, userid, name, process_status) VALUES (?, ?, ?, ?)";

        Connection connection = dataSource.getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql);
        connection.setAutoCommit(true);
        int index = 0;
        for (Manager manager : managerList) {
            pstmt.setInt(1, taskNo);
            pstmt.setLong(2, manager.getUserid());
            pstmt.setString(3, manager.getName());
            pstmt.setString(4, manager.getProcessStatus());
            pstmt.addBatch();

            if (index % 10000 == 0) {
                pstmt.executeBatch();
                pstmt = connection.prepareStatement(sql);
            }
            index++;
        }
        if (index != managerList.size() - 1) {
            pstmt.executeBatch();
        }
    }


    public void saveManagersAndTask(List<Manager> managerList, Integer taskNo) {
        Task managerTask = newManagerTask(taskNo);
        managerTask.setTotalCount(managerList.size());
        managerTask.setPendingCount(managerList.size());
        taskRepository.save(managerTask);

        for (Manager manager : managerList) {
            manager.setTaskNo(taskNo);
        }

        insertEntities(managerList, MANAGER_PARTITION_SIZE);

//        managerRepository.saveAll(managerList);
//        taskRepository.save(managerTask);
    }

    public <S> void insertEntities(Iterable<S> entities, int size) {
        Iterator<S> iterator = entities.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            entityManager.persist(iterator.next());
            index++;
            if (index % size == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
        entityManager.flush();
        entityManager.clear();
    }

    public <S> void insertEntity(S entities) {
        entityManager.persist(entities);
    }

    public Task newManagerTask(Integer taskNo) {
        Task managerTask = new Task();
        managerTask.setTaskNo(taskNo);
        managerTask.setTaskStatus("PENDING");
        managerTask.setStartDatetime(LocalDateTime.now());
        return managerTask;
    }
}
