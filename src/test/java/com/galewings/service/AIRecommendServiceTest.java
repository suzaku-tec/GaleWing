package com.galewings.service;

import com.galewings.entity.AIRecommendFeed;
import com.galewings.entity.Feed;
import com.galewings.repository.AIRecommendRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AIRecommendServiceTest {

    @Mock
    AIRecommendRepository aiRecommendRepository;

    @InjectMocks
    AIRecommendService aiRecommendService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void selectAIRecommndFeed() {
        when(aiRecommendRepository.selectAIRecommndFeed()).thenReturn(null);

        List<AIRecommendFeed> aiRecommendFeeds = aiRecommendService.selectAIRecommndFeed();

        assertNull(aiRecommendFeeds);
    }

    @Test
    void selectAIRecommendOrigin() {
        when(aiRecommendRepository.selectAIRecommendOrigin("testId")).thenReturn(null);

        List<Feed> feedList = aiRecommendService.selectAIRecommendOrigin("testId");

        assertNull(feedList);
    }

    @Test
    void deleteAIRecommend() {
        when(aiRecommendRepository.deleteAIRecommendOrigin(any())).thenReturn(1);
        when(aiRecommendRepository.deleteAIRecommendFeed(any())).thenReturn(1);

        aiRecommendService.deleteAIRecommend("testId");

        verify(aiRecommendRepository).deleteAIRecommendOrigin(any());
        verify(aiRecommendRepository).deleteAIRecommendFeed(any());
    }
}