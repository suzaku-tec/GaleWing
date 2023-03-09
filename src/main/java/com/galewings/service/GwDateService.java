package com.galewings.service;

import com.google.common.base.Strings;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GwDateService {

  @Value("${days.retained}")
  private String daysRetained;

  public boolean checkFormatDate(String dateText, DateFormat df) {
    try {
      LocalDate.parse(dateText,
          df.dtf.withResolverStyle(ResolverStyle.STRICT));

      // 適応させた書式と同じ文字列化判定
      return true;
    } catch (DateTimeParseException e) {
      // 日付変換できなかったらエラー
      return false;
    }
  }

  public LocalDate now() {
    return LocalDate.now();
  }

  public LocalDate retainedDate() {
    return now().minusDays(
        Strings.isNullOrEmpty(daysRetained) ? 0 : Integer.parseInt(daysRetained));
  }

  public boolean isRetainedDateAfter(String targetDate) {
    DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");

    LocalDate date = LocalDate.parse(targetDate, dtFormat);
    LocalDate retainedDate = retainedDate();
    return !retainedDate.isAfter(date);
  }
  
  public enum DateFormat {
    SQLITE_DATE_FORMAT("yyyy-MM-dd", "uuuu-MM-dd");

    public final String stringFormat;

    public final DateTimeFormatter dtf;

    DateFormat(String format, String dateTimeFormat) {
      this.stringFormat = format;
      this.dtf = DateTimeFormatter.ofPattern(dateTimeFormat);
    }
  }
}
