package com.galewings.task.bat;

import com.atilika.kuromoji.dict.Dictionary;
import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import com.atilika.kuromoji.viterbi.ViterbiNode;
import com.galewings.entity.Feed;
import com.galewings.repository.FeedRepository;
import com.galewings.repository.TitleWordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

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
    void testRun1() {
        when(feedRepository.getAllFeed()).thenReturn(List.of(new Feed()));
        when(titleWordRepository.isExist(anyString())).thenReturn(true);
        when(titleWordRepository.insert(anyString(), anyString())).thenReturn(0);

        when(tokenizer.tokenize(anyString())).thenReturn(List.of(new Token(1, "", ViterbiNode.Type.UNKNOWN, 1,
                null)));
        ReflectionTestUtils.setField(exportSiteTitleWordTask, "tokenizer", tokenizer);

        exportSiteTitleWordTask.run();
    }

    @Test
    void testRun2() {
        when(feedRepository.getAllFeed()).thenReturn(List.of(new Feed()));
        when(titleWordRepository.isExist(anyString())).thenReturn(false);
        when(titleWordRepository.insert(anyString(), anyString())).thenReturn(0);

        when(tokenizer.tokenize(anyString())).thenReturn(List.of(new Token(1, "", ViterbiNode.Type.UNKNOWN, 1,
                null)));
        ReflectionTestUtils.setField(exportSiteTitleWordTask, "tokenizer", tokenizer);

        exportSiteTitleWordTask.run();
    }

    @Test
    void testRun3() {
        when(feedRepository.getAllFeed()).thenReturn(List.of(new Feed()));
        when(titleWordRepository.isExist(anyString())).thenReturn(true);
        when(titleWordRepository.insert(anyString(), anyString())).thenReturn(0);

        when(tokenizer.tokenize(anyString())).thenReturn(List.of(new Token(1, "", ViterbiNode.Type.UNKNOWN, 1,
                createMockDictionary("test"))));
        ReflectionTestUtils.setField(exportSiteTitleWordTask, "tokenizer", tokenizer);

        exportSiteTitleWordTask.run();
    }

    private Dictionary createMockDictionary(String retGetFeature) {
        return new Dictionary() {
            @Override
            public int getLeftId(int wordId) {
                return 0;
            }

            @Override
            public int getRightId(int wordId) {
                return 0;
            }

            @Override
            public int getWordCost(int wordId) {
                return 0;
            }

            @Override
            public String getAllFeatures(int wordId) {
                return null;
            }

            @Override
            public String[] getAllFeaturesArray(int wordId) {
                return new String[0];
            }

            @Override
            public String getFeature(int wordId, int... fields) {
                return retGetFeature;
            }
        };
    }
}
