package com.galewings.repository;

import com.miragesql.miragesql.SqlManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class WordRepositoryTest {
    @Mock
    SqlManager sqlManager;
    @Mock
    JdbcTemplate jdbcTemplate;
    @InjectMocks
    WordRepository wordRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsExist() {
        when(sqlManager.getSingleResult(any(), any(), any())).thenReturn(0);
        boolean result = wordRepository.isExist("word");
        Assertions.assertTrue(result);
    }

    @Test
    void testInsert() {
        when(sqlManager.getSingleResult(any(), any())).thenReturn(0);
        int result = wordRepository.insert("word");
        Assertions.assertEquals(0, result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme