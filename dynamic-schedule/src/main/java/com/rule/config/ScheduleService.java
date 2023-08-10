package com.rule.config;

import com.rule.dao.ScheduleConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class ScheduleService implements CommandLineRunner {
    @Resource
    private ScheduleConfigRepository scheduleConfigRepository;

    private AtomicInteger taskIdProducer = new AtomicInteger(0);

    private Map<Integer, ScheduledFuture<?>> executeScheduleFutureMap = new ConcurrentHashMap<>();

    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);


    public void startTask() {

        Integer seconds = Optional.ofNullable(scheduleConfigRepository.findByConfigCode("SYNC_PART_DURATION")).map(e -> e.getSeconds()).orElse(3);

        int taskId = taskIdProducer.incrementAndGet();

        ScheduledFuture<?> scheduledFuture = scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
            log.info("start to execute task, taskId: {}, current date: {}", taskIdProducer.get(), LocalDateTime.now());
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
//            log.info("execute task end, taskId: {}, current date: {}", taskIdProducer.get(), LocalDateTime.now());
        }, 5, seconds, TimeUnit.SECONDS);

        executeScheduleFutureMap.put(taskId, scheduledFuture);

    }

    public void cancelTask() {

        int taskId = taskIdProducer.get();

        ScheduledFuture<?> scheduledFuture = executeScheduleFutureMap.get(taskId);

        boolean cancel = scheduledFuture.cancel(false);

        if (cancel) {
            startTask();
        }
//        executeScheduleFutureMap.remove(taskId);
    }

    @Override
    public void run(String... args) {
        startTask();
    }
}
