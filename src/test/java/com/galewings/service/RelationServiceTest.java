package com.galewings.service;

import com.galewings.dto.relation.FeedRelation;
import com.galewings.repository.RelationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class RelationServiceTest {

    @Mock
    private RelationRepository relationRepository;

    @InjectMocks
    private RelationService relationService;

    @Test
    void list() {
        when(relationRepository.selectFeedRelationList(anyString())).thenReturn(List.of());
        List<FeedRelation> list = relationService.list("test-uuid");

        Assertions.assertEquals(0, list.size());
    }
}