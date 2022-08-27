package com.galewings.repository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.galewings.entity.Category;
import com.miragesql.miragesql.SqlManager;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SiteCategoryRepositoryTest {

  @Mock
  SqlManager sqlManager;
  @InjectMocks
  SiteCategoryRepository siteCategoryRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testSelectCategoryFor() {
    when(sqlManager.getResultList(any(), any(), any())).thenReturn(Collections.emptyList());

    List<Category> result = siteCategoryRepository.selectCategoryFor("siteUuid");
    Assertions.assertEquals(Collections.emptyList(), result);
  }

  @Test
  void testAdd() {
    when(sqlManager.executeUpdate(any(), any())).thenReturn(0);

    int result = siteCategoryRepository.add("categoryUuid", "siteUuid");
    Assertions.assertEquals(0, result);
  }

  @Test
  void testDeleteCategory() {
    when(sqlManager.executeUpdate(any(), any())).thenReturn(0);
    int result = siteCategoryRepository.deleteCategory("categoryUuid", "siteUuid");
    Assertions.assertEquals(0, result);
  }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme