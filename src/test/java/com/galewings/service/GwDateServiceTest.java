package com.galewings.service;

import java.time.LocalDate;
import java.time.Month;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

class GwDateServiceTest {

  @InjectMocks
  private GwDateService gwDateService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCheckFormatDate() {
    boolean result = gwDateService.checkFormatDate("2000-01-01",
        GwDateService.DateFormat.SQLITE_DATE_FORMAT);
    Assertions.assertTrue(result);
  }

  @Test
  void testNow() {
    LocalDate testDate = LocalDate.of(2023, 3, 8);
    try (MockedStatic<LocalDate> mock = Mockito.mockStatic(LocalDate.class,
        Mockito.CALLS_REAL_METHODS)) {
      mock.when(LocalDate::now).thenReturn(testDate);
      LocalDate result = gwDateService.now();
      Assertions.assertEquals(LocalDate.of(2023, Month.MARCH, 8), result);
    }

  }

  @Test
  void testRetainedDate() {
    LocalDate testDate = LocalDate.of(2023, 3, 8);
    try (MockedStatic<LocalDate> mock = Mockito.mockStatic(LocalDate.class,
        Mockito.CALLS_REAL_METHODS)) {
      mock.when(LocalDate::now).thenReturn(testDate);
      LocalDate result = gwDateService.retainedDate();
      Assertions.assertEquals(LocalDate.of(2023, Month.MARCH, 8), result);
    }

  }

  @Test
  void testIsRetainedDateAfter() {
    ReflectionTestUtils.setField(gwDateService, "daysRetained", "1",
        String.class);
    LocalDate testDate = LocalDate.of(2000, 1, 12);
    try (MockedStatic<LocalDate> mock = Mockito.mockStatic(LocalDate.class,
        Mockito.CALLS_REAL_METHODS)) {
      mock.when(LocalDate::now).thenReturn(testDate);
      boolean result = gwDateService.isRetainedDateAfter("2000-01-12 23:34:45");
      Assertions.assertTrue(result);
    }

  }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme