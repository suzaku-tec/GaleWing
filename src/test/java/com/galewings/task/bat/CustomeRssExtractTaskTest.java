package com.galewings.task.bat;

import com.galewings.service.CustomRssService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

class CustomeRssExtractTaskTest {
    @Mock
    CustomRssService customRssService;
    @InjectMocks
    CustomeRssExtractTask customeRssExtractTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRun() {
        customeRssExtractTask.run();
        verify(customRssService).extractDiff();
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme