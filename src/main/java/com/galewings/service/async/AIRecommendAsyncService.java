package com.galewings.service.async;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galewings.entity.Feed;
import com.galewings.repository.AIRecommendRepository;
import com.galewings.repository.FeedRepository;
import com.galewings.util.OllamaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Transactional
@Service
public class AIRecommendAsyncService {

    private final FeedRepository feedRepository;

    private final OllamaClient ollamaClient;

    private final AIRecommendRepository aiRecommendRepository;

    private static final Logger logger = LoggerFactory.getLogger(AIRecommendAsyncService.class);

    public AIRecommendAsyncService(FeedRepository feedRepository, OllamaClient ollamaClient, AIRecommendRepository aiRecommendRepository) {
        this.feedRepository = feedRepository;
        this.ollamaClient = ollamaClient;
        this.aiRecommendRepository = aiRecommendRepository;
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> asyncMethod() throws JsonProcessingException {

        LocalDate today = LocalDate.now();
        List<Feed> feedList = feedRepository.selectPublicDateFrom(today);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(feedList);

        if (!feedList.isEmpty()) {

            try {

                String prompt = """
                        下記のデータを分析して、主要なトピックや傾向を抽出し、サマリしたものをJSON形式で出力してください。
                        
                        # 元データ
                        %s
                        
                        # 出力フォーマット
                        {
                            news: [{
                              title: サマリタイトル
                              origin:元ネタになったURLの配列
                            }]
                        }
                        """.formatted(json);

                String response = ollamaClient.generate(prompt, "gemma4:latest");
                response = response.trim();
                if (response.startsWith("```json")) {
                    response = response.substring(7); // "```json" の長さ
                }
                if (response.endsWith("```")) {
                    response = response.substring(0, response.length() - 3);
                }
                response = response.trim();

                logger.debug("AI Recommend Response: {}", response);

                JsonNode root = mapper.readTree(response);
                JsonNode newsNode = root.get("news");

                for (JsonNode n : newsNode) {
                    String title = n.get("title").asText();
                    JsonNode originNode = n.get("origin");

                    String uuid = UUID.randomUUID().toString();

                    aiRecommendRepository.insertAIRecommend(uuid, title);

                    for (JsonNode urlNode : originNode) {
                        String url = urlNode.asText();
                        aiRecommendRepository.insertAIRecommendOrigin(uuid, url);
                    }
                }

            } catch (Exception e) {
                logger.error("Error in AI Recommend Async Method", e);
            }
        }

        return CompletableFuture.completedFuture(null);
    }
}
