package com.galewings.service;

import com.google.common.base.Strings;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GwDateService {

  @Value("${days.retained}")
  private String daysRetained;

  public boolean checkFormatDate(String dateText, DateFormat df) {
    SimpleDateFormat sdf = new SimpleDateFormat(df.format);
    sdf.setLenient(false);

    try {
      sdf.parse(dateText);
      String formatDate = sdf.format(dateText);

      // 適応させた書式と同じ文字列化判定
      return dateText.equals(formatDate);
    } catch (ParseException e) {
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
    DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

    LocalDate date = LocalDate.parse(targetDate, dtFormat);
    return !retainedDate().isAfter(date);
  }

  public enum DateFormat {
    SQLITE_DATE_FORMAT("yyyy-MM-dd");

    public final String format;

    DateFormat(String format) {
      this.format = format;
    }
  }
}
