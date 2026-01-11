package com.galewings.controller;

import com.galewings.dto.input.FeedReportDto;
import com.galewings.service.ai.FeedReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;

class FeedReportControllerTest {
    @Mock
    FeedReportService feedReportService;
    @InjectMocks
    FeedReportController feedReportController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSummary() {
        FeedReportDto testFeedReportDto = new FeedReportDto();
        testFeedReportDto.link = "http://example.com";

        feedReportController.summary(testFeedReportDto);
        verify(feedReportService).summary(anyString());
    }

    @Test
    void testInformationGathering() {
        FeedReportDto testFeedReportDto = new FeedReportDto();
        testFeedReportDto.link = "http://example.com";

        feedReportController.informationGathering(testFeedReportDto);
        verify(feedReportService).informationGathering(anyString());
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme