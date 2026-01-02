package com.galewings.task.bat;

import com.galewings.dto.wordNews.response.WordNewsDto;
import com.galewings.service.WordNewsApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.fail;

class WordNewsApiTaskTest {
    @Mock
    WordNewsApiService wordNewsApiService;
    @InjectMocks
    WordNewsApiTask wordNewsApiTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRun() throws IOException {
        when(wordNewsApiService.topNews(anyString())).thenReturn(new WordNewsDto());

        wordNewsApiTask.run();

        verify(wordNewsApiService, times(1)).topNews(anyString());
    }

    @Test
    void testRunException() throws IOException {
        when(wordNewsApiService.topNews(anyString())).thenThrow(new IOException());

        try {
            wordNewsApiTask.run();
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e) {
            // Expected exception
        }
    }

}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme