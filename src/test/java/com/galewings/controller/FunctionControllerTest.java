package com.galewings.controller;

import com.galewings.ModelMock;
import com.galewings.entity.FunctionCtrl;
import com.galewings.repository.FunctionCtrlRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.*;

class FunctionControllerTest {
    @Mock
    FunctionCtrlRepository functionCtrlRepository;
    @InjectMocks
    FunctionController functionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIndex() {
        List<FunctionCtrl> resultList = List.of(new FunctionCtrl());
        when(functionCtrlRepository.getAll()).thenReturn(resultList);

        String result = functionController.index(new ModelMock());
        Assertions.assertEquals("functionCtrl", result);
    }

    @Test
    void testUpdate() {
        doNothing().when(functionCtrlRepository).update(anyString(), anyString());
        functionController.update(new FunctionCtrl());
        verify(functionCtrlRepository).update(null, null);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme