package com.rule.schedule;

import com.rule.repository.TaskRepository;
import com.rule.thread.TaskTriggerPoolHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class TaskScheduleHelper {

    private volatile boolean scheduleThreadToStop = false;

    @Resource
    private TaskRepository taskRepository;

    @Resource
    private TaskTriggerPoolHelper taskTriggerPoolHelper;

    public static void main(String[] args) {
        System.out.println(60000 - System.currentTimeMillis() % 1000);
    }

//    @PostConstruct
    public void start() {

        // schedule thread
        Thread scheduleThread = new Thread(() -> {

            try {
                //
                TimeUnit.MILLISECONDS.sleep(5000 - System.currentTimeMillis() % 1000);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }

            log.info(">>>>>>>>> init part-task scheduler success.");

            while (!scheduleThreadToStop) {
                log.info(">>>>>>>>> scan unfinished task.");
                // Scan unFinished task
              /*  List<Integer> pendingTaskNos = taskRepository.selectUnFinishedTaskNos();

                pendingTaskNos.forEach(e -> );
                taskTriggerPoolHelper.addTrigger();*/
            }

            log.info(">>>>>>>>>>> part-task, TaskScheduleHelper#scheduleThread stop");
        });
        scheduleThread.setDaemon(true);
        scheduleThread.setName("part-task, TaskScheduleHelper#scheduleThread");
        scheduleThread.start();
    }


}
