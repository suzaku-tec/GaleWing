package com.galewings.task.bat;

import com.galewings.entity.Feed;
import com.galewings.repository.FeedGroupingRepository;
import com.galewings.repository.FeedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.when;

class FeedGroupingTaskTest {

    @Mock
    FeedRepository feedRepository;

    @Mock
    FeedGroupingRepository feedGroupingRepository;

    @InjectMocks
    FeedGroupingTask feedGroupingTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void run() {
        Feed feed1 = new Feed();
        feed1.title = "Test Feed 1";
        Feed feed2 = new Feed();
        feed2.title = "Test Feed 1";

        List<Feed> feedList = List.of(feed1, feed2);
        when(feedRepository.getAllFeed()).thenReturn(feedList);
        feedGroupingTask.run();
    }
}