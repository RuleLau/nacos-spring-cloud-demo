package com.rule.mq;

import java.util.Date;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DelayTaskSchedule {

    //5s 10s 15s 30s,60s 2min, 3min 4min, 5min,
    private static final int[] delayTimes = new int[]{5, 10, 15, 30, 60, 60 * 2, 60 * 3, 60 * 4, 60 * 5, 60 * 6, 60 * 7, 60 * 8, 60 * 9, 60 * 10, 60 * 20, 60 * 30};

    // 调度任务线程池
    private final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    private final LinkedBlockingDeque<Task> taskQueues;

    public DelayTaskSchedule() {
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(4);
        taskQueues = new LinkedBlockingDeque<>();
        newThreadRegister();
    }

    public void start() throws InterruptedException {
        while (true) {
            Task task = taskQueues.take();
            Runnable r = task.getRunnable();
            int retryTimes = task.getRetryTimes();
            int delay = delayTimes[retryTimes] * 1000;
            System.out.println("获取任务，执行延迟投递，当前时间为：" + new Date() + ", 延迟时间为: " + delay / 1000 + "s");
            scheduledThreadPoolExecutor.schedule(r, delayTimes[retryTimes] * 1000, TimeUnit.MILLISECONDS);
        }
    }

    public void addTask(Task task) {
        taskQueues.add(task);
    }

    public void newThreadRegister() {
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                int finalI = i;
                addTask(new Task(i, () -> System.out.println("执行任务，任务级别为：" + delayTimes[finalI] + "s，当前时间为：" + new Date())));
            }
        }).start();
    }

    public static void main(String[] args) throws Exception{

        DelayTaskSchedule delayTaskSchedule = new DelayTaskSchedule();
        delayTaskSchedule.start();
        System.out.println("启动延迟线程池成功!!");
        Thread.sleep(1000);
    }

    public static class Task {

        private int retryTimes;
        private Runnable runnable;

        public Task(int retryTimes, Runnable runnable) {
            this.retryTimes = retryTimes;
            this.runnable = runnable;
        }

        public int getRetryTimes() {
            return retryTimes;
        }

        public void setRetryTimes(int retryTimes) {
            this.retryTimes = retryTimes;
        }

        public Runnable getRunnable() {
            return runnable;
        }

        public void setRunnable(Runnable runnable) {
            this.runnable = runnable;
        }
    }
}
