package com.galewings.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RssProxyServiceTest {
    RssProxyService rssProxyService = new RssProxyService();

    @Test
    void testConvertUrlToProxy() {
        String result = rssProxyService.convertUrlToProxy("https://sample.test.jpp");
        Assertions.assertEquals("https://localhost:8080/proxy/img?url=https%3A%2F%2Fsample.test.jpp", result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme