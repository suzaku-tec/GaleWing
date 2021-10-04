package com.galewings.repository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.galewings.TestEntityObject;
import com.galewings.entity.Stack;
import com.miragesql.miragesql.SqlManager;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class StackRepositoryTest {

  @Mock
  SqlManager sqlManager;
  @InjectMocks
  StackRepository stackRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void testGetStackList() throws ParseException {
    List<Stack> testList = Arrays.asList(TestEntityObject.stack());
    when(sqlManager.getResultList(eq(Stack.class), any())).thenReturn(testList);

    List<Stack> result = stackRepository.getStackList();
    Assertions.assertEquals(testList, result);
  }

  @Test
  void testAddStack() {
    when(sqlManager.executeUpdate(any(), any())).thenReturn(1);

    int result = stackRepository.addStack("uuid", "link");
    Assertions.assertEquals(1, result);
  }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme