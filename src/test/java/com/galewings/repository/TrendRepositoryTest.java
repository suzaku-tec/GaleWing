package com.galewings.repository;

import com.miragesql.miragesql.SqlManager;
import com.miragesql.miragesql.SqlResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class TrendRepositoryTest {
    @Mock
    SqlManager sqlManager;
    @InjectMocks
    TrendRepository trendRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInsert() {
        when(sqlManager.executeUpdate(any(SqlResource.class), any(Object.class))).thenReturn(0);

        int result = trendRepository.insert("date", "word", Long.valueOf(1));
        Assertions.assertEquals(0, result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme