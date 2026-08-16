package com.galewings.task.bat;

import com.galewings.entity.Feed;
import com.galewings.entity.FeedClassification;
import com.galewings.repository.FeedClassificationRepository;
import com.galewings.repository.FeedRepository;
import com.galewings.service.GwDateService;
import com.galewings.service.filter.ClassificationResult;
import com.galewings.service.filter.GwRuleBasedNewsClassifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

class FeedClassificationTaskTest {

    @Mock
    GwRuleBasedNewsClassifier gwRuleBasedNewsClassifier;

    @Mock
    FeedRepository feedRepository;

    @Mock
    FeedClassificationRepository feedClassificationRepository;

    @Mock
    GwDateService dateService;

    @InjectMocks
    FeedClassificationTask feedClassificationTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRun_Feed_0() {
        when(feedRepository.getAllFeed()).thenReturn(Collections.emptyList());

        feedClassificationTask.run();

        verify(feedRepository).getAllFeed();
        verify(gwRuleBasedNewsClassifier, never()).classify(any());
    }

    @Test
    void testRun_Feed_1() {
        Feed feed = new Feed();
        ClassificationResult classificationResult = new ClassificationResult("Test Category", Collections.emptySet(), 0.0, Collections.emptyList());
        when(feedRepository.getAllFeed()).thenReturn(List.of(feed));
        when(feedClassificationRepository.existsClassification(any())).thenReturn(false);
        when(gwRuleBasedNewsClassifier.classify(feed)).thenReturn(classificationResult);
        when(gwRuleBasedNewsClassifier.getVersion()).thenReturn("test");
        when(dateService.now()).thenReturn(LocalDate.now());

        feedClassificationTask.run();

        verify(feedRepository).getAllFeed();
        verify(feedClassificationRepository).existsClassification(feed.uuid);
        verify(gwRuleBasedNewsClassifier).classify(feed);
        verify(gwRuleBasedNewsClassifier).getVersion();
    }

    @Test
    void testRun_Feed_1_ExistingClassification() {
        Feed feed = new Feed();
        ClassificationResult classificationResult = new ClassificationResult("Test Category", Collections.emptySet(), 0.0, Collections.emptyList());
        FeedClassification feedClassification = new FeedClassification();
        feedClassification.setClassifierVersion("0");

        when(feedRepository.getAllFeed()).thenReturn(List.of(feed));
        when(feedClassificationRepository.existsClassification(any())).thenReturn(true);
        when(gwRuleBasedNewsClassifier.classify(feed)).thenReturn(classificationResult);
        when(gwRuleBasedNewsClassifier.getVersion()).thenReturn("1");
        when(feedClassificationRepository.getClassification(any())).thenReturn(feedClassification);
        when(dateService.now()).thenReturn(LocalDate.now());

        feedClassificationTask.run();

        verify(feedRepository).getAllFeed();
        verify(feedClassificationRepository).existsClassification(feed.uuid);
        verify(gwRuleBasedNewsClassifier).classify(feed);
        verify(gwRuleBasedNewsClassifier, times(2)).getVersion();
    }

}