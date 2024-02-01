package com.galewings.service;

import com.galewings.dto.util.GwURL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedConstruction;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;

class URLServiceTest {

    @InjectMocks
    URLService uRLService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidateUrl() {
        boolean result = uRLService.validateUrl("url");
        Assertions.assertFalse(result);
    }

    @Test
    void testFixUrl() {
        Optional<String> result = uRLService.fixUrl("domain", "url");
        Assertions.assertEquals(Optional.empty(), result);
    }

    @Test
    void testGetUrlDomain() {
        String result = uRLService.getUrlDomain("url");
        Assertions.assertNull(result);
    }

    @Test
    void testBuildQueryString() {
        String result = uRLService.buildQueryString("baseUrl", Map.of("params", "params"));
        Assertions.assertEquals("baseUrl?params=params", result);
    }

    @Test
    void testBuildQueryStringURI() throws URISyntaxException {
        URI result = uRLService.buildQueryStringURI("baseUrl", Map.of("params", "params"));
        Assertions.assertEquals("baseUrl?params=params", result.toString());
    }

    @Test
    public void test_returns_byte_array() throws IOException {
        // Arrange
        InputStream mockInputStream = mock(InputStream.class);

        try (MockedConstruction<GwURL> gwURLMockedConstruction =
                     mockConstruction(GwURL.class, (gwURL, mockMethod) -> {
                         doReturn(mockInputStream).when(gwURL).getInputStream();
                     })) {
            when(mockInputStream.readAllBytes()).thenReturn(new byte[]{1, 2, 3});

            // Act
            byte[] actual = uRLService.getUrlResourceAllByte("http://example.com/resource");
        }

    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme