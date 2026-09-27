package com.galewings.task.trend;

import com.galewings.config.SudachiConfig;
import com.worksap.nlp.sudachi.Morpheme;
import com.worksap.nlp.sudachi.MorphemeList;
import com.worksap.nlp.sudachi.Tokenizer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;

class TitleTokenizerTest {

    @Test
    void extractKeywords() throws IOException {
        SudachiConfig config = new SudachiConfig();

        TitleTokenizer tt = new TitleTokenizer(createMockTokenizer());

        List<Morpheme> list = tt.extractKeywords("test");

        Assertions.assertNotNull(list);
    }

    private Tokenizer createMockTokenizer() {
        return new Tokenizer() {
            @Override
            public MorphemeList tokenize(SplitMode mode, String text) {
                return MorphemeList.EMPTY;
            }

            @Override
            public Iterable<MorphemeList> tokenizeSentences(SplitMode mode, String text) {
                return null;
            }

            @Override
            public Iterable<MorphemeList> tokenizeSentences(SplitMode mode, Reader input) throws IOException {
                return null;
            }

            @Override
            public Iterator<List<Morpheme>> lazyTokenizeSentences(SplitMode mode, Readable input) {
                return null;
            }

            @Override
            public void setDumpOutput(PrintStream output) {

            }

            @Override
            public String dumpInternalStructures(String text) {
                return "";
            }
        };
    }
}