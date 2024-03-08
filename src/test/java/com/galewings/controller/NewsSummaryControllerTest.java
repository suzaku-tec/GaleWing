package com.galewings.controller;

import com.galewings.ModelMock;
import com.galewings.entity.NewsSummary;
import com.galewings.repository.NewsSummaryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

class NewsSummaryControllerTest {
    @Mock
    NewsSummaryRepository newsSummaryRepository;
    @InjectMocks
    NewsSummaryController newsSummaryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIndex() {
        when(newsSummaryRepository.selectAll()).thenReturn(List.of(new NewsSummary()));

        String result = newsSummaryController.index(new ModelMock());
        Assertions.assertEquals("newsSummary", result);
    }

    @Test
    void testDelete() {
        when(newsSummaryRepository.delete(anyString())).thenReturn(0);

        newsSummaryController.delete("uuid");
    }

    @Test
    void testAddSummary() {
        when(newsSummaryRepository.exists(anyString())).thenReturn(true);

        newsSummaryController.addSummary(null, "uuid");
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme