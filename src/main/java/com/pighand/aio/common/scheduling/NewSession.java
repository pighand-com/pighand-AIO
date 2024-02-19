package com.pighand.aio.common.scheduling;

import com.pighand.aio.service.eCommerce.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NewSession {

    @Autowired
    private SessionService sessionService;

    @Scheduled(cron = "0 1 8 * * ?")
    public void newSession() {
        sessionService.create(1L);
    }
}
