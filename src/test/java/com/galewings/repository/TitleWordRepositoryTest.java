package com.galewings.repository;

import com.miragesql.miragesql.SqlManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class TitleWordRepositoryTest {
    @Mock
    SqlManager sqlManager;
    @InjectMocks
    TitleWordRepository titleWordRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsExist() {
        when(sqlManager.getSingleResult(any(), any(), any())).thenReturn(1);
        boolean result = titleWordRepository.isExist("link");
        Assertions.assertTrue(result);
    }

    @Test
    void testInsert() {
        when(sqlManager.executeUpdate(any(), any())).thenReturn(0);
        int result = titleWordRepository.insert("link", "word");
        Assertions.assertEquals(0, result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme