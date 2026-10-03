package com.galewings.service;

import com.galewings.util.WebSearchTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class OllamaChatService {

    private final ChatClient chatClient;
    private final WebSearchTools webSearchTools;

    public OllamaChatService(ChatClient.Builder chatClientBuilder, WebSearchTools webSearchTools) {
        this.chatClient = chatClientBuilder.build();
        this.webSearchTools = webSearchTools;
    }

    public String chat(String message) {
        return chatClient
                .prompt()
                .system("""
                        必要な場合はwebSearchツールを使用してください。
                        検索結果にない情報を事実として補わないでください。
                        回答には参照したURLを含めてください。
                        """)
                .user(message)
                .tools(webSearchTools)
                .call()
                .content();
    }
}