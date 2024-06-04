package com.galewings.task.bat;

import com.galewings.service.MinhonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MinhonTranserate implements Runnable {

    private final MinhonService minhonService;

    @Autowired
    public MinhonTranserate(MinhonService minhonService) {
        this.minhonService = minhonService;
    }

    @Override
    public void run() {
        try {
            String str = minhonService.transelate("test");
            System.out.println("transelate: " + str);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
