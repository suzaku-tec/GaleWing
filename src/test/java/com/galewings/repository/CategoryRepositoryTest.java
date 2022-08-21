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

class CategoryRepositoryTest {

  @Mock
  SqlManager sqlManager;
  @InjectMocks
  CategoryRepository categoryRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testSelectAll() {
    when(sqlManager.getResultList(any(), any())).thenReturn(Collections.emptyList());
    List<Category> result = categoryRepository.selectAll();
    Assertions.assertEquals(0, result.size());
  }

  @Test
  void testAdd() {
    when(sqlManager.insertEntity(any())).thenReturn(0);
    categoryRepository.add(new Category());
  }

  @Test
  void testDelete() {
    when(sqlManager.executeUpdate(any(), any())).thenReturn(0);
    categoryRepository.delete("uuid");
  }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme