package com.galewings.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageProxyControllerTest {

    @Mock
    private WebClient.Builder webClientBuilder; // ビルダーをモックする

    @Mock
    WebClient webClient;

    // WebClient のメソッドチェーン用モック
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private ClientResponse clientResponse;
    @Mock
    private ClientResponse.Headers clientResponseHeaders;

    ImageProxyController controller;

    @BeforeEach
    void setUp() {
        // 1. コンストラクタで呼ばれる WebClient.Builder の挙動を定義
        // codecs(...) は自分自身(this)を返し、build() はモックした webClient を返す
        when(webClientBuilder.codecs(any(Consumer.class))).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        // 2. コントローラーを手動でインスタンス化（確実です）
        controller = new ImageProxyController(webClientBuilder);

    }

    @Test
    void testProxyImage() {
        byte[] expectedBytes = new byte[]{1, 2, 3};
        ResponseEntity responseEntity = ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(expectedBytes);

        // 3. WebClient の get().uri().header() までの共通の挙動
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.exchangeToMono(any())).thenReturn(Mono.just(responseEntity));

        Mono<ResponseEntity<byte[]>> result = controller.proxyImage("url");
        Assertions.assertNotNull(result);
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme