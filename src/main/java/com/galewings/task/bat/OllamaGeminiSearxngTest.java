package com.galewings.task.bat;

import com.galewings.service.GwDateService;
import com.galewings.service.OllamaChatService;
import com.galewings.util.ReportService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.Executor;

@Component
public class OllamaGeminiSearxngTest implements Runnable {

    private final ReportService reportService;

    private final GwDateService gwDateService;

    private final OllamaChatService ollamaChatService;

    private final Executor taskExecutor;

    public OllamaGeminiSearxngTest(ReportService reportService, GwDateService gwDateService, OllamaChatService ollamaChatService, Executor taskExecutor) {
        this.reportService = reportService;
        this.gwDateService = gwDateService;
        this.ollamaChatService = ollamaChatService;
        this.taskExecutor = taskExecutor;
    }

    @Override
    public void run() {

        String msg = """
                # 指示
                下記のブログの内容を1000文字以内にまとめた文章を、ブログのプロの目線でわかりやすく日本語で作ってください。
                
                https://www.oricon.co.jp/news/2481347/full/
                
                ## 制約条件
                - 自然な感情表現をするようにしてください。
                - 文と文の間の流れを自然にしてください。
                - 多様な表現を使用して文章に深みをつけてください。
                - 一貫したスタイルを保ってください。
                - SEO対策としてのキーワード使用は控えめにし、自然な文脈での使用を心がけてください。
                - 特に重要な部分は箇条書きでまとめてください。
                - 回答の根拠となったURLを最後にリストアップしてください
                
                """;

        taskExecutor.execute(() -> {
            String result = ollamaChatService.chat(msg);

            String nowStr = gwDateService.nowDateTime().format(GwDateService.DateFormat.FILE_TIME_FORMAT.dtf);
            String fileName = "test_" + nowStr + ".txt";

            try {
                reportService.report(fileName, result);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }
}
