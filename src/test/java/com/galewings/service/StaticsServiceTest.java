package com.galewings.service;

import com.galewings.dto.statistics.ReadRateDto;
import com.galewings.repository.StaticsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

class StaticsServiceTest {
    @Mock
    StaticsRepository staticsRepository;
    @InjectMocks
    StaticsService staticsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSelectReadRate() {
        ReadRateDto test = new ReadRateDto();
        when(staticsRepository.selectReadRate()).thenReturn(test);

        ReadRateDto result = staticsService.selectReadRate();
        Assertions.assertEquals(test, result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme