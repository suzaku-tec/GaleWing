package com.galewings.repository;

import com.galewings.dto.statistics.ReadRateDto;
import com.galewings.dto.statistics.ranking.RankDto;
import com.miragesql.miragesql.SqlManager;
import com.miragesql.miragesql.SqlResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class StaticsRepositoryTest {
    @Mock
    SqlManager sqlManager;
    @InjectMocks
    StaticsRepository staticsRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSelectReadRate() {
        ReadRateDto test = new ReadRateDto();

        when(sqlManager.getSingleResult(any(), any(SqlResource.class))).thenReturn(test);

        ReadRateDto result = staticsRepository.selectReadRate();
        Assertions.assertEquals(test, result);
    }

    @Test
    void testSelectWordRank() {
        when(sqlManager.getResultList(any(), any(), any())).thenReturn(java.util.Collections.emptyList());

        List<RankDto> result = staticsRepository.selectWordRank("", "");

        assertEquals(0, result.size());
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme