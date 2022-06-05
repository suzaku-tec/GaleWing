package com.galewings.service;

import static org.mockito.Mockito.when;

import com.galewings.entity.Feed;
import com.galewings.repository.FeedRepository;
import java.util.List;
import org.apache.lucene.search.spell.JaroWinklerDistance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class GwJaroDistanceServiceTest {

  @Mock
  JaroWinklerDistance jaroWinklerDistance;
  @Mock
  FeedRepository feedRepository;
  @InjectMocks
  GwJaroDistanceService gwJaroDistanceService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testResembleTitleForJaroWinkler() {
    Feed testData1 = new Feed();
    testData1.title = "足なんて飾りです。偉い人にはそれが分からんのです。";
    Feed testData2 = new Feed();
    testData2.title = "親父にもぶたれたことないのに";
    when(feedRepository.getAllFeed()).thenReturn(List.of(testData1, testData2));

    List<Feed> result = gwJaroDistanceService.resembleTitleForJaroWinkler(
        "胸なんて飾りです。男にはそれが分からんのです。");
    Assertions.assertEquals(List.of(testData1), result);
  }

  @Test
  void testResembleTitleForJaroWinkler2() {
    Feed testData = new Feed();
    testData.title = "足なんて飾りです。偉い人にはそれが分からんのです。";
    List<Feed> result = gwJaroDistanceService.resembleTitleForJaroWinkler("胸なんて飾りです。男にはそれが分からんのです。",
        List.of(testData));
    Assertions.assertEquals(List.of(testData), result);
  }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme