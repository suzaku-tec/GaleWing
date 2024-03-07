package com.galewings.task.bat;

import com.galewings.repository.NewsSummaryRepository;
import com.galewings.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestAutoSummaryTask implements Runnable {

    @Autowired
    private GeminiService geminiService;

    @Autowired
    private NewsSummaryRepository newsSummaryRepository;

    @Override
    public void run() {
        var list = newsSummaryRepository.selectNoSummary();
        if (!list.isEmpty()) {
            String text =
                    "下記のブログの内容を1000文字以内にまとめた文章を、ブログのプロの目線でわかりやすく日本語で作ってください。特に重要な部分は箇条書きでまとめてください。\r\n" + list.get(0).getFeedUuid();
            String summary = null;
            try {
                summary = geminiService.tellMe(text).getCandidates().get(0).getContent().getParts().get(0).getText();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println("回答:" + summary);
        }
    }
}
