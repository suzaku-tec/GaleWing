package com.galewings.task.bat;

import com.galewings.entity.Feed;
import com.galewings.repository.FeedKeywordRepository;
import com.galewings.repository.FeedRepository;
import com.galewings.task.trend.TitleTokenizer;
import com.worksap.nlp.sudachi.Morpheme;
import com.worksap.nlp.sudachi.Tokenizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TrendAnalyseTest {
    @InjectMocks
    TrendAnalyse trendAnalyse;

    @Mock
    TitleTokenizer titleTokenizer;

    @Mock
    FeedRepository feedRepository;

    @Mock
    FeedKeywordRepository feedKeywordRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void run_フィードなし() {
        when(feedRepository.getAllFeed()).thenReturn(Collections.emptyList());

        trendAnalyse.run();

        verify(feedKeywordRepository, never()).insertKeyword(any());
    }

    @Test
    void run_フィード解析済み() {
        when(feedRepository.getAllFeed()).thenReturn(List.of(new Feed()));
        when(feedKeywordRepository.isAnalysed(any(), any())).thenReturn(true);

        trendAnalyse.run();

        verify(feedKeywordRepository, never()).insertKeyword(any());
    }

    @Test
    void run_キーワードなし() {
        when(feedRepository.getAllFeed()).thenReturn(List.of(new Feed()));
        when(feedKeywordRepository.isAnalysed(any(), any())).thenReturn(false);
        when(titleTokenizer.extractKeywords(any())).thenReturn(Collections.emptyList());

        trendAnalyse.run();

        verify(feedKeywordRepository, never()).insertKeyword(any());
    }

    @Test
    void run_キーワードあり() {
        Morpheme morpheme = new Morpheme() {
            @Override
            public int begin() {
                return 0;
            }

            @Override
            public int end() {
                return 0;
            }

            @Override
            public String surface() {
                return "test-surface";
            }

            @Override
            public List<String> partOfSpeech() {
                return List.of("test1", "test2");
            }

            @Override
            public short partOfSpeechId() {
                return 0;
            }

            @Override
            public String dictionaryForm() {
                return "";
            }

            @Override
            public String normalizedForm() {
                return "test-normalizedForm";
            }

            @Override
            public String readingForm() {
                return "";
            }

            @Override
            public List<Morpheme> split(Tokenizer.SplitMode mode) {
                return List.of();
            }

            @Override
            public boolean isOOV() {
                return false;
            }

            @Override
            public int getWordId() {
                return 0;
            }

            @Override
            public int getDictionaryId() {
                return 0;
            }

            @Override
            public int[] getSynonymGroupIds() {
                return new int[0];
            }
        };

        when(feedRepository.getAllFeed()).thenReturn(List.of(new Feed()));
        when(feedKeywordRepository.isAnalysed(any(), any())).thenReturn(false);
        when(titleTokenizer.extractKeywords(any())).thenReturn(List.of(morpheme));

        trendAnalyse.run();

        verify(feedKeywordRepository).insertKeyword(any());
    }


}
