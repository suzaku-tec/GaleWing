package com.galewings.service.async;

import com.galewings.repository.FeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class QueueUrlReadAsyncService {

  private final FeedRepository feedRepository;

  @Autowired
  public QueueUrlReadAsyncService(FeedRepository feedRepository) {
    this.feedRepository = feedRepository;
  }

  /**
   * キューイングされているURLを既読にする
   *
   * @return
   */
  @Async("taskExecutor")
  public CompletableFuture<Void> asyncMethod() {

    List<String> urls = feedRepository.selectReadListQueue();
    urls.forEach(url -> {
      feedRepository.updateReadFeedNoOpen(url);
      feedRepository.deleteReadListQueue(url);
    });

    return CompletableFuture.completedFuture(null);
  }
}
