package com.galewings.controller;

import com.galewings.dto.input.KeywordLinkDto;
import com.galewings.dto.input.KeywordSummaryDto;
import com.galewings.service.GwDateService;
import com.galewings.service.OllamaChatService;
import com.galewings.util.ReportService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReportControllerTest {

    @Mock
    ReportService reportService;

    @Mock
    GwDateService gwDateService;

    @Mock
    OllamaChatService ollamaChatService;

    @Mock
    Executor taskExecutor;

    @InjectMocks
    private ReportController reportController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getReportsList() throws IOException {
        when(reportService.getReportList()).thenReturn(Collections.emptyList());

        List<String> result = reportController.getReportsList();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void summaryReport() throws IOException {
        Executor directExecutor = Runnable::run;
        ReportController controller = new ReportController(reportService, new GwDateService(), ollamaChatService, directExecutor);
        KeywordSummaryDto keywordSummaryDto = new KeywordSummaryDto();
        KeywordLinkDto keywordLinkDto = new KeywordLinkDto();
        keywordLinkDto.link = "testLink";
        keywordLinkDto.title = "testTitle";
        keywordSummaryDto.linkList = List.of(keywordLinkDto);
        when(ollamaChatService.chat(any())).thenReturn("testChat");
        doNothing().when(reportService).report(any(), any());

        controller.summaryReport(keywordSummaryDto);

        verify(reportService, times(1)).report(any(), any());
    }

    @Test
    void summaryReportException() throws IOException {
        Executor directExecutor = Runnable::run;
        ReportController controller = new ReportController(reportService, gwDateService, ollamaChatService, directExecutor);
        KeywordSummaryDto keywordSummaryDto = new KeywordSummaryDto();
        KeywordLinkDto keywordLinkDto = new KeywordLinkDto();
        keywordLinkDto.link = "testLink";
        keywordLinkDto.title = "testTitle";
        keywordSummaryDto.linkList = List.of(keywordLinkDto);
        when(ollamaChatService.chat(any())).thenReturn("testChat");
        doThrow(new IOException("test exception")).when(reportService).report(any(), any());

        try {
            controller.summaryReport(keywordSummaryDto);
            Assertions.fail();
        } catch (RuntimeException e) {
        }

    }

}