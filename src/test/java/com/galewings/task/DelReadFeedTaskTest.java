package com.galewings.task;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import com.galewings.repository.FeedRepository;
import com.galewings.repository.SiteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    doNothing().when(feedRepository).deleteReadFeed(any());
    delReadFeedTask.deleteReadFeed();
  }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme