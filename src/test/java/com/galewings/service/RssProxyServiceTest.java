package com.galewings.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RssProxyServiceTest {
    RssProxyService rssProxyService = new RssProxyService();

    @Test
    void testConvertUrlToProxy() {
        String result = rssProxyService.convertUrlToProxy("html");
        Assertions.assertEquals("replaceMeWithExpectedResult", result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme