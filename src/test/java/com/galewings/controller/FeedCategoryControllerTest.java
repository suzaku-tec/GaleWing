package com.galewings.controller;

import com.galewings.ModelMock;
import com.galewings.service.FeedCategoryService;
import com.galewings.service.filter.ClassificationCategory;
import com.galewings.service.filter.GwRuleBasedNewsClassifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.when;

class FeedCategoryControllerTest {
    @Mock
    FeedCategoryService feedCategoryService;
    @Mock
    GwRuleBasedNewsClassifier gwRuleBasedNewsClassifier;
    @InjectMocks
    FeedCategoryController feedCategoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void index() {
        ClassificationCategory classificationCategory = new ClassificationCategory();
        classificationCategory.setId("testCategory");
        when(gwRuleBasedNewsClassifier.getCategories()).thenReturn(List.of(classificationCategory));
        ModelMock modelMock = new ModelMock();

        String viewName = feedCategoryController.index(modelMock);
        Assertions.assertEquals("feedCategory/index", viewName);
        Assertions.assertEquals(List.of("testCategory"), modelMock.getAttribute("categoryList"));
    }

    @Test
    void getFeedCategories() {
        when(feedCategoryService.getFeedCategories("testCategory")).thenReturn(List.of());
        List<?> result = feedCategoryController.getFeedCategories("testCategory");
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }
}