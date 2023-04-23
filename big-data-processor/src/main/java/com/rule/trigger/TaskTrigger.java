package com.rule.trigger;

import com.rule.po.Manager;
import com.rule.po.Task;
import com.rule.repository.ManagerRepository;
import com.rule.repository.TaskRepository;
import com.rule.thread.TaskTriggerPoolHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Component
@Slf4j
public class TaskTrigger {

    @Resource
    private ManagerRepository managerRepository;

    @Resource
    private TaskRepository taskRepository;

    @Resource
    private TaskTriggerPoolHelper taskTriggerPoolHelper;

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void trigger(Task task, List<Manager> pendingManagers) {
        Integer taskNo = task.getTaskNo();

        log.info(">>>>>>>>>>> process trigger start, taskNo:{}, pending manager size: {}", taskNo, pendingManagers.size());

        int successCount = 0;
        int failCount = 0;

        // 1.  update manager status
        for (Manager pendingManager : pendingManagers) {
            pendingManager.setProcessStatus("SUCCESS");
            try {
                managerRepository.save(pendingManager);
                successCount++;
            } catch (Exception e) {
                log.error("update manager failed, and error: {}", e);
                failCount++;
            }
        }

        // 2. update task info
        taskRepository.updateTaskInfo(task.getTaskNo(), -pendingManagers.size(), successCount, failCount);

        // 3、> 1000 task put into worker or block queue
        taskTriggerPoolHelper.addTrigger(taskNo);

        log.debug(">>>>>>>>>>> process trigger end, taskNo:{}", taskNo);
    }

}
