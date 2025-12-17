package com.galewings.service.async;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galewings.dto.tag.SiteTagInfo;
import com.galewings.entity.Feed;
import com.galewings.entity.Site;
import com.galewings.repository.FeedTagRepository;
import com.galewings.service.OllamaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

class TitleTagAnalysisAsyncServiceTest {
    @Mock
    OllamaService ollamaService;
    @Mock
    ObjectMapper objectMapper;
    @Mock
    FeedTagRepository feedTagRepository;
    @InjectMocks
    TitleTagAnalysisAsyncService titleTagAnalysisAsyncService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAsyncMethod() throws JsonProcessingException {
        String dummy = """
                {
                  "title": "ピンクと深紅の共演…四季桜と紅葉が同時に見頃、豊田「川見四季桜の里」",
                  "tags": [
                    {
                      "tag": "桜",
                      "category": "自然",
                      "relevance": 0.95
                    },
                    {
                      "tag": "紅葉",
                      "category": "自然",
                      "relevance": 0.9
                    },
                    {
                      "tag": "豊田",
                      "category": "地域",
                      "relevance": 0.8
                    },
                    {
                      "tag": "四季桜",
                      "category": "植物",
                      "relevance": 0.85
                    },
                    {
                      "tag": "観光",
                      "category": "エンタメ",
                      "relevance": 0.7
                    },
                    {
                      "tag": "川見四季桜の里",
                      "category": "地域",
                      "relevance": 0.8
                    }
                  ],
                  "primary_category": "自然"
                }
                """;
        when(ollamaService.tellMe(anyString())).thenReturn(dummy);

        Feed f = new Feed();
        f.link = "link";

        CompletableFuture<Void> result = titleTagAnalysisAsyncService.asyncMethod(new Site(), f);
        result.join();
        verify(feedTagRepository).insertSiteTagInfo(any(SiteTagInfo.class), anyString());
        Assertions.assertTrue(result.isDone());
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme