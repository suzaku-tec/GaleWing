package com.galewings.task.bat;

import com.galewings.repository.FeedTagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class DelFeedCategoryTest {
    @Mock
    FeedTagRepository feedTagRepository;
    @InjectMocks
    DelFeedCategory delFeedCategory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRun() {
        when(feedTagRepository.deleteLink()).thenReturn(0);

        delFeedCategory.run();

        verify(feedTagRepository, times(1)).deleteLink();
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme