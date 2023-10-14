package com.galewings.repository;

import com.galewings.dto.qiita.QiitaItemsDto;
import com.miragesql.miragesql.SqlManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class QiitaRankingRepositoryTest {
    @Mock
    SqlManager sqlManager;
    @InjectMocks
    QiitaRankingRepository qiitaRankingRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInsert() {
        when(sqlManager.executeUpdate(any(), any())).thenReturn(0);
        int result = qiitaRankingRepository.insert(new QiitaItemsDto());
        Assertions.assertEquals(0, result);
    }

    @Test
    void testAllDelete() {
        when(sqlManager.executeUpdate(any())).thenReturn(0);
        int result = qiitaRankingRepository.allDelete();
        Assertions.assertEquals(0, result);
    }
}

