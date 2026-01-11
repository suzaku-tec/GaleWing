package com.galewings.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galewings.dto.SearxngResponse;
import com.galewings.dto.SearxngSearchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SearchServiceTest {

    @InjectMocks
    private SearchService searchService;

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private HttpResponse<String> mockResponse;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // @Value フィールドの注入
        ReflectionTestUtils.setField(searchService, "baseUrl", "http://localhost:8080/search");
        // コンストラクタで生成された HttpClient をモックに差し替え
        ReflectionTestUtils.setField(searchService, "httpClient", mockHttpClient);
    }

    @Test
    @DisplayName("正常系：検索結果が正しく取得できること")
    void search_Success() throws Exception {
        // 準備：モックのレスポンスJSONを作成
        SearxngResponse mockData = new SearxngResponse();
        SearxngSearchResult result = new SearxngSearchResult();
        result.title = "Test Title";
        mockData.results = List.of(result);
        String json = objectMapper.writeValueAsString(mockData);

        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(json);
        when(mockHttpClient.send(any(), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);

        // 実行
        List<SearxngSearchResult> results = searchService.search("test-query");

        // 検証
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Test Title", results.get(0).title);
    }

    @Test
    @DisplayName("異常系：ステータスコードが200以外の場合に空リストを返す（または例外）")
    void search_Non200Response() throws Exception {
        // 準備
        when(mockResponse.statusCode()).thenReturn(500);
        when(mockHttpClient.send(any(), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);

        // 実行（現在の実装では catch して printStackTrace して空リストを返す）
        List<SearxngSearchResult> results = searchService.search("test-query");

        // 検証
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("例外系：通信エラーが発生した場合に空リストを返す")
    void search_Exception() throws Exception {
        // 準備
        when(mockHttpClient.send(any(), any())).thenThrow(new IOException("Connection error"));

        // 実行
        List<SearxngSearchResult> results = searchService.search("test-query");

        // 検証
        assertTrue(results.isEmpty());
    }
}