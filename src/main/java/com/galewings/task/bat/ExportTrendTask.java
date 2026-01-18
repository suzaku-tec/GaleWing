package com.galewings.task.bat;

import com.atilika.kuromoji.ipadic.Tokenizer;
import com.galewings.repository.FeedRepository;
import com.galewings.repository.TrendRepository;
import com.galewings.service.GwDateService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.galewings.service.GwDateService.DateFormat.DATE_TIME_COMMON;
import static com.galewings.service.GwDateService.DateFormat.SQLITE_DATE_FORMAT;

@Component
@Transactional
public class ExportTrendTask implements Runnable {

    private static final String NOUN = "名詞";

    private static final Set PartOfSpeechLevel2HoldSet = Set.of("一般", "固有名詞", "サ変接続");

    private final FeedRepository feedRepository;

    private final GwDateService gwDateService;

    private final TrendRepository trendRepository;

    public ExportTrendTask(FeedRepository feedRepository, GwDateService gwDateService, TrendRepository trendRepository) {
        this.feedRepository = feedRepository;
        this.gwDateService = gwDateService;
        this.trendRepository = trendRepository;
    }

    @Scheduled(cron = "${trend.scheduler.cron}")
    @Override
    public void run() {

        Tokenizer tokenizer = new Tokenizer();
        Map<String, Long> wordCounter = feedRepository.getAllFeed().stream()
                .filter(feed -> gwDateService.isToday(feed.publishedDate, DATE_TIME_COMMON.dtf))
                .flatMap(feed -> tokenizer.tokenize(feed.getTitle()).stream())
                .filter(token -> !"*".equals(token.getBaseForm()))
                .filter(token -> NOUN.equals(token.getPartOfSpeechLevel1()))
                .filter(token -> PartOfSpeechLevel2HoldSet.contains(token.getPartOfSpeechLevel2()))
                .map(token -> token.getBaseForm())
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()));

        wordCounter.forEach((word, count) -> {
            if (count == null) {
                return;
            }

            if (count < 5) {
                return;
            }

            trendRepository.insert(gwDateService.now().format(SQLITE_DATE_FORMAT.dtf), word, count);
        });

    }
}
