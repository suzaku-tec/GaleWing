package com.galewings.repository;

import com.galewings.entity.PodcastFeed;
import com.miragesql.miragesql.SqlManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

class PodcastFeedRepositoryTest {
    @Mock
    SqlManager sqlManager;
    @InjectMocks
    PodcastFeedRepository podcastFeedRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInsert() {
        when(sqlManager.insertEntity(any())).thenReturn(0);
        int result = podcastFeedRepository.insert(new PodcastFeed());
        Assertions.assertEquals(0, result);
    }

    @Test
    void testIsExist() {
        when(sqlManager.getCount(any(), any())).thenReturn(0);
        boolean result = podcastFeedRepository.isExist("url");
        Assertions.assertEquals(false, result);
    }

    @Test
    void testIsNotExist() {
        when(sqlManager.getCount(any(), any())).thenReturn(0);
        boolean result = podcastFeedRepository.isNotExist("url");
        Assertions.assertEquals(true, result);
    }

    @Test
    void testSelectAll() {
        when(sqlManager.getResultList(any(), any())).thenReturn(Collections.emptyList());
        List<PodcastFeed> result = podcastFeedRepository.selectAll();
        Assertions.assertEquals(Collections.emptyList(), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme