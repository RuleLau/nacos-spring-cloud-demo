package com.rule.config;

import java.util.concurrent.Callable;

public class ScheduleTask<T> {

    private Integer taskId;

    private Callable<T> task;

}
