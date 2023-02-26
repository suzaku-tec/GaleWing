package com.galewings.repository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.galewings.entity.Feed;
import com.galewings.entity.Site;
import com.miragesql.miragesql.SqlManager;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class FeedRepositoryTest {

  @Mock
  SqlManager sqlManager;
  @InjectMocks
  FeedRepository feedRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testExistFeed() {
    when(sqlManager.getCount(any(), any())).thenReturn(0);
    boolean result = feedRepository.existFeed("link");
    Assertions.assertFalse(result);
  }

  @Test
  void testGetAllFeed() {
    when(sqlManager.getResultList(any(), any())).thenReturn(Collections.emptyList());
    List<Feed> result = feedRepository.getAllFeed();
    Assertions.assertEquals(Collections.emptyList(), result);
  }

  @Test
  void testGetSiteFeed() {
    when(sqlManager.getResultList(any(), any(), any())).thenReturn(Collections.emptyList());
    List<Feed> result = feedRepository.getSiteFeed(new Site());
    Assertions.assertEquals(Collections.emptyList(), result);
  }

  @Test
  void testUpdateSiteFeedRead() {
    when(sqlManager.executeUpdate(any(), any())).thenReturn(0);
    int result = feedRepository.updateSiteFeedRead("identifier");
    Assertions.assertEquals(0, result);
  }

  @Test
  void testGetFeed() {
    when(sqlManager.getResultList(any(), any(), any())).thenReturn(Collections.emptyList());
    List<Feed> result = feedRepository.getFeed("uuid");
    Assertions.assertEquals(Collections.emptyList(), result);
  }

  @Test
  void testUpdateReadFeed() {
    int result = feedRepository.updateReadFeed("link");
    Assertions.assertEquals(0, result);
  }

  @Test
  void testInsertEntity() {
    when(sqlManager.insertEntity(any())).thenReturn(0);
    int result = feedRepository.insertEntity(new Feed());
    Assertions.assertEquals(0, result);
  }

  @Test
  void testSelectPublicDateFrom() {
    when(sqlManager.getResultList(any(), any(), any())).thenReturn(Collections.emptyList());
    List<Feed> result = feedRepository.selectPublicDateFrom(
        new GregorianCalendar(2023, Calendar.FEBRUARY, 26, 2, 31).getTime());
    Assertions.assertEquals(Collections.emptyList(), result);
  }

  @Test
  void testSelectFeedFor() {
    when(sqlManager.getSingleResult(any(), any(), any())).thenReturn(null);
    Feed result = feedRepository.selectFeedFor("link");
    Assertions.assertNull(result);
  }

  @Test
  void testDeleteReadFeed() {
    when(sqlManager.executeUpdate(any(), any())).thenReturn(0);
    feedRepository.deleteReadFeed("1");
  }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme