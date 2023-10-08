package com.galewings.task.bat;

import com.atilika.kuromoji.ipadic.Tokenizer;
import com.galewings.dto.SiteTitleWordListDto;
import com.galewings.repository.FeedRepository;
import com.galewings.repository.TitleWordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExportSiteTitleWordTask implements Runnable {

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private TitleWordRepository titleWordRepository;

    private static final String NOUN = "名詞";
    private static final String PROPER_NOUN = "固有名詞";

    Tokenizer tokenizer = new Tokenizer();

    @Override
    public void run() {
        feedRepository.getAllFeed().stream().map(feed -> {
                    SiteTitleWordListDto dto = new SiteTitleWordListDto();
                    dto.link = feed.link;
                    dto.wordList = tokenizer.tokenize(feed.title).stream()
                            .filter(token -> !"*".equals(token.getBaseForm()))
                            .filter(token -> NOUN.equals(token.getPartOfSpeechLevel1()))
                            .filter(token -> !PROPER_NOUN.equals(token.getPartOfSpeechLevel2())).toList();
                    return dto;
                }).filter(dto -> !titleWordRepository.isExist(dto.link))
                .forEach(dto -> {
                    dto.wordList.stream().forEach(word -> {
                        titleWordRepository.insert(dto.link, word.getBaseForm());
                    });
                });
    }
}
