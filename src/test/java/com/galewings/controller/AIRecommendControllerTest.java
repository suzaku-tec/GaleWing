package com.galewings.controller;

import com.galewings.ModelMock;
import com.galewings.entity.AIRecommendFeed;
import com.galewings.entity.Feed;
import com.galewings.repository.FeedRepository;
import com.galewings.service.AIRecommendService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AIRecommendControllerTest {

    @Mock
    AIRecommendService aiRecommendService;

    @Mock
    FeedRepository feedRepository;

    @InjectMocks
    AIRecommendController aiRecommendController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void index() {
        AIRecommendFeed testData1_AiRecommendFeed = new AIRecommendFeed();
        testData1_AiRecommendFeed.id = "1";
        testData1_AiRecommendFeed.title = "title1";
        AIRecommendFeed testData2_AiRecommendFeed = new AIRecommendFeed();
        testData2_AiRecommendFeed.id = "2";
        testData2_AiRecommendFeed.title = "title2";

        Feed testData1_Feed = new Feed();
        Feed testData2_Feed = new Feed();

        ModelMock testData_ModelMock = new ModelMock();

        when(aiRecommendService.selectAIRecommndFeed()).thenReturn(List.of(
                testData1_AiRecommendFeed,
                testData2_AiRecommendFeed
        ));

        when(aiRecommendService.selectAIRecommendOrigin("1")).thenReturn(List.of(
                testData1_Feed,
                testData2_Feed
        ));

        String result = aiRecommendController.index(testData_ModelMock);

        assertEquals("ai_recommend/index", result);
        assertEquals(2, ((List<?>) testData_ModelMock.getAttribute("recommends")).size());
    }

    @Test
    void selectFeed() {
        when(aiRecommendService.selectAIRecommndFeed()).thenReturn(List.of(
                new AIRecommendFeed(),
                new AIRecommendFeed()
        ));

        List<AIRecommendFeed> aiRecommendFeeds = aiRecommendController.selectFeed();

        assertEquals(2, aiRecommendFeeds.size());
    }

    @Test
    void selectFeedOrigin() {
        when(aiRecommendService.selectAIRecommendOrigin("1")).thenReturn(List.of(
                new Feed(),
                new Feed()
        ));

        List<Feed> feedList = aiRecommendController.selectFeedOrigin("1");

        assertEquals(2, feedList.size());
    }

    @Test
    void readAiRecommendFeed() {
        when(aiRecommendService.selectAIRecommendOrigin("1")).thenReturn(List.of(
                new Feed(),
                new Feed()
        ));
        when(feedRepository.updateReadFeedNoOpen(any())).thenReturn(1);
        doNothing().when(aiRecommendService).deleteAIRecommend(any());

        aiRecommendController.readAiRecommendFeed("1");

        verify(aiRecommendService, times(1)).selectAIRecommendOrigin("1");
        verify(feedRepository, times(2)).updateReadFeedNoOpen(any());
        verify(aiRecommendService, times(1)).deleteAIRecommend(any());
    }
}