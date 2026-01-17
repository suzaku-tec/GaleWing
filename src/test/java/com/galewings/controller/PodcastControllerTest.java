package com.galewings.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.galewings.entity.PodcastFeed;
import com.galewings.service.PodcastService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PodcastControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PodcastService podcastService;

    @InjectMocks
    private PodcastController podcastController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(podcastController).build();
    }

    @Test
    @DisplayName("index: 正常系 - ビュー名とモデル属性の確認")
    public void testIndex() throws Exception {
        mockMvc.perform(get("/podcast"))
                .andExpect(status().isOk())
                .andExpect(view().name("/podcast/index"))
                .andExpect(model().attributeExists("modalFileList"));
    }

    @Test
    @DisplayName("add: 正常系 - Podcastの追加が呼ばれること")
    public void testAdd() throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("url", "https://example.com/rss");
        body.put("title", "Test Podcast");

        mockMvc.perform(post("/podcast/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());

        verify(podcastService, times(1)).addPodcast("https://example.com/rss", "Test Podcast");
    }

    @Test
    @DisplayName("sync: 正常系 - 同期処理が呼ばれること")
    public void testSync() throws Exception {
        mockMvc.perform(post("/podcast/sync"))
                .andExpect(status().isOk());

        verify(podcastService, times(1)).sync();
    }

    @Test
    @DisplayName("getNotReadFeed: 正常系 - 未読フィードリストを返すこと")
    public void testGetNotReadFeed() throws Exception {
        PodcastFeed feed = new PodcastFeed(); // プロパティがある場合は適切にセット
        List<PodcastFeed> expectedList = List.of(feed);

        when(podcastService.getNotReadFeed()).thenReturn(expectedList);

        mockMvc.perform(post("/podcast/notReadFeed"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));

        verify(podcastService, times(1)).getNotReadFeed();
    }

    @Test
    @DisplayName("markRead: 正常系 - 既読処理が行われ更新件数を返すこと")
    public void testMarkRead() throws Exception {
        Map<String, String> body = Map.of("url", "https://example.com/feed");
        when(podcastService.markRead("https://example.com/feed")).thenReturn(1);

        mockMvc.perform(post("/podcast/markRead")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        verify(podcastService, times(1)).markRead("https://example.com/feed");
    }

    @Test
    @DisplayName("markRead: 異常系/境界値 - URLが空の場合でもエラーにならないこと")
    public void testMarkRead_EmptyUrl() throws Exception {
        Map<String, String> body = new HashMap<>(); // 空のMap

        mockMvc.perform(post("/podcast/markRead")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());

        verify(podcastService, times(1)).markRead("");
    }
}