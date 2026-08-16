package com.galewings.repository;

import com.miragesql.miragesql.SqlManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;

class FeedClassificationRepositoryTest {
    @Mock
    SqlManager sqlManager;
    @InjectMocks
    FeedClassificationRepository feedClassificationRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void existsClassification() {
        when(sqlManager.getSingleResult(any(), any(), anyMap())).thenReturn(1);

        assertTrue(feedClassificationRepository.existsClassification("testFeedLink"));
    }

    @Test
    void existsClassification_0() {
        when(sqlManager.getSingleResult(any(), any(), anyMap())).thenReturn(0);

        assertFalse(feedClassificationRepository.existsClassification("testFeedLink"));
    }

    @Test
    void existsClassification_null() {
        when(sqlManager.getSingleResult(any(), any(), anyMap())).thenReturn(null);

        assertFalse(feedClassificationRepository.existsClassification("testFeedLink"));
    }

    @Test
    void getClassification() {
        when(sqlManager.getSingleResult(any(), any(), anyMap())).thenReturn(null);
        assertNull(feedClassificationRepository.getClassification("testFeedLink"));
    }

    @Test
    void mergeClassification() {
        when(sqlManager.executeUpdate(any(), any())).thenReturn(1);
        assertEquals(1, feedClassificationRepository.mergeClassification(null));
    }
}