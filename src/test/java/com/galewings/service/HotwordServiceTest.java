package com.galewings.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.galewings.entity.Feed;
import com.galewings.repository.FeedRepository;
import com.galewings.service.HotwordService.FromDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class HotwordServiceTest {

  //Field tokenizer of type JapaneseTokenizer - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
  @Mock
  FeedRepository feedRepository;
  @InjectMocks
  HotwordService hotwordService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testHotword() {

    Feed testData = new Feed();
    testData.title = "test";

    when(feedRepository.selectPublicDateFrom(any())).thenReturn(List.of(testData));

    Map<String, Long> resultMap = hotwordService.hotword(FromDate.ONE_HOUR_AGO);
    assertEquals(1, resultMap.size());
    assertEquals(Long.valueOf(1L), resultMap.get("test"));
  }
}
