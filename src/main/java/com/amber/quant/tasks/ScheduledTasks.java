package com.amber.quant.tasks;

import com.amber.quant.service.IAccountPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    @Autowired
    IAccountPositionService accountPositionService;

    //@Scheduled(fixedRate = 5000)
    public void performTask() {
        accountPositionService.test();
    }
}
