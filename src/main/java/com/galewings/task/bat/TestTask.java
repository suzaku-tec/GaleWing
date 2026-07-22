package com.galewings.task.bat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TestTask implements Runnable {
    @Override
    public void run() {
        try {

            String response = """
                    ```json
                    {
                      "news": [
                        {
                          "title": "産総研（AIST）の地域拠点での一般公開・広報活動の活発化（2026年）",
                          "origin": [
                            "https://unit.aist.go.jp/tohoku/pr/koukai/2026/",
                            "https://www.aist.go.jp/aist_j/information/research_bases/shikoku/news/shikoku_20260807-001.html",
                            "https://www.aist.go.jp/aist_j/news/ev20260801.html",
                            "https://www.aist.go.jp/aist_j/information/research_bases/hokkaido/news/2026OpenDay.html"
                          ]
                        },
                        {
                          "title": "高市総理大臣の主要な公的活動報告（経済イベント出席および弔意メッセージ）",
                          "origin": [
                            "https://www.kantei.go.jp/jp/104/actions/202512/30dainoukai.html",
                            "https://www.kantei.go.jp/jp/104/discourse/20251230message.html"
                          ]
                        }
                      ]
                    }
                    ```""";
            ObjectMapper mapper = new ObjectMapper();
            response = response.trim();
            if (response.startsWith("```json")) {
                response = response.substring(7); // "```json" の長さ
            }
            if (response.endsWith("```")) {
                response = response.substring(0, response.length() - 3);
            }
            response = response.trim();

            JsonNode root = mapper.readTree(response);
            JsonNode newsNode = root.get("news");

            for (JsonNode n : newsNode) {
                String title = n.get("title").asText();
                JsonNode originNode = n.get("origin");

                String uuid = UUID.randomUUID().toString();

                System.out.println("UUID: " + uuid + ", TITLE: " + title);
                for (JsonNode urlNode : originNode) {
                    String url = urlNode.asText();
                    System.out.println("URL: " + url);
                }

                System.out.println("===");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
