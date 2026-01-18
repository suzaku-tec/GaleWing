package com.galewings.service;

import com.galewings.dto.statistics.ReadRateDto;
import com.galewings.dto.statistics.ranking.RankDto;
import com.galewings.dto.statistics.ranking.RankingDto;
import com.galewings.repository.StaticsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class StaticsServiceTest {

    @Mock
    StaticsRepository staticsRepository;

    @Mock
    GwDateService gwDateService; // 追加: 日付サービスもモック化

    @InjectMocks
    StaticsService staticsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("既読率取得のテスト")
    void testSelectReadRate() {
        ReadRateDto testDto = new ReadRateDto();
        when(staticsRepository.selectReadRate()).thenReturn(testDto);

        ReadRateDto result = staticsService.selectReadRate();
        Assertions.assertEquals(testDto, result);
    }

    @Test
    @DisplayName("ワードランキング取得のテスト（正常系：データあり）")
    void testSelectWordRank() {
        // --- モックの設定 ---
        // 1. 日付の制御
        LocalDate mockNow = LocalDate.of(2024, 1, 10);
        // GwDateServiceの振る舞いをシミュレート
        when(gwDateService.now()).thenReturn(mockNow);
        // minusDaysの連鎖があるため、それも考慮（実装に合わせて調整）
        // ※gwDateService.now()が毎回新しいインスタンスを返す想定なら以下のように設定
        when(gwDateService.now().minusDays(7)).thenReturn(mockNow.minusDays(7));

        // 2. リポジトリの戻り値作成
        RankDto rank1 = new RankDto();
        rank1.label = "2024-01-01";
        rank1.data = "WordA";
        rank1.rank = 1;

        RankDto rank2 = new RankDto();
        rank2.label = "2024-01-01";
        rank2.data = "WordA"; // 同じデータ(key)でグルーピングされる
        rank2.rank = 11;      // 10より大きいためnullになるはず

        when(staticsRepository.selectWordRank(anyString(), anyString()))
                .thenReturn(List.of(rank1, rank2));

        // --- 実行 ---
        RankingDto result = staticsService.selectWordRank();

        // --- 検証 ---
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.labels.size(), "ラベルの重複排除が機能していること");
        Assertions.assertEquals(1, result.datasets.size(), "データセットがWordAの1つにまとめられていること");

        // データ変換ロジックの検証（10を超えたらnull）
        List<Integer> dataPoints = result.datasets.get(0).data;
        Assertions.assertEquals(1, dataPoints.get(0));
        Assertions.assertNull(dataPoints.get(1), "順位が10を超える場合はnullになること");

        // 色の設定検証
        Assertions.assertNotNull(result.datasets.get(0).borderColor);
        Assertions.assertEquals(result.datasets.get(0).borderColor, result.datasets.get(0).backgroundColor);
    }

    @Test
    @DisplayName("ワードランキング取得のテスト（空リストの場合）")
    void testSelectWordRank_Empty() {
        // 日付モック
        LocalDate mockNow = LocalDate.of(2024, 1, 10);
        when(gwDateService.now()).thenReturn(mockNow);

        // 空リストを返す
        when(staticsRepository.selectWordRank(anyString(), anyString())).thenReturn(Collections.emptyList());

        RankingDto result = staticsService.selectWordRank();

        Assertions.assertTrue(result.labels.isEmpty());
        Assertions.assertTrue(result.datasets.isEmpty());
    }
}