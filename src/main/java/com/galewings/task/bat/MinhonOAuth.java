package com.galewings.task.bat;

import com.galewings.service.MinhonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MinhonOAuth implements Runnable {

    private final MinhonService minhonService;

    @Autowired
    public MinhonOAuth(MinhonService minhonService) {
        this.minhonService = minhonService;
    }

    @Override
    public void run() {
        String str = minhonService.oauth();
        System.out.println("OAuth: " + str);
    }
}
