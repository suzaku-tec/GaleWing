package com.galewings.task.bat;

import com.galewings.service.MinhonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.fail;

class MinhonTranserateTest {
    @Mock
    MinhonService minhonService;
    @InjectMocks
    MinhonTranserate minhonTranserate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRun() throws IOException {
        when(minhonService.transelate(anyString())).thenReturn("transelateResponse");

        minhonTranserate.run();
        verify(this.minhonService, times(1)).transelate(anyString());
    }

    @Test
    void testRunException() throws IOException {
        when(minhonService.transelate(anyString())).thenThrow(new IOException("IO Exception"));

        try {
            minhonTranserate.run();
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Expected exception
        }
    }

}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme