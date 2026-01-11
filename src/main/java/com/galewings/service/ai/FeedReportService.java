package com.galewings.service.ai;

import com.galewings.dto.SearxngSearchResult;
import com.galewings.entity.Feed;
import com.galewings.exception.GaleWingsSystemException;
import com.galewings.repository.FeedRepository;
import com.galewings.repository.NewsSummaryRepository;
import com.galewings.service.OllamaService;
import com.galewings.service.SearchService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class FeedReportService {

    private final FeedRepository feedRepository;

    private final OllamaService ollamaService;

    private final SearchService searchService;

    private final NewsSummaryRepository newsSummaryRepository;

    @Autowired
    public FeedReportService(FeedRepository feedRepository, OllamaService ollamaService, SearchService searchService, NewsSummaryRepository newsSummaryRepository) {
        this.feedRepository = feedRepository;
        this.ollamaService = ollamaService;
        this.searchService = searchService;
        this.newsSummaryRepository = newsSummaryRepository;
    }

    public void summary(String link) {
        Feed feed = feedRepository.selectFeedFor(link);

        newsSummaryRepository.insertSummary(feed.link);

        String prompt = """
                # 指示
                下記のブログの内容を1000文字以内にまとめた文章を、ブログのプロの目線でわかりやすく日本語で作ってください。
                
                %s
                
                ## 制約条件
                - 自然な感情表現をするようにしてください。
                - 文と文の間の流れを自然にしてください。
                - 多様な表現を使用して文章に深みをつけてください。
                - 一貫したスタイルを保ってください。
                - SEO対策としてのキーワード使用は控えめにし、自然な文脈での使用を心がけてください。
                - 特に重要な部分は箇条書きでまとめてください。
                """.formatted(feed.link);

        String result = ollamaService.tellMe(prompt);

        newsSummaryRepository.updateSummary(feed.link, result);

    }

    public void informationGathering(String link) {
        Feed feed = feedRepository.selectFeedFor(link);

        if (feed == null) {
            throw new GaleWingsSystemException("Not Found Feed. link:" + link);
        }

        newsSummaryRepository.insertInformationGathering(feed.link);

        KeywordResponse keywordResponse = getKeywordList(feed.title);

        String keywordSearchStr = keywordResponse.getKeywords().stream()
                .map(String::trim) // 前後の空白を除去するなどの加工が可能
                .collect(Collectors.joining(" "));

        List<SearxngSearchResult> search = searchService.search(keywordSearchStr).stream().limit(10).toList();

        String contents = search.stream().map(searxngSearchResult -> searxngSearchResult.content).collect(Collectors.joining("\n---\n"));

        String prompt = """
                あなたは、信頼性の高い情報を提示できる高精度なファクトベースAIです。
                内容を要約して報告してください。
                要約内容はルールに従って回答してください。
                
                ---
                %s
                ---
                
                # ルール
                - わからない/未確認は「わからない」と明言すること
                - 推測は「推測ですが」と明示すること \s
                - 現在日付（YYYY-MM-DD JST）を必ず明記すること \s
                - 根拠/出典（可能なら一次情報）を必ず添付すること \s
                - 専門的知見が必要な場合は「専門家に確認が」と明記すること \s
                - 統計・法/倫理・実務/実績の三つの観点から再評価し、結論が変わるかどうか、変わるなら理由を述べてください。
                - 出力：【結論】【根拠】【注意点・例外】【出典】【確実性: 高/中/低】
                
                """.formatted(contents);

        String result = ollamaService.tellMe(prompt);

        newsSummaryRepository.updateInformationGathering(feed.link, result);
    }

    /**
     * キーワード情報を抽出
     *
     * @param title タイトル
     * @return キーワード情報
     */
    private KeywordResponse getKeywordList(String title) {
        String keywordPrompt = """
                あなたはテキストから重要なキーワードを抽出するエキスパートです。
                
                # 指示
                - 入力テキストから重要なキーワード・フレーズを抽出してください。
                - 入力文に実際に出てくる語だけを使ってください。
                - 名詞中心に、日本語として自然な単語単位で抽出してください。
                - 出力はJSONで、次の形式だけを返してください：
                  {"keywords": ["キーワード1", "キーワード2", ...]}
                
                # 対象テキスト
                %s
                
                """.formatted(title);

        String resultKeywordPrompt = ollamaService.tellMe(keywordPrompt);
        Pattern pattern = Pattern.compile("```json\\s*(.*?)\\s*```", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(resultKeywordPrompt);

        if (!matcher.find()) {
            throw new GaleWingsSystemException("not analys keyword");
        }

        return new Gson().fromJson(matcher.group(1).trim(), KeywordResponse.class);
    }
}

class KeywordResponse {
    private List<String> keywords;

    // GetterとSetter
    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        return "KeywordResponse{keywords=" + keywords + "}";
    }
}