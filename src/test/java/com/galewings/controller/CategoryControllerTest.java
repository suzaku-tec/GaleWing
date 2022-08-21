package com.galewings.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.galewings.ModelMock;
import com.galewings.dto.input.AddCategory;
import com.galewings.dto.input.DeleteCategory;
import com.galewings.repository.CategoryRepository;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CategoryControllerTest {

  @Mock
  CategoryRepository categoryRepository;
  @InjectMocks
  CategoryController categoryController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testIndex() {
    when(categoryRepository.selectAll()).thenReturn(Collections.emptyList());
    ModelMock modelMock = new ModelMock();
    String result = categoryController.index(modelMock);
    Assertions.assertEquals("category", result);
    Assertions.assertNotNull(modelMock.getAttribute("categoryList"));
  }

  @Test
  void testAddCategory() {
    doNothing().when(categoryRepository).add(any());
    categoryController.addCategory(new AddCategory());
  }

  @Test
  void testDelete() {
    doNothing().when(categoryRepository).delete(any());
    categoryController.delete(new DeleteCategory());
  }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme