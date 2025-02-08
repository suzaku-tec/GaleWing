package com.galewings.service.async;

import com.galewings.repository.FeedRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

class QueueUrlReadAsyncServiceTest {
  @Mock
  FeedRepository feedRepository;
  @InjectMocks
  QueueUrlReadAsyncService queueUrlReadAsyncService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testAsyncMethod() throws ExecutionException, InterruptedException {
    when(feedRepository.updateReadFeed(anyString())).thenReturn(0);
    when(feedRepository.selectReadListQueue()).thenReturn(List.of("selectReadListQueueResponse"));
    when(feedRepository.deleteReadListQueue(anyString())).thenReturn(0);

    CompletableFuture<Void> result = queueUrlReadAsyncService.asyncMethod();
    Assertions.assertNull(result.get());
  }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme