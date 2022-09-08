package com.example.applfttest.service;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Service
public class MonitoringEventService {
    private final TaskScheduler scheduler;
    Map<Long, ScheduledFuture<?>> jobsMap = new HashMap<>();

    public MonitoringEventService(TaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void createMonitoringEvent(long id, int sec, Runnable runnable) {
        ScheduledFuture<?> scheduledFuture = scheduler.scheduleAtFixedRate(runnable, Duration.ofSeconds(sec));
        jobsMap.put(id, scheduledFuture);
    }


    public void removeMonitoringEvent(long id) {
        ScheduledFuture<?> scheduledFuture = jobsMap.get(id);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            jobsMap.remove(id);
        }
    }
}
