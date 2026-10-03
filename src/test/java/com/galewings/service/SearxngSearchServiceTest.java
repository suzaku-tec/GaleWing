package com.galewings.service;

import com.galewings.dto.SearxngResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class SearxngSearchServiceTest {
    private static final String BASE_URL = "http://localhost:18080";

    private RestClient.Builder builder;
    private MockRestServiceServer server;
    private SearxngSearchService service;

    @BeforeEach
    void setUp() {
        builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build();
        service = new SearxngSearchService(builder);
    }

    @Test
    void search_は期待どおりのリクエストを送り_レスポンスを返す() {
        String responseBody = """
                {
                  "query": "spring boot",
                  "results": [
                    {
                      "title": "Spring Boot",
                      "url": "https://spring.io/projects/spring-boot"
                    }
                  ]
                }
                """;

        server.expect(requestTo(BASE_URL + "/search?q=spring%20boot&format=json&language=ja&safesearch=1&categories=general"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(queryParam("q", "spring%20boot"))
                .andExpect(queryParam("format", "json"))
                .andExpect(queryParam("language", "ja"))
                .andExpect(queryParam("safesearch", "1"))
                .andExpect(queryParam("categories", "general"))
                .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));

        SearxngResponse response = service.search("spring boot");

        assertThat(response).isNotNull();
        assertThat(response.results).hasSize(1);
        assertThat(response.results.get(0).title).isEqualTo("Spring Boot");

        server.verify();
    }

    @Test
    void search_は500のとき例外を投げる() {
        server.expect(requestTo(BASE_URL + "/search?q=error&format=json&language=ja&safesearch=1&categories=general"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(queryParam("q", "error"))
                .andRespond(withServerError());

        assertThatThrownBy(() -> service.search("error"))
                .isInstanceOf(RestClientException.class);

        server.verify();
    }
}