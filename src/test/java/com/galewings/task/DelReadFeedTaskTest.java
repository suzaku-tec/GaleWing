package com.galewings.task;

import com.galewings.repository.FeedRepository;
import com.galewings.repository.SiteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class DelReadFeedTaskTest {

  @Mock
  SiteRepository siteRepository;
  @Mock
  FeedRepository feedRepository;
  @InjectMocks
  DelReadFeedTask delReadFeedTask;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testDeleteReadFeed() {
    when(feedRepository.deleteReadFeed(any())).thenReturn(0);
    int result = delReadFeedTask.deleteReadFeed();
    Assertions.assertEquals(0, result);
  }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme