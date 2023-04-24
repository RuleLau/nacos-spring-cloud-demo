package com.rule.thread;

import com.rule.po.Task;
import com.rule.repository.ManagerRepository;
import com.rule.repository.TaskRepository;
import com.rule.trigger.TaskTrigger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class TaskTriggerPoolHelper {

    @Resource
    private ManagerRepository managerRepository;

    private ThreadPoolExecutor fastTriggerPool = null;
    private ThreadPoolExecutor slowTriggerPool = null;

    @Resource
    private TaskTrigger taskTrigger;

    @Resource
    private TaskRepository taskRepository;

    @PostConstruct
    public void start() {
        fastTriggerPool = new ThreadPoolExecutor(4, 10,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                r -> new Thread(r, "part-task, admin TaskTriggerPoolHelper-fastTriggerPool-" + r.hashCode()));

        slowTriggerPool = new ThreadPoolExecutor(2, 10,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(2000),
                r -> new Thread(r, "part-task, admin TaskTriggerPoolHelper-slowTriggerPool-" + r.hashCode()));
    }

    /**
     * add trigger
     */

    @Transactional(rollbackFor = Exception.class)
    public void addTrigger(int taskNo) {
        int pendingCount = managerRepository.getPendingManagerCount(taskNo);
        if (pendingCount <= 0) {
            return;
        }

        Task task = taskRepository.selectTaskNo(taskNo);
        if (ObjectUtils.isEmpty(task) || "FINISHED".equals(task.getTaskStatus()) || "FAILED".equals(task.getTaskStatus())) {
            log.warn(">>>>>>>>>>>> trigger finished, and no pending managers with taskNo: {}", taskNo);
            return;
        }

        // choose thread pool
        ThreadPoolExecutor triggerPool_ = fastTriggerPool;
        if (pendingCount > 100) {
            triggerPool_ = slowTriggerPool;
        }
        log.info("taskNo: {} in {} pool", taskNo, triggerPool_.toString());

        // trigger
        triggerPool_.execute(() -> taskTrigger.trigger(task));
    }
}
