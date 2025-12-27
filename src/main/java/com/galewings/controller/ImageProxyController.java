package com.galewings.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/proxy")
public class ImageProxyController {

    private final WebClient webClient;

    @Autowired
    public ImageProxyController(WebClient.Builder webClientBuilder) {
        // インスタグラムなどのCDNは User-Agent がないと拒否される場合があるため設定
        this.webClient = webClientBuilder
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)) // 10MBまで許容
                .build();
    }

    @GetMapping("/img")
    public Mono<ResponseEntity<byte[]>> proxyImage(@RequestParam("url") String url) {
        return webClient.get()
                .uri(url)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .exchangeToMono(response -> {
                    // 元のレスポンスの Content-Type を取得（デフォルトは image/jpeg）
                    MediaType contentType = response.headers().contentType().orElse(MediaType.IMAGE_JPEG);

                    return response.bodyToMono(byte[].class)
                            .map(bytes -> ResponseEntity.ok()
                                    .contentType(contentType)
                                    .body(bytes));
                })
                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()));
    }

}