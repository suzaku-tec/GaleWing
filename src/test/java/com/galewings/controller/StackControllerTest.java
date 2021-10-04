package com.galewings.controller;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.galewings.TestEntityObject;
import com.galewings.dto.input.StackAddDto;
import com.galewings.entity.Stack;
import com.galewings.repository.FeedRepository;
import com.galewings.repository.StackRepository;
import java.text.ParseException;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class StackControllerTest {

  @Mock
  StackRepository stackRepository;
  @Mock
  FeedRepository feedRepository;
  @InjectMocks
  StackController stackController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void testIndex() {
    String result = stackController.index();
    Assertions.assertEquals("stack", result);
  }

  @Test
  void testGetStackList() throws JsonProcessingException, ParseException {
    Stack testStack = TestEntityObject.stack();

    when(stackRepository.getStackList()).thenReturn(Arrays.asList(testStack));

    String result = stackController.getStackList();
    Assertions.assertEquals(
        "[{\"title\":\"test-title\",\"uuid\":\"test-uuid\",\"link\":\"test-link\",\"uri\":\"test-uri\",\"author\":\"test-author\",\"comments\":\"test-comments\",\"publishedDate\":\"test-publishedDate\",\"stackDate\":558543600000}]",
        result);
  }

  @Test
  void testAddStack() {
    when(stackRepository.addStack(anyString(), anyString())).thenReturn(0);
    when(feedRepository.updateReadFeed(anyString())).thenReturn(0);

    String result = stackController.addStack(new StackAddDto());
    Assertions.assertEquals("true", result);
  }

}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme