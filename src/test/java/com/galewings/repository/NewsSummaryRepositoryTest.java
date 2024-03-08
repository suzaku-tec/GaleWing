package com.galewings.repository;

import com.galewings.entity.NewsSummary;
import com.miragesql.miragesql.SqlManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class NewsSummaryRepositoryTest {
    @Mock
    SqlManager sqlManager;
    @InjectMocks
    NewsSummaryRepository newsSummaryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInsert() {
        when(sqlManager.executeUpdate(any(), any())).thenReturn(0);

        newsSummaryRepository.insert("uuid");
    }

    @Test
    void testSelectAll() {
        when(sqlManager.getResultList(any(), any())).thenReturn(List.of(new NewsSummary()));

        List<NewsSummary> result = newsSummaryRepository.selectAll();
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void testSelectNoSummary() {
        when(sqlManager.getResultList(any(), any())).thenReturn(List.of(new NewsSummary()));

        List<NewsSummary> result = newsSummaryRepository.selectNoSummary();
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void testSelect() {
        NewsSummary actual = new NewsSummary();
        when(sqlManager.getSingleResult(any(), any(), any())).thenReturn(actual);

        NewsSummary result = newsSummaryRepository.select("uuid");
        Assertions.assertEquals(actual, result);
    }

    @Test
    void testUpdate() {
        when(sqlManager.executeUpdate(any(), any())).thenReturn(0);

        int result = newsSummaryRepository.update("uuid", "summary");
        Assertions.assertEquals(0, result);
    }

    @Test
    void testDelete() {
        when(sqlManager.executeUpdate(any(), any())).thenReturn(0);

        int result = newsSummaryRepository.delete("uuid");
        Assertions.assertEquals(0, result);
    }

    @Test
    void testExists() {
        when(sqlManager.getCount(any(), any())).thenReturn(0);

        boolean result = newsSummaryRepository.exists("uuid");
        Assertions.assertFalse(result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme