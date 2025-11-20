package com.galewings.task.bat;

import com.galewings.repository.FeedRepository;
import com.galewings.service.GeminiService;
import com.galewings.util.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ExtractingPreviousDayTopicsTest {

    @InjectMocks
    private ExtractingPreviousDayTopics extractingPreviousDayTopics;

    @Mock
    private FeedRepository feedRepository;

    @Mock
    private GeminiService geminiService;

    @Mock
    private ReportService reportService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void extractsTopicsForPreviousDaySuccessfully() throws IOException {

        when(feedRepository.selectFeedToRange(any(), any())).thenReturn(Collections.emptyList());
        when(geminiService.tellMe(anyString())).thenReturn("test");
        doNothing().when(reportService).report(any(), any());

        extractingPreviousDayTopics.run();

        verify(geminiService).tellMe("前日のニュースについて下記の通りです。話題になっているニュースについて上位50件を抜き出してください" + System.lineSeparator() + "---" + System.lineSeparator());
        verify(reportService).report(contains("_topics.txt"), eq("test"));
    }

}