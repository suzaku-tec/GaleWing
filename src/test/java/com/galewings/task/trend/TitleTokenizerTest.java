package com.galewings.task.trend;

import com.galewings.config.SudachiConfig;
import com.worksap.nlp.sudachi.Morpheme;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

class TitleTokenizerTest {

    @Test
    void extractKeywords() throws IOException {
        SudachiConfig config = new SudachiConfig();

        TitleTokenizer tt = new TitleTokenizer(config.sudachiTokenizer(config.sudachiDictionary()));

        List<Morpheme> list = tt.extractKeywords("test");

        Assertions.assertNotNull(list);
    }
}