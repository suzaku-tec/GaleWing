package com.galewings.repository;

import com.galewings.dto.CirculationDto;
import com.miragesql.miragesql.SqlManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class CirculationRepositoryTest {
    @Mock
    SqlManager sqlManager;
    @InjectMocks
    CirculationRepository circulationRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSelectForId() {
        when(sqlManager.getResultList(any(), any(), any())).thenReturn(Collections.emptyList());
        List<CirculationDto> result = circulationRepository.selectForId("id");
        Assertions.assertEquals(result.size(), 0);
    }

    @Test
    void testSelectAll() {
        when(sqlManager.getResultList(any(), any())).thenReturn(Collections.emptyList());
        List<CirculationDto> result = circulationRepository.selectAll();
        Assertions.assertEquals(result.size(), 0);
    }

    @Test
    void testInsert() {
        when(sqlManager.executeUpdate(any(), any())).thenReturn(0);
        int result = circulationRepository.insert(new CirculationDto());
        Assertions.assertEquals(0, result);
    }

    @Test
    void testUpdate() {
        when(sqlManager.executeUpdate(any(), any())).thenReturn(0);
        int result = circulationRepository.update(new CirculationDto());
        Assertions.assertEquals(0, result);
    }

    @Test
    void testDelete() {
        when(sqlManager.executeUpdate(any(), any())).thenReturn(0);
        int result = circulationRepository.delete("id");
        Assertions.assertEquals(0, result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme