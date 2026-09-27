package com.galewings.service;

import com.galewings.dto.output.KeywordPrevDayComparisonDto;
import com.galewings.entity.Feed;
import com.galewings.repository.FeedKeywordRepository;
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

class TrendAnalyseServiceTest {

    @InjectMocks
    TrendAnalyseService trendAnalyseService;

    @Mock
    FeedKeywordRepository feedKeywordRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void searchDate() {
        when(feedKeywordRepository.selectKeywordPrevDayComparison(any())).thenReturn(Collections.emptyList());

        List<KeywordPrevDayComparisonDto> keywordPrevDayComparisonDtos = trendAnalyseService.searchDate("2019-04-20");

        Assertions.assertEquals(0, keywordPrevDayComparisonDtos.size());
    }

    @Test
    void selectKeywordFeed() {
        when(feedKeywordRepository.selectKeywordFeed(any())).thenReturn(Collections.emptyList());

        List<Feed> feedList = trendAnalyseService.selectKeywordFeed("test");

        Assertions.assertEquals(0, feedList.size());
    }
}
