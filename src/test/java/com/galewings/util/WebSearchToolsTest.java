package com.galewings.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class WebSearchToolsTest {

    @Mock
    WebSearchTools webSearchTools;

    @InjectMocks
    WebSearchTools webSearchToolsTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void webSearch() {
        when(webSearchTools.webSearch(any())).thenReturn(null);

        var result = webSearchTools.webSearch("test");

        Assertions.assertNull(result);
    }
}