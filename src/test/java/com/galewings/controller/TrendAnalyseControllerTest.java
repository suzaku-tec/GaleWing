package com.galewings.controller;

import com.galewings.ModelMock;
import com.galewings.entity.Feed;
import com.galewings.service.TrendAnalyseService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TrendAnalyseControllerTest {

    @InjectMocks
    TrendAnalyseController trendAnalyseController;

    @Mock
    TrendAnalyseService trendAnalyseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void index() {
        String result = trendAnalyseController.index();

        Assertions.assertEquals("trend", result);
    }

    @Test
    void searchDate() {
        when(trendAnalyseService.searchDate(any())).thenReturn(Collections.emptyList());
        ModelMock modelMock = new ModelMock();

        String result = trendAnalyseController.searchDate("2026/01/01", modelMock);

        Assertions.assertEquals("trend", result);
        Assertions.assertNotNull(modelMock.asMap().get("keywordPrevDayComparisonList"));
        Assertions.assertNotNull(modelMock.asMap().get("targetDate"));
    }

    @Test
    void getKeywordFeedList() {
        when(trendAnalyseService.selectKeywordFeed(any())).thenReturn(Collections.emptyList());

        List<Feed> keywordFeedList = trendAnalyseController.getKeywordFeedList("");

        Assertions.assertEquals(0, keywordFeedList.size());
    }
}