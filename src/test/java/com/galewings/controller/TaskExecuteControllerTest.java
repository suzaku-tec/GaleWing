package com.galewings.controller;

import com.galewings.dto.input.TaskExecuteDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TaskExecuteControllerTest {
    @Mock
    Map<String, Runnable> taskMap;
    @InjectMocks
    TaskExecuteController taskExecuteController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIndex() {
        String result = taskExecuteController.index(null);
        Assertions.assertEquals("task", result);
    }

    @Test
    void testExecute() {
        when(taskMap.get(any())).thenReturn(() -> {
        });
        taskExecuteController.execute(new TaskExecuteDto());
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme