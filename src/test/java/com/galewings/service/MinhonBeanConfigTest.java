package com.galewings.service;

import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

class MinhonBeanConfigTest {
    MinhonBeanConfig minhonBeanConfig = new MinhonBeanConfig();

    @Test
    void testOkHttpClient() {
        OkHttpClient result = minhonBeanConfig.okHttpClient();
        Assertions.assertNotNull(result);
    }

    @Test
    void testRestTemplate() {
        RestTemplate result = minhonBeanConfig.restTemplate();
        Assertions.assertNotNull(result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme