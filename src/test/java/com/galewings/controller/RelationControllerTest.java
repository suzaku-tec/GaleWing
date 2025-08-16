package com.galewings.controller;

import com.galewings.dto.input.RelationListDto;
import com.galewings.dto.relation.FeedRelation;
import com.galewings.service.RelationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class RelationControllerTest {

    @Mock
    private RelationService relationService;

    @InjectMocks
    private RelationController relationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void list() {
        when(relationService.list(anyString())).thenReturn(List.of());
        List<FeedRelation> list = relationController.list(new RelationListDto());

        assertEquals(0, list.size());
    }
}