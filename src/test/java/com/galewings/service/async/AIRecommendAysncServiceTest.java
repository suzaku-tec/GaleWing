package com.galewings.service.async;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.galewings.entity.Feed;
import com.galewings.repository.AIRecommendRepository;
import com.galewings.repository.FeedRepository;
import com.galewings.util.OllamaClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class AIRecommendAysncServiceTest {

    @Mock
    FeedRepository feedRepository;

    @Mock
    OllamaClient ollamaClient;

    @Mock
    AIRecommendRepository aiRecommendRepository;

    @InjectMocks
    AIRecommendAsyncService aiRecommendAsyncService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAsyncMethod() throws ExecutionException, InterruptedException, JsonProcessingException {
        when(feedRepository.selectPublicDateFrom((LocalDate) any())).thenReturn(List.of(new Feed()));
        when(ollamaClient.generate(any(), any())).thenReturn("""
                ```json
                {
                    "news": [
                        { "title": "test", "origin": ["test1", "test2", "test3"] }
                    ]
                }
                ```
                """);
        when(aiRecommendRepository.insertAIRecommend(any(), any())).thenReturn(1);

        CompletableFuture<Void> result = aiRecommendAsyncService.asyncMethod();
        Assertions.assertNull(result.get());
    }
}
