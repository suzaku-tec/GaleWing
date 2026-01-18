package com.galewings.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class GwDateServiceTest {

    @InjectMocks
    private GwDateService gwDateService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        // デフォルト値の設定
        ReflectionTestUtils.setField(gwDateService, "daysRetained", "0");
    }

    // --- checkFormatDate ---

    @Test
    @DisplayName("正常な日付形式の場合にtrueを返すこと")
    void testCheckFormatDate_Valid() {
        boolean result = gwDateService.checkFormatDate("2000-01-01", GwDateService.DateFormat.SQLITE_DATE_FORMAT);
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("不正な日付形式の場合にfalseを返すこと")
    void testCheckFormatDate_Invalid() {
        // 存在しない日付 (STRICTモードの確認)
        boolean result = gwDateService.checkFormatDate("2000-02-30", GwDateService.DateFormat.SQLITE_DATE_FORMAT);
        Assertions.assertFalse(result);
    }

    // --- now / retainedDate ---

    @Test
    void testNow() {
        LocalDate testDate = LocalDate.of(2023, 3, 8);
        try (MockedStatic<LocalDate> mock = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
            mock.when(LocalDate::now).thenReturn(testDate);
            LocalDate result = gwDateService.now();
            Assertions.assertEquals(testDate, result);
        }
    }

    @ParameterizedTest
    @CsvSource({
            "10, 2023-03-08, 2023-02-26", // 10日前
            "0,  2023-03-08, 2023-03-08", // 当日
            ",   2023-03-08, 2023-03-08"  // null/空文字
    })
    @DisplayName("保持期間に応じた日付計算が正しいこと")
    void testRetainedDate(String days, String now, String expected) {
        ReflectionTestUtils.setField(gwDateService, "daysRetained", days);
        LocalDate fixedNow = LocalDate.parse(now);

        try (MockedStatic<LocalDate> mock = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
            mock.when(LocalDate::now).thenReturn(fixedNow);

            LocalDate result = gwDateService.retainedDate();
            Assertions.assertEquals(LocalDate.parse(expected), result);
        }
    }

    // --- isRetainedDateAfter ---

    @Test
    @DisplayName("保持期限内の日付判定（境界値含む）")
    void testIsRetainedDateAfter() {
        ReflectionTestUtils.setField(gwDateService, "daysRetained", "7");
        LocalDate mockNow = LocalDate.of(2023, 3, 10); // 保持期限: 2023-03-03

        try (MockedStatic<LocalDate> mock = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
            mock.when(LocalDate::now).thenReturn(mockNow);

            // 期限より後の日付 (2023-03-04)
            Assertions.assertTrue(gwDateService.isRetainedDateAfter("2023-03-04 00:00:00"));
            // 期限ちょうどのどの日付 (2023-03-03)
            Assertions.assertTrue(gwDateService.isRetainedDateAfter("2023-03-03 00:00:00"));
            // 期限より前の日付 (2023-03-02)
            Assertions.assertFalse(gwDateService.isRetainedDateAfter("2023-03-02 23:59:59"));
        }
    }

    // --- isToday ---

    @Test
    @DisplayName("指定した文字列が今日と同じ日付か判定できること")
    void testIsToday() {
        LocalDate mockNow = LocalDate.of(2023, 3, 8);
        try (MockedStatic<LocalDate> mock = Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {
            mock.when(LocalDate::now).thenReturn(mockNow);

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            Assertions.assertTrue(gwDateService.isToday("2023/03/08", fmt));
            Assertions.assertFalse(gwDateService.isToday("2023/03/09", fmt));
        }
    }

    // --- convertDateFormat ---

    @Test
    @DisplayName("日付文字列のフォーマット変換ができること")
    void testConvertDateFormat() {
        String result = gwDateService.convertDateFormat("2023-03-08", "yyyy-MM-dd", "yyyyMMdd");
        Assertions.assertEquals("20230308", result);
    }

    // --- isTargetDate ---

    @Test
    @DisplayName("LocalDateと文字列日付の比較ができること")
    void testIsTargetDate() {
        LocalDate target = LocalDate.of(2023, 3, 8);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");

        Assertions.assertTrue(gwDateService.isTargetDate(target, "20230308", fmt));
        Assertions.assertFalse(gwDateService.isTargetDate(target, "20230309", fmt));
    }
}