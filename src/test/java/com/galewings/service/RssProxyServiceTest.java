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

    @Test
    void testConvertUrlToProxyWithMultipleUrls() {
        String input = "Check this out: https://example.com/image1.jpg and this: http://example.org/image2.png";
        String expected = "Check this out: https://localhost:8080/proxy/img?url=https%3A%2F%2Fexample.com%2Fimage1.jpg and this: https://localhost:8080/proxy/img?url=http%3A%2F%2Fexample.org%2Fimage2.png";
        String result = rssProxyService.convertUrlToProxy(input);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testConvertUrlToProxyWithNoUrls() {
        String input = "No URLs here!";
        String result = rssProxyService.convertUrlToProxy(input);
        Assertions.assertEquals(input, result);
    }
}
