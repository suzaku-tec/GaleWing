package com.galewings.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

class OllamaConfigTest {
    OllamaConfig ollamaConfig = new OllamaConfig();

    @Test
    void testOllamaWebClient() {
        WebClient result = ollamaConfig.ollamaWebClient();
        Assertions.assertNotNull(result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme