package com.galewings.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Optional;

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
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme