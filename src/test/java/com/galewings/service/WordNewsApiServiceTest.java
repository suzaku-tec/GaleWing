package com.galewings.service;

import com.galewings.dto.wordNews.response.WordNewsDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

class WordNewsApiServiceTest {

    @Mock
    private GwDateService gwDateService;

    @InjectMocks
    private WordNewsApiService wordNewsApiService;

    private final String mockTopNewsStr = "{\n" +
            "    \"top_news\": [\n" +
            "        {\n" +
            "            \"news\": [\n" +
            "                {\n" +
            "                    \"id\": 224767206,\n" +
            "                    \"title\": \"Jury to Begin Deliberations In Trump Trial\",\n" +
            "                    \"text\": \"test\",\n" +
            "                    \"summary\": \"test\",\n" +
            "                    \"url\": \"https://politicalwire.com/2024/05/28/jury-to-begin-deliberations-in-trump-trial/\",\n" +
            "                    \"image\": \"https://politicalwire.com/wp-content/uploads/2018/02/PW-podcast-logo.jpg\",\n" +
            "                    \"publish_date\": \"2024-05-29 00:10:48\",\n" +
            "                    \"author\": \"Taegan Goddard\",\n" +
            "                    \"authors\": [\n" +
            "                        \"Taegan Goddard\"\n" +
            "                    ]\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 224839780,\n" +
            "                    \"title\": \"Jury in Donald Trump's hush money case to begin deliberations\",\n" +
            "                    \"text\": \"test\",\n" +
            "                    \"summary\": \"test\",\n" +
            "                    \"url\": \"https://apnews.com/article/trump-trial-deliberations-jury-judge-verdict-390f045e9e8a37f069e82576edd7a842\",\n" +
            "                    \"image\": \"https://dims.apnews.com/dims4/default/3446536/2147483647/strip/true/crop/6195x3485+0+323/resize/1440x810!/quality/90/?url=https%3A%2F%2Fassets.apnews.com%2Fec%2Fd5%2Ff56fdc38f86e38b6217b50083d19%2Fd5ef4afe1cb64825be1349de8fd40df0\",\n" +
            "                    \"publish_date\": \"2024-05-29 04:30:08\",\n" +
            "                    \"authors\": []\n" +
            "                }\n" +
            "            ]\n" +
            "        }\n" +
            "    ],\n" +
            "    \"language\": \"en\",\n" +
            "    \"country\": \"us\"\n" +
            "}";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTopNews() throws IOException {

        ReflectionTestUtils.setField(wordNewsApiService, "apiKey", "test");
        ReflectionTestUtils.setField(wordNewsApiService, "apiDomainUrl", "http://localhost:3000");

        WordNewsApiService service = Mockito.spy(wordNewsApiService);

        HttpURLConnection mockConnection = Mockito.mock(HttpURLConnection.class);
        doReturn(mockConnection).when(service).createConnection(any(URL.class));
        when(mockConnection.getInputStream()).thenReturn(new ByteArrayInputStream(mockTopNewsStr.getBytes()));
        when(mockConnection.getResponseCode()).thenReturn(200);

        when(gwDateService.now()).thenReturn(LocalDate.now());

        WordNewsDto result = service.topNews("country");
        Assertions.assertNotNull(result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme