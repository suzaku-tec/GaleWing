package com.galewings.repository;

import com.galewings.dto.output.KeywordPrevDayComparisonDto;
import com.galewings.entity.Feed;
import com.galewings.entity.FeedKeyword;
import com.miragesql.miragesql.SqlManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class FeedKeywordRepositoryTest {

    @InjectMocks
    FeedKeywordRepository feedKeywordRepository;

    @Mock
    SqlManager sqlManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void insertKeyword() {
        when(sqlManager.insertEntity(any())).thenReturn(0);

        int result = feedKeywordRepository.insertKeyword(new FeedKeyword());

        Assertions.assertEquals(0, result);
    }

    @Test
    void isAnalysed() {
        when(sqlManager.getCount(any(), any())).thenReturn(0);

        boolean analysed = feedKeywordRepository.isAnalysed("feed_uuid", "feed_link");

        Assertions.assertFalse(analysed);
    }

    @Test
    void isAnalysed2() {
        when(sqlManager.getCount(any(), any())).thenReturn(1);

        boolean analysed = feedKeywordRepository.isAnalysed("feed_uuid", "feed_link");

        Assertions.assertTrue(analysed);
    }

    @Test
    void selectKeywordPrevDayComparison() {
        when(sqlManager.getResultList(any(), any(), any())).thenReturn(null);

        List<KeywordPrevDayComparisonDto> keywordPrevDayComparisonDtos = feedKeywordRepository.selectKeywordPrevDayComparison("2026/01/01");

        Assertions.assertNull(keywordPrevDayComparisonDtos);
    }

    @Test
    void selectKeywordFeed() {
        when(sqlManager.getResultList(any(), any(), any())).thenReturn(null);

        List<Feed> feedList = feedKeywordRepository.selectKeywordFeed("keyword");

        Assertions.assertNull(feedList);
    }
}