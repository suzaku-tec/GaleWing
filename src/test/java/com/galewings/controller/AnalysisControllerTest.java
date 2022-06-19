package com.galewings.controller;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import com.galewings.dto.input.AnalysisFeedAllReadDto;
import com.galewings.entity.Feed;
import com.galewings.repository.FeedRepository;
import com.galewings.service.GwJaroDistanceService;
import com.galewings.service.HotwordService;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AnalysisControllerTest {

  @Mock
  HotwordService hotwordService;
  @Mock
  GwJaroDistanceService jaroDistanceService;
  @Mock
  FeedRepository feedRepository;
  @InjectMocks
  AnalysisController analysisController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testIndex() throws UnsupportedEncodingException {
    when(feedRepository.selectFeedFor(anyString())).thenReturn(new Feed());

    String result = analysisController.index("targetLink", null);
    Assertions.assertEquals("analysis", result);
  }

  @Test
  void testJaroWinklerDistance() {
    Feed testData = new Feed();
    when(jaroDistanceService.resembleTitleForJaroWinkler(anyString())).thenReturn(
        List.of(testData));

    List<Feed> result = analysisController.jaroWinklerDistance("targetTitle");
    Assertions.assertEquals(List.of(testData), result);
  }

  @Test
  void testAllRead() {
    when(feedRepository.updateReadFeed(anyString())).thenReturn(0);

    AnalysisFeedAllReadDto testData = new AnalysisFeedAllReadDto();
    testData.links = List.of("");

    boolean result = analysisController.allRead(testData);
    Assertions.assertEquals(true, result);
  }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme