package com.galewings.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

class NewsApiConfigTest {
    NewsApiConfig newsApiConfig = new NewsApiConfig();

    @Test
    void testCustomRestClient() {
        RestClient result = newsApiConfig.customRestClient();
        Assertions.assertNotNull(result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme