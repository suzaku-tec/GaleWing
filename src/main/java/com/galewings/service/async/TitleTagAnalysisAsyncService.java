package com.galewings.service.async;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galewings.dto.tag.SiteTagInfo;
import com.galewings.entity.Feed;
import com.galewings.entity.Site;
import com.galewings.repository.FeedTagRepository;
import com.galewings.service.OllamaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
public class TitleTagAnalysisAsyncService {

    private final OllamaService ollamaService;

    private final FeedTagRepository feedTagRepository;

    @Autowired
    public TitleTagAnalysisAsyncService(OllamaService ollamaService, FeedTagRepository feedTagRepository) {
        this.ollamaService = ollamaService;
        this.feedTagRepository = feedTagRepository;
    }

    @Transactional
    @Async("taskTagAnalys")
    public CompletableFuture<Void> asyncMethod(Site site, Feed feed) throws JsonProcessingException {
        String tagInfoJsonStr = ollamaService.tellMe("以下のWebページタイトルから、コンテンツの主なカテゴリやテーマを表すタグを抽出してください。目的はコンテンツ分類です。\\n\\nタイトル: 「" + feed.title + "」\\n\\n以下のJSONスキーマに厳密に従って出力してください。JSON以外のテキストは一切出力せず、{ で始まり } で終わる有効なJSONのみを返してください。\\n\\n{\\n  \"title\": \"string\",  // 元のタイトル\\n  \"tags\": [\\n    {\\n      \"tag\": \"string\",  // 抽出されたタグ（名詞やキーワード、3個）\\n      \"category\": \"string\",  // タグのカテゴリ（例: 技術、ビジネス、エンタメ）\\n      \"relevance\": \"number\"  // 関連度 (0.0〜1.0)\\n    }\\n  ],\\n  \"primary_category\": \"string\"  // 主要カテゴリ\\n}\\n\n");

        // 1. 先頭の ```json または ```
        tagInfoJsonStr = tagInfoJsonStr.replaceAll("^```(?:json)?\\s*", "").trim();

        // 2. 末尾の ```
        tagInfoJsonStr = tagInfoJsonStr.replaceAll("\\s*```\\s*$", "").trim();

        ObjectMapper objectMapper = new ObjectMapper();
        SiteTagInfo info = objectMapper.readValue(tagInfoJsonStr, SiteTagInfo.class);
        feedTagRepository.insertSiteTagInfo(info, feed.link);

        return CompletableFuture.completedFuture(null);
    }
}
