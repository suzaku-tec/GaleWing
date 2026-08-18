package com.galewings.service;

import com.galewings.entity.Feed;
import com.galewings.repository.FeedRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.when;

class FeedCategoryServiceTest {

    @Mock
    FeedRepository feedRepository;
    @InjectMocks
    FeedCategoryService feedCategoryService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getFeedCategories() {
        when(feedRepository.findByCategoryId("testCategory")).thenReturn(List.of(new Feed()));
        List<Feed> feeds = feedCategoryService.getFeedCategories("testCategory");
        Assertions.assertNotNull(feeds);
        Assertions.assertEquals(1, feeds.size());
    }
}