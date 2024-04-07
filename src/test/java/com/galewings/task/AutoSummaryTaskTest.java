package com.galewings.task;

import com.galewings.dto.ai.googleai.request.PartsDto;
import com.galewings.dto.ai.googleai.response.*;
import com.galewings.entity.NewsSummary;
import com.galewings.entity.Settings;
import com.galewings.repository.NewsSummaryRepository;
import com.galewings.repository.SettingRepository;
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

    @Mock
    SettingRepository settingRepository;

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
        Settings settings = new Settings();
        settings.setting = "test";

        when(geminiService.tellMe(anyString())).thenReturn(responseDto);
        when(newsSummaryRepository.selectNoSummary()).thenReturn(List.of(new NewsSummary()));
        when(newsSummaryRepository.update(anyString(), anyString())).thenReturn(0);
        when(settingRepository.selectOneFor(anyString())).thenReturn(settings);

        autoSummaryTask.autoSammary();
    }

    @Test
    void testAutoSammaryCandidates() throws InterruptedException, NoSuchFieldException, IllegalAccessException {

        GeminiResponseDto responseDto = new GeminiResponseDto();
        CandidatesDto candidatesDto = new CandidatesDto();
        ContentDto contentDto = new ContentDto();
        PartsDto partsDto = new PartsDto();
        contentDto.setParts(List.of(partsDto));

        PromptFeedbackDto promptFeedbackDto = new PromptFeedbackDto();
        SafetyRatingsDto safetyRatingsDto = new SafetyRatingsDto();
        safetyRatingsDto.setCategory("HARM_CATEGORY_SEXUALLY_EXPLICIT");
        promptFeedbackDto.setSafetyRatings(List.of(safetyRatingsDto));
        responseDto.setPromptFeedback(promptFeedbackDto);

        Settings settings = new Settings();
        settings.setting = "test";

        when(geminiService.tellMe(anyString())).thenReturn(responseDto);
        when(newsSummaryRepository.selectNoSummary()).thenReturn(List.of(new NewsSummary()));
        when(newsSummaryRepository.update(anyString(), anyString())).thenReturn(0);
        when(settingRepository.selectOneFor(anyString())).thenReturn(settings);

        autoSummaryTask.autoSammary();
    }

    @Test
    void testAutoSammarySettingsNul() throws InterruptedException, NoSuchFieldException, IllegalAccessException {

        GeminiResponseDto responseDto = new GeminiResponseDto();
        CandidatesDto candidatesDto = new CandidatesDto();
        ContentDto contentDto = new ContentDto();
        PartsDto partsDto = new PartsDto();
        contentDto.setParts(List.of(partsDto));
        candidatesDto.setContent(contentDto);
        responseDto.setCandidates(List.of(candidatesDto));
        Settings settings = null;

        when(geminiService.tellMe(anyString())).thenReturn(responseDto);
        when(newsSummaryRepository.selectNoSummary()).thenReturn(List.of(new NewsSummary()));
        when(newsSummaryRepository.update(anyString(), anyString())).thenReturn(0);
        when(settingRepository.selectOneFor(anyString())).thenReturn(settings);

        autoSummaryTask.autoSammary();
    }

    @Test
    void testAutoSammarySettingNul() throws InterruptedException, NoSuchFieldException, IllegalAccessException {

        GeminiResponseDto responseDto = new GeminiResponseDto();
        CandidatesDto candidatesDto = new CandidatesDto();
        ContentDto contentDto = new ContentDto();
        PartsDto partsDto = new PartsDto();
        contentDto.setParts(List.of(partsDto));
        candidatesDto.setContent(contentDto);
        responseDto.setCandidates(List.of(candidatesDto));
        Settings settings = new Settings();
        settings.setting = null;

        when(geminiService.tellMe(anyString())).thenReturn(responseDto);
        when(newsSummaryRepository.selectNoSummary()).thenReturn(List.of(new NewsSummary()));
        when(newsSummaryRepository.update(anyString(), anyString())).thenReturn(0);
        when(settingRepository.selectOneFor(anyString())).thenReturn(settings);

        autoSummaryTask.autoSammary();
    }

}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme