package com.galewings.controller;

import com.galewings.dto.statistics.ReadRateDto;
import com.galewings.dto.statistics.ranking.RankingDto;
import com.galewings.service.StaticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class StaticsControllerTest {
    @Mock
    StaticsService staticsService;
    @InjectMocks
    StaticsController staticsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIndex() {
        ModelAndView result = staticsController.index();
        assertEquals("statics", result.getViewName());
    }

    @Test
    void testGetReadRate() {
        ReadRateDto test = new ReadRateDto();
        when(staticsService.selectReadRate()).thenReturn(test);

        ReadRateDto result = staticsController.getReadRate();
        assertEquals(test, result);
    }

    @Test
    void testGetWordRank() {
        RankingDto test = new RankingDto();
        when(staticsService.selectWordRank()).thenReturn(test);

        RankingDto result = staticsController.getWordRank();
        assertEquals(test, result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme