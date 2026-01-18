package com.galewings.task.bat;

import com.galewings.entity.Feed;
import com.galewings.repository.FeedRepository;
import com.galewings.repository.TrendRepository;
import com.galewings.service.GwDateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExportTrendTaskTest {

    @Mock
    private FeedRepository feedRepository;

    @Mock
    private GwDateService gwDateService;

    @Mock
    private TrendRepository trendRepository;

    @InjectMocks
    private ExportTrendTask exportTrendTask;

    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
    }

    @Test
    @DisplayName("正常系：5回以上出現する名詞が正しくトレンドに登録されること")
    void testRun_Success() {
        // GIVEN: 5回以上出現する単語（"Java"）を含むフィードを準備
        Feed feed = new Feed();
        feed.publishedDate = "2023-10-27 10:00:00";
        feed.title = "Javaのテスト テスト テスト テスト テストをする"; // "Java"が5回

        when(feedRepository.getAllFeed()).thenReturn(List.of(feed));
        when(gwDateService.isToday(eq(feed.publishedDate), any())).thenReturn(true);
        when(gwDateService.now()).thenReturn(LocalDate.parse("2023-10-27"));
        when(trendRepository.insert(any(), any(), any())).thenReturn(1);

        // WHEN
        exportTrendTask.run();

        // THEN: Javaが1回インサートされることを確認（出現数5）
        verify(trendRepository, times(1)).insert(any(), any(), any());
    }

    @Test
    @DisplayName("境界値：出現回数が5回未満の単語は登録されないこと")
    void testRun_UnderThreshold() {
        // GIVEN: 4回しか出現しない単語
        Feed feed = new Feed();
        feed.publishedDate = "2023-10-27 10:00:00";
        feed.title = "Spring"; // 4回

        when(feedRepository.getAllFeed()).thenReturn(List.of(feed));
        when(gwDateService.isToday(any(), any())).thenReturn(true);

        // WHEN
        exportTrendTask.run();

        // THEN: 5回未満なのでinsertは呼ばれない
        verify(trendRepository, never()).insert(any(), any(), anyLong());
    }

    @Test
    @DisplayName("フィルタリング：今日以外のフィードは処理対象外となること")
    void testRun_NotToday() {
        // GIVEN: 今日ではない日付のフィード
        Feed feed = new Feed();
        feed.publishedDate = "2000-01-01 00:00:00";
        feed.title = "Java";

        when(feedRepository.getAllFeed()).thenReturn(List.of(feed));
        when(gwDateService.isToday(any(), any())).thenReturn(false);

        // WHEN
        exportTrendTask.run();

        // THEN: isTodayがfalseのため、tokenize以降の処理に流れない
        verify(trendRepository, never()).insert(any(), any(), anyLong());
    }

    @Test
    @DisplayName("異常系：フィードが空の場合は何も起きないこと")
    void testRun_EmptyFeed() {
        // GIVEN
        when(feedRepository.getAllFeed()).thenReturn(Collections.emptyList());

        // WHEN
        exportTrendTask.run();

        // THEN
        verify(trendRepository, never()).insert(any(), any(), anyLong());
    }
}