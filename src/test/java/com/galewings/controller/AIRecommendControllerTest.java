package com.galewings.controller;

import com.galewings.ModelMock;
import com.galewings.entity.AIRecommendFeed;
import com.galewings.entity.Feed;
import com.galewings.repository.FeedRepository;
import com.galewings.service.AIRecommendService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Objects;

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
        AIRecommendFeed testData1AiRecommendFeed = new AIRecommendFeed();
        testData1AiRecommendFeed.id = "1";
        testData1AiRecommendFeed.title = "title1";
        AIRecommendFeed testData2AiRecommendFeed = new AIRecommendFeed();
        testData2AiRecommendFeed.id = "2";
        testData2AiRecommendFeed.title = "title2";

        Feed testData1Feed = new Feed();
        Feed testData2Feed = new Feed();

        ModelMock testDataModelMock = new ModelMock();

        when(aiRecommendService.selectAIRecommndFeed()).thenReturn(List.of(
                testData1AiRecommendFeed,
                testData2AiRecommendFeed
        ));

        when(aiRecommendService.selectAIRecommendOrigin("1")).thenReturn(List.of(
                testData1Feed,
                testData2Feed
        ));

        String result = aiRecommendController.index(testDataModelMock);

        Assertions.assertEquals("ai_recommend/index", result);
        Assertions.assertEquals(2, ((List<?>) Objects.requireNonNull(testDataModelMock.getAttribute("recommends"))).size());
    }

    @Test
    void selectFeed() {
        when(aiRecommendService.selectAIRecommndFeed()).thenReturn(List.of(
                new AIRecommendFeed(),
                new AIRecommendFeed()
        ));

        List<AIRecommendFeed> aiRecommendFeeds = aiRecommendController.selectFeed();

        Assertions.assertEquals(2, aiRecommendFeeds.size());
    }

    @Test
    void selectFeedOrigin() {
        when(aiRecommendService.selectAIRecommendOrigin("1")).thenReturn(List.of(
                new Feed(),
                new Feed()
        ));

        List<Feed> feedList = aiRecommendController.selectFeedOrigin("1");

        Assertions.assertEquals(2, feedList.size());
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