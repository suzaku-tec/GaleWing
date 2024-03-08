package com.galewings.task;

import com.galewings.dto.ai.googleai.request.PartsDto;
import com.galewings.dto.ai.googleai.response.CandidatesDto;
import com.galewings.dto.ai.googleai.response.ContentDto;
import com.galewings.dto.ai.googleai.response.GeminiResponseDto;
import com.galewings.entity.NewsSummary;
import com.galewings.repository.NewsSummaryRepository;
import com.galewings.service.GeminiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

class AutoSummaryTaskTest {
    @Mock
    GeminiService geminiService;
    @Mock
    NewsSummaryRepository newsSummaryRepository;
    @InjectMocks
    AutoSummaryTask autoSummaryTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAutoSammary() throws InterruptedException, NoSuchFieldException, IllegalAccessException {

        GeminiResponseDto responseDto = new GeminiResponseDto();
        CandidatesDto candidatesDto = new CandidatesDto();
        ContentDto contentDto = new ContentDto();
        PartsDto partsDto = new PartsDto();
        contentDto.setParts(List.of(partsDto));
        candidatesDto.setContent(contentDto);
        responseDto.setCandidates(List.of(candidatesDto));

        when(geminiService.tellMe(anyString())).thenReturn(responseDto);
        when(newsSummaryRepository.selectNoSummary()).thenReturn(List.of(new NewsSummary()));
        when(newsSummaryRepository.update(anyString(), anyString())).thenReturn(0);

        autoSummaryTask.autoSammary();
    }

}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme