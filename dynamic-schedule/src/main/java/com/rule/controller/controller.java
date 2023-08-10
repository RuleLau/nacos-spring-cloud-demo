package com.rule.controller;

import com.rule.config.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class controller {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/reset/task")
    public void resetTask() {
        scheduleService.cancelTask();
    }

}
