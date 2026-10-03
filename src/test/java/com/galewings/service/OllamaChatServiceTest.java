package com.galewings.service;

import com.galewings.util.WebSearchTools;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OllamaChatServiceTest {

    @Mock
    private ChatClient chatClient;

    @Mock
    private ChatClient.Builder chatClientBuilder;

    @Mock
    private ChatClient.ChatClientRequestSpec requestSpec;

    @Mock
    private ChatClient.CallResponseSpec callResponseSpec;

    @Mock
    private WebSearchTools webSearchTools;

    private OllamaChatService ollamaChatService;

    @BeforeEach
    void setUp() {
        // ChatClient.Builder の build() メソッドが chatClient を返すようにスタブ
        when(chatClientBuilder.build()).thenReturn(chatClient);

        // チェーンメソッドのスタブ
        lenient().when(chatClient.prompt()).thenReturn(requestSpec);
        lenient().when(requestSpec.system(any(String.class))).thenReturn(requestSpec);
        lenient().when(requestSpec.user(any(String.class))).thenReturn(requestSpec);
        lenient().when(requestSpec.tools(any())).thenReturn(requestSpec);
        lenient().when(requestSpec.call()).thenReturn(callResponseSpec);
        lenient().when(callResponseSpec.content()).thenReturn("応答");
    }

    @Test
    @DisplayName("chat メソッドが LLM の応答を返すこと")
    void chat_returnsLlmResponse() {
        // Arrange
        String expectedResponse = "これはテスト応答です。[https://example.com]";
        when(callResponseSpec.content()).thenReturn(expectedResponse);

        // Act
        ollamaChatService = new OllamaChatService(chatClientBuilder, webSearchTools);
        String actualResponse = ollamaChatService.chat("テストメッセージ");

        // Assert
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("システムプロンプトが正しく設定されること")
    void chat_systemPromptIsSetCorrectly() {
        // Arrange
        ArgumentCaptor<String> systemPromptCaptor = ArgumentCaptor.forClass(String.class);
        when(requestSpec.system(systemPromptCaptor.capture())).thenReturn(requestSpec);

        // Act
        ollamaChatService = new OllamaChatService(chatClientBuilder, webSearchTools);
        ollamaChatService.chat("テスト");

        // Assert
        String capturedSystemPrompt = systemPromptCaptor.getValue();
        assertThat(capturedSystemPrompt).contains("webSearchツールを使用");
        assertThat(capturedSystemPrompt).contains("検索結果にない情報を事実として補わない");
        assertThat(capturedSystemPrompt).contains("参照したURLを含めて");
    }
}