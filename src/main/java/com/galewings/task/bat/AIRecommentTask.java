package com.galewings.task.bat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.galewings.service.async.AIRecommendAsyncService;
import org.springframework.stereotype.Component;

@Component
public class AIRecommentTask implements Runnable {

    private final AIRecommendAsyncService aiRecommendAsyncService;

    public AIRecommentTask(final AIRecommendAsyncService aiRecommendAsyncService) {
        this.aiRecommendAsyncService = aiRecommendAsyncService;
    }

    @Override
    public void run() {
        try {
            aiRecommendAsyncService.asyncMethod();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
