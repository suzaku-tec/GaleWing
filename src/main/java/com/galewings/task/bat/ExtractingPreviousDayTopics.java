package com.galewings.task.bat;

import com.galewings.entity.Feed;
import com.galewings.repository.FeedRepository;
import com.galewings.service.GeminiService;
import com.galewings.util.ReportService;
import com.galewings.util.markdown.MarkdownBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExtractingPreviousDayTopics implements Runnable {

    private final FeedRepository feedRepository;

    private final GeminiService geminiService;

    private final ReportService reportService;

    @Autowired
    public ExtractingPreviousDayTopics(FeedRepository feedRepository, GeminiService geminiService, ReportService reportService) {
        this.feedRepository = feedRepository;
        this.geminiService = geminiService;
        this.reportService = reportService;
    }

    @Override
    public void run() {
        // 前日の00:00:00~23:59:59の取得
        LocalDate today = LocalDate.now();
        LocalDate previousDay = today.minusDays(1);
        LocalDateTime startOfPreviousDay = LocalDateTime.of(previousDay, LocalTime.MIN);
        LocalDateTime endOfPreviousDay = LocalDateTime.of(previousDay, LocalTime.MAX);

        List<Feed> feedList = feedRepository.selectFeedToRange(startOfPreviousDay, endOfPreviousDay);

        String feedListStr = feedList.stream().map(feed -> MarkdownBuilder.create().list().link(feed.title, feed.uri).build()).collect(Collectors.joining());

        String previousDayTopics = geminiService.tellMe("前日のニュースについて下記の通りです。話題になっているニュースについて上位50件を抜き出してください" + System.lineSeparator() + "---" + System.lineSeparator() + feedListStr);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = today.format(formatter);

        try {
            reportService.report(formattedDate + "_topics.txt", previousDayTopics);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
