package com.galewings.task.bat;

import com.atilika.kuromoji.ipadic.Tokenizer;
import com.galewings.repository.FeedRepository;
import com.galewings.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExportWordTask implements Runnable {

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private WordRepository wordRepository;

    private final String NOUN = "名詞";
    private final String PROPER_NOUN = "固有名詞";

    @Override
    public void run() {
        Tokenizer tokenizer = new Tokenizer();
        feedRepository.getAllFeed().stream()
                .map(feed -> feed.getTitle())
                .map(title -> tokenizer.tokenize(title))
                .flatMap(tokens -> tokens.stream())
                .filter(token -> !"*".equals(token.getBaseForm()))
                .filter(token -> NOUN.equals(token.getPartOfSpeechLevel1()))
                .filter(token -> !PROPER_NOUN.equals(token.getPartOfSpeechLevel2()))
                .filter(token -> wordRepository.isExist(token.getBaseForm()))
                .forEach(token -> wordRepository.insert(token.getBaseForm()));
    }
}
