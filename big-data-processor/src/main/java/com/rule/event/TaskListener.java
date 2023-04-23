package com.rule.event;

import com.rule.thread.TaskTriggerPoolHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class TaskListener implements ApplicationListener<TaskEvent> {

    @Autowired
    private TaskTriggerPoolHelper taskTriggerPoolHelper;

    @Override
    public void onApplicationEvent(TaskEvent event) {
        Integer taskNo = event.getTaskNo();
        taskTriggerPoolHelper.addTrigger(taskNo);
    }
}
