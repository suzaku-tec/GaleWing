package com.galewings.service;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import static com.galewings.service.GwDateService.DateFormat.SQLITE_DATE_FORMAT;

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
        SQLITE_DATE_FORMAT("yyyy-MM-dd", "uuuu-MM-dd"), FILE_TIME_FORMAT("yyyyMMddHHmmss", "uuuuMMddHHmmss"), DATE_TIME_COMMON("yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd hh:mm:ss");

        public final String stringFormat;

        public final DateTimeFormatter dtf;

        public final SimpleDateFormat sdf;

        DateFormat(String format, String dateTimeFormat) {
            this.stringFormat = format;
            this.dtf = DateTimeFormatter.ofPattern(dateTimeFormat);
            this.sdf = new SimpleDateFormat(stringFormat);
        }
    }

    public boolean isToday(String dateStr, DateTimeFormatter format) {
        String nowDate = now().format(SQLITE_DATE_FORMAT.dtf);

        String compareDateStr = LocalDate.parse(dateStr, format).format(SQLITE_DATE_FORMAT.dtf);
        return nowDate.equals(compareDateStr);
    }

    public String convertDateFormat(String dateStr, String beforFormat, String afterFormat) {
        DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern(beforFormat);
        LocalDate date = LocalDate.parse(dateStr, dtFormat);
        DateTimeFormatter dtFormatAfter = DateTimeFormatter.ofPattern(afterFormat);
        return date.format(dtFormatAfter);
    }

    public boolean isTargetDate(LocalDate targetDate, String comparDate, DateTimeFormatter format) {
        String target = targetDate.format(SQLITE_DATE_FORMAT.dtf);
        String compareDateStr = LocalDate.parse(comparDate, format).format(SQLITE_DATE_FORMAT.dtf);
        return target.equals(compareDateStr);
    }

}
