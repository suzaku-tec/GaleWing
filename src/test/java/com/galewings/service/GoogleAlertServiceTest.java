package com.galewings.service;

import com.galewings.entity.Site;
import com.galewings.repository.FeedRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.Month;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

class GoogleAlertServiceTest {
    @Mock
    GwDateService gwDateService;
    @Mock
    FeedRepository feedRepository;
    @InjectMocks
    GoogleAlertService googleAlertService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsGoogleAlert() {
        boolean result = googleAlertService.isGoogleAlert(new Site());
        Assertions.assertFalse(result);
    }

    @Test
    void testUpdateFeed() {
        when(gwDateService.now()).thenReturn(LocalDate.of(2024, Month.FEBRUARY, 18));
        when(feedRepository.existFeed(anyString())).thenReturn(true);

        googleAlertService.updateFeed(new Site());
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme