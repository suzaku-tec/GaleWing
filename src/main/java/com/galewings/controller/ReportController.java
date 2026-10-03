package com.galewings.controller;

import com.galewings.dto.input.KeywordSummaryDto;
import com.galewings.service.GwDateService;
import com.galewings.service.OllamaChatService;
import com.galewings.util.ReportService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

@RequestMapping("/report")
@Controller
@Transactional
public class ReportController {

    private final ReportService reportService;

    private final GwDateService gwDateService;

    private final OllamaChatService ollamaChatService;

    private final Executor taskExecutor;

    public ReportController(ReportService reportService, GwDateService gwDateService, OllamaChatService ollamaChatService, Executor taskExecutor) {
        this.reportService = reportService;
        this.gwDateService = gwDateService;
        this.ollamaChatService = ollamaChatService;
        this.taskExecutor = taskExecutor;
    }

    @GetMapping("/list")
    @ResponseBody
    public List<String> getReportsList() throws IOException {
        return this.reportService.getReportList();
    }

    @PostMapping("/keyword/list")
    @ResponseBody
    public void summaryReport(@RequestBody KeywordSummaryDto keywordSummaryDto) {

        StringBuilder sb = new StringBuilder("以下の記事の内容を統合して要約してください。");

        keywordSummaryDto.linkList.forEach(link -> sb.append("- ").append(link.link).append("\n"));

        sb.append("""
                出力内容は、下記の内容に沿って出力してください
                
                【目的】
                分析対象の記事を参照し、全体を要約して下記のフォーマットで出力してください
                
                【要約の方針】
                - 複数の記事に共通して登場する事実・主張・論点を中心に整理する
                - 同じ内容が複数の記事にある場合は、重複して列挙せず、一つに統合する
                - 共通点の中でも、特に重要なものから順に示す
                - 記事によって見解、具体例、数値、前提条件が異なる場合は、その違いを補足する
                - 共通点があるように見えても、実際には意味や前提が異なる場合は区別する
                - どの記事にも明確な根拠がない内容は、共通認識として扱わない
                - 情報源に書かれていないことは推測で補わない
                
                【出力形式】
                1. 全体要約
                   - [キーワードやテーマ]について、最も重要な内容を[300〜500字]でまとめる
                
                2. 共通する主要論点
                   - 論点
                   - 内容
                   - この論点を扱っている記事
                   - 記事間での一致度や注意点
                
                3. 記事による違い
                   - 観点
                   - 共通している部分
                   - 異なる部分
                   - 違いが生じる理由として考えられる前提条件
                
                4. 重要な具体例・数値・固有名詞
                
                5. 注意点・未確認事項
                
                6. 情報源一覧
                
                """);

        taskExecutor.execute(() -> {
            String result = ollamaChatService.chat(sb.toString());

            String nowStr = gwDateService.nowDateTime().format(GwDateService.DateFormat.FILE_TIME_FORMAT.dtf);
            String fileName = keywordSummaryDto.keyword + "_" + nowStr + ".txt";

            try {
                reportService.report(fileName, result);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }
}
