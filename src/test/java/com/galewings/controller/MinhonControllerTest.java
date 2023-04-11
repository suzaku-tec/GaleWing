package com.galewings.controller;

import com.galewings.service.MinhonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

class MinhonControllerTest {
    @Mock
    MinhonService service;
    @InjectMocks
    MinhonController minhonController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTranselate() throws IOException {
        when(service.transelate(anyString())).thenReturn("transelateResponse");
        String result = minhonController.transelate("text");
        Assertions.assertEquals("transelateResponse", result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme