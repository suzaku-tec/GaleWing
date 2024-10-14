package com.galewings.task.bat;

import com.galewings.service.WordNewsApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WordNewsApiTask implements Runnable {

    private final WordNewsApiService wordNewsApiService;

    @Autowired
    public WordNewsApiTask(WordNewsApiService wordNewsApiService) {
        this.wordNewsApiService = wordNewsApiService;
    }

    @Override
    public void run() {
        try {
            wordNewsApiService.topNews("jp");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
