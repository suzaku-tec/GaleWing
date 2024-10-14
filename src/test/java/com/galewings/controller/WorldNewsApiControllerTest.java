package com.galewings.controller;

import com.galewings.ModelMock;
import com.galewings.dto.wordNews.response.WordNewsDto;
import com.galewings.service.WordNewsApiService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

class WorldNewsApiControllerTest {
    @Mock
    WordNewsApiService wordNewsApiService;
    @InjectMocks
    WorldNewsApiController worldNewsApiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIndex() throws IOException {
        when(wordNewsApiService.topNews(anyString())).thenReturn(new WordNewsDto());

        String result = worldNewsApiController.index(new ModelMock());
        Assertions.assertEquals("worldNews", result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme