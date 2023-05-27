package com.galewings.controller;

import com.galewings.service.PodcastService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class PodcastControllerTest {
    @Mock
    PodcastService podcastService;
    @InjectMocks
    PodcastController podcastController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIndex() {
        String result = podcastController.index();
        Assertions.assertEquals("/podcast/index", result);
    }

    @Test
    void testAdd() {
        podcastController.add();
    }

    @Test
    void testSync() {
        String result = podcastController.sync();
        Assertions.assertEquals("", result);
    }

}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme