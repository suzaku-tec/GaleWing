package com.galewings.service;

import com.galewings.dto.newsapi.request.OptionalRequestParam;
import com.galewings.dto.newsapi.response.NewsApiResponseDto;
import com.galewings.exception.GaleWingsSystemException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.fail;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class NewsApiServiceTest {

    @InjectMocks
    NewsApiService newsApiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTopHeadlines() throws URISyntaxException, IOException, IllegalAccessException {
        String apiKey = "test";

        var restClientBuilder = RestClient.builder();
        MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
        mockServer.expect(requestTo("/top-headlines?country=us&apiKey=" + apiKey))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess().body("{\"status\":\"ok\",\"totalResults\":4145,\"articles\":[{\"source\":{\"id\":null,\"name\":\"ETFDailyNews\"},\"author\":\"MarketBeatNews\",\"title\":\"test\",\"description\":\"test description\",\"url\":\"https://localhost\",\"urlToImage\": null,\"publishedAt\":\"2024-09-17T08:44:33Z\",\"content\":\"test\"}]}"));

        RestClient restClient = restClientBuilder.build();
        newsApiService = new NewsApiService(restClient);
        ReflectionTestUtils.setField(newsApiService, "apiKey", apiKey);
        NewsApiResponseDto result = newsApiService.topHeadlines();
        Assertions.assertNotNull(result);
        Assertions.assertEquals("ok", result.getStatus());
        Assertions.assertEquals(4145L, result.getTotalResults());
    }

    @Test
    void testTopHeadlines2() throws IOException {
        String apiKey = "test";

        var restClientBuilder = RestClient.builder();
        MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
        mockServer.expect(requestTo("/top-headlines?country=jp&apiKey=" + apiKey + "&category=test"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess().body("{\"status\":\"ok\",\"totalResults\":4145,\"articles\":[{\"source\":{\"id\":null,\"name\":\"ETFDailyNews\"},\"author\":\"MarketBeatNews\",\"title\":\"test\",\"description\":\"test description\",\"url\":\"https://localhost\",\"urlToImage\": null,\"publishedAt\":\"2024-09-17T08:44:33Z\",\"content\":\"test\"}]}"));

        RestClient restClient = restClientBuilder.build();
        newsApiService = new NewsApiService(restClient);
        ReflectionTestUtils.setField(newsApiService, "apiKey", apiKey);
        OptionalRequestParam param = new OptionalRequestParam();
        param.country = "jp";
        param.category = "test";
        NewsApiResponseDto result = newsApiService.topHeadlines(param);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("ok", result.getStatus());
        Assertions.assertEquals(4145L, result.getTotalResults());

    }

    @Test
    void testNoApikey() throws IOException {
        String apiKey = null;

        var restClientBuilder = RestClient.builder();
        MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restClientBuilder).build();
        mockServer.expect(requestTo("/top-headlines?country=jp&apiKey=" + apiKey + "&category=test"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess().body("{\"status\":\"ok\",\"totalResults\":4145,\"articles\":[{\"source\":{\"id\":null,\"name\":\"ETFDailyNews\"},\"author\":\"MarketBeatNews\",\"title\":\"test\",\"description\":\"test description\",\"url\":\"https://localhost\",\"urlToImage\": null,\"publishedAt\":\"2024-09-17T08:44:33Z\",\"content\":\"test\"}]}"));

        RestClient restClient = restClientBuilder.build();
        newsApiService = new NewsApiService(restClient);
        ReflectionTestUtils.setField(newsApiService, "apiKey", apiKey);
        OptionalRequestParam param = new OptionalRequestParam();
        param.country = "jp";
        param.category = "test";

        try {
            NewsApiResponseDto result = newsApiService.topHeadlines(param);
            fail("Should throw GaleWingsSystemException");
        } catch (GaleWingsSystemException e) {
            Assertions.assertEquals("newsapi.api-key not found. add .env file", e.getMessage());
        }
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme