package com.galewings.repository;

import com.miragesql.miragesql.SqlManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class FeedGroupingRepositoryTest {

    @Mock
    SqlManager sqlManager;
    @InjectMocks
    FeedGroupingRepository feedGroupingRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInsert() {
        when(sqlManager.executeUpdate(any(), any())).thenReturn(0);
        feedGroupingRepository.insert("", "", 0f);
    }

    @Test
    void testAllDelete() {
        when(sqlManager.executeUpdate(any())).thenReturn(0);
        feedGroupingRepository.allDelete();
    }
}