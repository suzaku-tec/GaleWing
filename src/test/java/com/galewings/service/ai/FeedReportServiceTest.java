package com.galewings.service.ai;

import com.galewings.dto.SearxngSearchResult;
import com.galewings.entity.Feed;
import com.galewings.exception.GaleWingsSystemException;
import com.galewings.repository.FeedRepository;
import com.galewings.repository.NewsSummaryRepository;
import com.galewings.service.OllamaService;
import com.galewings.service.SearchService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.fail;

class FeedReportServiceTest {
    @Mock
    FeedRepository feedRepository;
    @Mock
    OllamaService ollamaService;
    @Mock
    SearchService searchService;
    @Mock
    NewsSummaryRepository newsSummaryRepository;
    @InjectMocks
    FeedReportService feedReportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSummary() {
        Feed test = new Feed();
        test.link = "link";

        when(feedRepository.selectFeedFor(anyString())).thenReturn(test);
        when(ollamaService.tellMe(anyString())).thenReturn("```json\n{\n\"keywords\":[\n\"Java\",\n\"関数型インターフェース\"\n]\n}\n```");
        when(newsSummaryRepository.updateSummary(anyString(), anyString())).thenReturn(0);

        feedReportService.summary("link");
        verify(newsSummaryRepository).insertSummary(anyString());
    }

    @Test
    void testInformationGathering() {
        Feed test = new Feed();
        test.link = "link";

        when(feedRepository.selectFeedFor(anyString())).thenReturn(test);
        when(ollamaService.tellMe(anyString())).thenReturn("```json\n{\n\"keywords\":[\n\"Java\",\n\"関数型インターフェース\"\n]\n}\n```");
        when(searchService.search(anyString())).thenReturn(List.of(new SearxngSearchResult()));
        when(newsSummaryRepository.updateInformationGathering(anyString(), anyString())).thenReturn(0);

        feedReportService.informationGathering("link");
        verify(newsSummaryRepository).insertInformationGathering(anyString());
    }

    @Test
    void testInformationGathering_FeedNull() {
        when(feedRepository.selectFeedFor(anyString())).thenReturn(null);
        when(ollamaService.tellMe(anyString())).thenReturn("```json\n{\n\"keywords\":[\n\"Java\",\n\"関数型インターフェース\"\n]\n}\n```");
        when(searchService.search(anyString())).thenReturn(List.of(new SearxngSearchResult()));
        when(newsSummaryRepository.updateInformationGathering(anyString(), anyString())).thenReturn(0);

        try {
            feedReportService.informationGathering("link");
            fail("Expected GaleWingsSystemException to be thrown");
        } catch (GaleWingsSystemException e) {
            Assertions.assertNotNull(e.getMessage());
        }
    }

    void testInformationGathering_NoKeyword() {
        when(feedRepository.selectFeedFor(anyString())).thenReturn(new Feed());
        when(ollamaService.tellMe(anyString())).thenReturn("test");
        when(searchService.search(anyString())).thenReturn(List.of(new SearxngSearchResult()));
        when(newsSummaryRepository.updateInformationGathering(anyString(), anyString())).thenReturn(0);

        try {
            feedReportService.informationGathering("link");
            fail("Expected GaleWingsSystemException to be thrown");
        } catch (GaleWingsSystemException e) {
            Assertions.assertEquals("not analys keyword", e.getMessage());
        }
    }

}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme