package com.galewings.task.bat;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import com.atilika.kuromoji.viterbi.ViterbiNode;
import com.galewings.entity.Feed;
import com.galewings.repository.FeedRepository;
import com.galewings.repository.WordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.mockito.Mockito.*;

class ExportWordTaskTest {
    @Mock
    FeedRepository feedRepository;
    @Mock
    WordRepository wordRepository;
    @InjectMocks
    ExportWordTask exportWordTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRun() {

        Feed testFeed = new Feed();
        testFeed.title = "";
        when(feedRepository.getAllFeed()).thenReturn(List.of(new Feed()));
        when(wordRepository.isExist(anyString())).thenReturn(true);
        when(wordRepository.insert(anyString())).thenReturn(0);

        Tokenizer mockTokenizer = mock(Tokenizer.class);
        when(mockTokenizer.tokenize(anyString())).thenReturn(List.of(new Token(1, "", ViterbiNode.Type.UNKNOWN, 1,
                null)));
        ReflectionTestUtils.setField(exportWordTask, "tokenizer", mockTokenizer);

        exportWordTask.run();
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: https://weirddev.com/forum#!/testme