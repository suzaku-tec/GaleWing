package com.galewings.service;

import com.galewings.util.OllamaClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

class OllamaServiceTest {
    @Mock
    OllamaClient ollamaClient;
    @InjectMocks
    OllamaService ollamaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTellMe() {
        when(ollamaClient.generate(anyString(), any())).thenReturn("generateResponse");

        String result = ollamaService.tellMe("text");
        Assertions.assertEquals("generateResponse", result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme