package com.galewings.task;

import com.galewings.repository.NewsSummaryRepository;
import com.galewings.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AutoSummaryTask {

    @Autowired
    private GeminiService geminiService;

    @Autowired
    private NewsSummaryRepository newsSummaryRepository;

    /**
     * 待ち時間
     */
    private final long WAITE_TIME = 20000;

    @Scheduled(cron = "${update.summary.scheduler.cron}")
    public void autoSammary() {
        newsSummaryRepository.selectNoSummary().stream().map(news -> {
            String text = "下記のブログの内容を1000文字以内にまとめた文章を、ブログのプロの目線でわかりやすく日本語で作ってください。特に重要な部分は箇条書きでまとめてください。\r\n" + news.feedUuid;
            String summary = null;
            try {
                summary = geminiService.tellMe(text).getCandidates().get(0).getContent().getParts().get(0).getText();
                Thread.sleep(WAITE_TIME);
            } catch (InterruptedException e) {
                summary = "";
                e.printStackTrace();
            }
            news.setSummary(summary);
            return news;
        }).forEach(newsSummary -> {
            newsSummaryRepository.update(newsSummary.getFeedUuid(), newsSummary.getSummary());
        });


    }
}
