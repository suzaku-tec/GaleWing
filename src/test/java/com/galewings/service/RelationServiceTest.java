package com.galewings.service;

import com.galewings.dto.relation.FeedRelation;
import com.galewings.entity.Feed;
import com.galewings.entity.FeedCategory;
import com.galewings.repository.FeedRepository;
import com.galewings.repository.FeedTagRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

class RelationServiceTest {
    @Mock
    FeedTagRepository feedTagRepository;
    @Mock
    FeedRepository feedRepository;
    @InjectMocks
    RelationService relationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testList() {
        when(feedTagRepository.selectLink(anyString())).thenReturn(List.of(new FeedCategory()));
        when(feedTagRepository.selectHighlyRelevantLink(any())).thenReturn(List.of("selectHighlyRelevantLinkResponse"));
        when(feedRepository.selectFeedFor(any())).thenReturn(new Feed());

        List<FeedRelation> result = relationService.list("link");
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void testListEmpty() {
        when(feedTagRepository.selectLink(anyString())).thenReturn(Collections.emptyList());
        when(feedTagRepository.selectHighlyRelevantLink(any())).thenReturn(List.of("selectHighlyRelevantLinkResponse"));
        when(feedRepository.selectFeedFor(any())).thenReturn(new Feed());

        List<FeedRelation> result = relationService.list("link");
        Assertions.assertEquals(0, result.size());
    }

}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme