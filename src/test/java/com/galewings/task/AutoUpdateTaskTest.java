package com.galewings.task;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import com.galewings.entity.Site;
import com.galewings.repository.FeedRepository;
import com.galewings.repository.SiteRepository;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AutoUpdateTaskTest {

  @Mock
  SiteRepository siteRepository;
  @Mock
  FeedRepository feedRepository;
  @InjectMocks
  AutoUpdateTask autoUpdateTask;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void testAllUpdate() {
    when(siteRepository.getAllSite()).thenReturn(Arrays.asList(new Site()));
    when(feedRepository.existFeed(anyString())).thenReturn(true);

    autoUpdateTask.allUpdate();
  }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme