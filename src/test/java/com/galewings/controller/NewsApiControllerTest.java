package com.galewings.controller;

import com.galewings.ModelMock;
import com.galewings.dto.newsapi.response.NewsApiResponseDto;
import com.galewings.service.NewsApiService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.mockito.Mockito.when;

class NewsApiControllerTest {
    @Mock
    NewsApiService newsApiService;
    @InjectMocks
    NewsApiController newsApiController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIndex() throws URISyntaxException, IOException, IllegalAccessException {
        when(newsApiService.topHeadlines()).thenReturn(new NewsApiResponseDto());
        ModelMock modelMock = new ModelMock();
        String result = newsApiController.index(modelMock);
        Assertions.assertEquals("newsApi", result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme