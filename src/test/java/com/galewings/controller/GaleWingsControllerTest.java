package com.galewings.controller;

import com.galewings.ModelMock;
import com.galewings.entity.SiteFeedCount;
import com.galewings.repository.SiteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.when;

class GaleWingsControllerTest {
    @Mock
    SiteRepository siteRepository;
    @InjectMocks
    GaleWingsController galeWingsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testIndex() {
        when(siteRepository.getSiteFeedCount()).thenReturn(List.of(new SiteFeedCount()));

        ModelMock modelMock = new ModelMock();
        String result = galeWingsController.index(modelMock, "uuid");
        Assertions.assertEquals("index", result);
    }

    @Test
    void testCardLayout() {
        when(siteRepository.getSiteFeedCount()).thenReturn(List.of(new SiteFeedCount()));

        ModelMock modelMock = new ModelMock();
        String result = galeWingsController.cardLayout(modelMock, "uuid", "1");
        Assertions.assertEquals("card", result);
    }

    @Test
    void testCardLayout2() {
        when(siteRepository.getSiteFeedCount()).thenReturn(List.of(new SiteFeedCount()));

        ModelMock modelMock = new ModelMock();
        String result = galeWingsController.cardLayout(modelMock, "uuid", "2");
        Assertions.assertEquals("card", result);
    }

}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme