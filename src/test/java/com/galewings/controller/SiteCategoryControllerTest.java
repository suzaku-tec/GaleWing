package com.galewings.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.galewings.ModelMock;
import com.galewings.dto.input.AddSiteCategoryDto;
import com.galewings.dto.input.DeleteSiteCategory;
import com.galewings.entity.Category;
import com.galewings.entity.Site;
import com.galewings.repository.CategoryRepository;
import com.galewings.repository.SiteCategoryRepository;
import com.galewings.repository.SiteRepository;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SiteCategoryControllerTest {

  @Mock
  SiteRepository siteRepository;
  @Mock
  SiteCategoryRepository siteCategoryRepository;
  @Mock
  CategoryRepository categoryRepository;
  @InjectMocks
  SiteCategoryController siteCategoryController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testIndex() {
    when(siteRepository.getSite(any())).thenReturn(new Site());
    when(siteCategoryRepository.selectCategoryFor(any())).thenReturn(List.of(new Category()));
    when(categoryRepository.selectAll()).thenReturn(List.of(new Category()));

    ModelMock modelMock = new ModelMock();

    String result = siteCategoryController.index("uuid", modelMock);
    Assertions.assertEquals("siteCategory", result);
    Assertions.assertNotNull(modelMock.getAttribute("site"));
    Assertions.assertNotNull(modelMock.getAttribute("siteCategoryList"));
    Assertions.assertNotNull(modelMock.getAttribute("categoryList"));
  }

  @Test
  void testAddSiteCategory() {
    List<Category> testCategoryList = List.of(new Category());

    when(siteCategoryRepository.selectCategoryFor(any())).thenReturn(testCategoryList);
    when(siteCategoryRepository.add(any(), any())).thenReturn(0);

    List<Category> result = siteCategoryController.addSiteCategory(new AddSiteCategoryDto());
    Assertions.assertEquals(testCategoryList, result);
  }

  @Test
  void testDelete() {
    when(siteCategoryRepository.deleteCategory(any(), any())).thenReturn(0);

    siteCategoryController.delete(new DeleteSiteCategory());
  }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme