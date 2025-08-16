package com.galewings.repository;

import com.galewings.dto.relation.FeedRelation;
import com.miragesql.miragesql.SqlManager;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class RelationRepositoryTest {

    @Mock
    private SqlManager sqlManager;

    @InjectMocks
    private RelationRepository relationRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void selectFeedRelationList() {
        when(sqlManager.getResultList(any(), any(), any()))
                .thenReturn(List.of()); // Mocking the return value
        List<FeedRelation> result = relationRepository.selectFeedRelationList("test-uuid");
        Assert.assertEquals(0, result.size()); // Asserting the size of the result list
    }
}