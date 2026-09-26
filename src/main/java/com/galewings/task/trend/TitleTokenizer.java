package com.galewings.task.trend;

import com.worksap.nlp.sudachi.Morpheme;
import com.worksap.nlp.sudachi.Tokenizer;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class TitleTokenizer {
    private final Tokenizer tokenizer;

    public TitleTokenizer(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public List<Morpheme> extractKeywords(String title) {
        String normalized = normalize(title);

        Set<String> words = new HashSet<String>();

        return tokenizer.tokenize(
                        Tokenizer.SplitMode.B,
                        normalized
                )
                .stream()
                .filter(this::isTargetToken)
                .filter(morpheme -> morpheme.normalizedForm().length() >= 2)
                .filter(morpheme -> words.add(morpheme.normalizedForm().toLowerCase()))
                .toList();
    }

    private boolean isTargetToken(Morpheme morpheme) {
        List<String> partOfSpeechList = morpheme.partOfSpeech();

        return partOfSpeechList.stream().anyMatch(word -> word.contains("名詞") || word.contains("固有名詞"));
    }

    /**
     * 表記揺れを統一
     * <p>
     * - Unicodeの仕様上、NFKC
     * - 連続の空白を1文字の空白に置換
     * - トリム処理
     *
     * @param title タイトル
     * @return 表記揺れ是正後のタイトル
     */
    private String normalize(String title) {
        if (title == null || title.isEmpty()) {
            return title;
        }

        return Normalizer.normalize(title, Normalizer.Form.NFKC)
                .replace("\\s+", " ")
                .trim();
    }
}
