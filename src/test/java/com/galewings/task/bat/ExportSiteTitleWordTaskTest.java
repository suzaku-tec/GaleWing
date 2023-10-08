package com.galewings.task.bat;

import com.atilika.kuromoji.ipadic.Tokenizer;
import com.galewings.entity.Feed;
import com.galewings.repository.FeedRepository;
import com.galewings.repository.TitleWordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

class ExportSiteTitleWordTaskTest {
    @Mock
    FeedRepository feedRepository;
    @Mock
    TitleWordRepository titleWordRepository;
    @Mock
    Tokenizer tokenizer;
    @InjectMocks
    ExportSiteTitleWordTask exportSiteTitleWordTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRun() {
        when(feedRepository.getAllFeed()).thenReturn(List.of(new Feed()));
        when(titleWordRepository.isExist(anyString())).thenReturn(true);
        when(titleWordRepository.insert(anyString(), anyString())).thenReturn(0);

        exportSiteTitleWordTask.run();
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme