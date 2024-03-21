package com.land.modular.task;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {

    /*@Async("asyncTaskExecutor")
    @Scheduled(cron = "0/1 * * * * ?")
    public void scheduledTask1() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + "---scheduledTask1 " + System.currentTimeMillis());
    }

    @Async
    @Scheduled(cron = "0/1 * * * * ?")
    public void scheduledTask2() {
        System.out.println(Thread.currentThread().getName() + "---scheduledTask2 " + System.currentTimeMillis());
    }*/
}
