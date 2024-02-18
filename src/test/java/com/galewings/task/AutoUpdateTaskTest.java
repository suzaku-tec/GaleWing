package com.galewings.task;

import com.galewings.entity.Site;
import com.galewings.repository.FeedRepository;
import com.galewings.repository.SiteRepository;
import com.galewings.service.GoogleAlertService;
import com.galewings.service.GwDateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

class AutoUpdateTaskTest {

    @Mock
    SiteRepository siteRepository;

    @Mock
    FeedRepository feedRepository;

    @Mock
    GwDateService gwDateService;

    @Mock
    private GoogleAlertService googleAlertService;

    @InjectMocks
    AutoUpdateTask autoUpdateTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAllUpdate() {
        when(siteRepository.getAllSite()).thenReturn(List.of(new Site()));
        when(googleAlertService.isGoogleAlert(any())).thenReturn(false);
        when(feedRepository.existFeed(anyString())).thenReturn(true);

        autoUpdateTask.allUpdate();
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme