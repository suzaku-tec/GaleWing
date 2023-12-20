package com.galewings.controller;

import com.galewings.ModelMock;
import com.galewings.dto.CirculationDto;
import com.galewings.dto.input.CirculationAdd;
import com.galewings.repository.CategoryRepository;
import com.galewings.repository.CirculationRepository;
import com.galewings.service.GwDateService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class CirculationControllerTest {
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    GwDateService gwDateService;
    @Mock
    CirculationRepository circulationRepository;
    @InjectMocks
    CirculationController circulationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAdd() {
        when(gwDateService.now()).thenReturn(LocalDate.of(2023, Month.DECEMBER, 20));
        when(circulationRepository.insert(any())).thenReturn(0);

        int result = circulationController.add(new CirculationAdd());
        Assertions.assertEquals(0, result);
    }

    @Test
    void testIndex() {
        when(circulationRepository.selectAll()).thenReturn(List.of(new CirculationDto()));

        Model model = new ModelMock();

        String result = circulationController.index(model);
        Assertions.assertEquals("circulation", result);
    }

    @Test
    void testGetCirculationList() {
        CirculationDto dto = new CirculationDto();
        when(circulationRepository.selectAll()).thenReturn(List.of(dto));

        List<CirculationDto> result = circulationController.getCirculationList();
        Assertions.assertEquals(List.of(dto), result);
    }

    @Test
    void testGetStatusList() {
        CirculationController.Status[] result = circulationController.getStatusList();
        Assertions.assertArrayEquals(CirculationController.Status.values(), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme