package com.galewings.service;

import com.galewings.entity.Podcast;
import com.galewings.entity.PodcastFeed;
import com.galewings.repository.PodcastFeedRepository;
import com.galewings.repository.PodcastRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;

class PodcastServiceTest {
    @Mock
    PodcastRepository podcastRepository;
    @Mock
    PodcastFeedRepository podcastFeedRepository;
    @InjectMocks
    PodcastService podcastService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSync() throws IOException {
        Podcast test = new Podcast();
        test.url = "http://localhost:8080";

        BufferedReader reader = Mockito.mock(BufferedReader.class);
        when(reader.readLine()).thenReturn("").thenReturn(null);

        when(podcastRepository.selectAll()).thenReturn(List.of(test));

        podcastService.sync();
    }

    @Test
    void testCreatePodcastFeed() {
        PodcastFeed result = podcastService.createPodcastFeed("url");
        Assertions.assertEquals("url", result.url);
    }

}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme