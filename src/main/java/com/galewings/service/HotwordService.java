package com.galewings.service;

import com.galewings.dto.TokenizerAttributeDto;
import com.galewings.repository.FeedRepository;
import java.io.IOException;
import java.io.StringReader;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.ja.JapaneseTokenizer;
import org.apache.lucene.analysis.ja.tokenattributes.BaseFormAttribute;
import org.apache.lucene.analysis.ja.tokenattributes.InflectionAttribute;
import org.apache.lucene.analysis.ja.tokenattributes.PartOfSpeechAttribute;
import org.apache.lucene.analysis.ja.tokenattributes.ReadingAttribute;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HotwordService {

  private final JapaneseTokenizer tokenizer = new JapaneseTokenizer(null, false,
      JapaneseTokenizer.Mode.NORMAL);

  @Autowired
  private FeedRepository feedRepository;

  enum FromDate {
    ONE_HOUR_AGO {
      @Override
      Date getDate() {
        return FromDate.toDate(LocalDateTime.now().minusHours(1));
      }
    },
    HALF_DAY_AGO {
      @Override
      Date getDate() {
        return FromDate.toDate(LocalDateTime.now().minusHours(12));
      }
    },
    ONE_DAY_AGO {
      @Override
      Date getDate() {
        return FromDate.toDate(LocalDateTime.now().minusDays(1));
      }
    },
    THREE_DAY_AGO {
      @Override
      Date getDate() {
        return FromDate.toDate(LocalDateTime.now().minusDays(3));
      }
    },
    ONE_WEEK_AGO {
      @Override
      Date getDate() {
        return FromDate.toDate(LocalDateTime.now().minusWeeks(1));
      }
    },
    ;

    abstract Date getDate();

    private static Date toDate(LocalDateTime localDateTime) {
      ZoneId zone = ZoneId.systemDefault();
      ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, zone);
      Instant instant = zonedDateTime.toInstant();
      return Date.from(instant);
    }
  }

  /**
   * ホットワード算出
   *
   * @param fromDate 日付情報
   * @return 名詞出現カウントマップ
   */
  public Map<String, Long> hotword(FromDate fromDate) {
    return feedRepository.selectPublicDateFrom(fromDate.getDate()).stream()
        .map(feed -> feed.title)
        .flatMap(title -> tokenizerStream(title))
        .filter(tokenizer -> StringUtils.contains(tokenizer.partOfSpeech, "名詞"))
        .map(tokenizer -> tokenizer.term)
        .collect(Collectors.groupingBy(String::toString, Collectors.counting()));
  }

  /**
   * 形態素解析結果をStream形式で返す
   *
   * @param target 形態素解析対象文字列
   * @return 解析結果
   */
  private Stream<TokenizerAttributeDto> tokenizerStream(String target) {
    try {
      tokenizer.setReader(new StringReader(target));
      tokenizer.reset();
      CharTermAttribute term = tokenizer.addAttribute(CharTermAttribute.class);
      OffsetAttribute offset = tokenizer.addAttribute(OffsetAttribute.class);
      PartOfSpeechAttribute partOfSpeech = tokenizer.addAttribute(PartOfSpeechAttribute.class);
      InflectionAttribute inflection = tokenizer.addAttribute(InflectionAttribute.class);
      BaseFormAttribute baseForm = tokenizer.addAttribute(BaseFormAttribute.class);
      ReadingAttribute reading = tokenizer.addAttribute(ReadingAttribute.class);

      List<TokenizerAttributeDto> tokenizerAttributeDtoList = new ArrayList<>();
      while (tokenizer.incrementToken()) {
        TokenizerAttributeDto attributeDto = new TokenizerAttributeDto();
        attributeDto.term = term.toString();
        attributeDto.offset = offset.startOffset();
        attributeDto.offset = offset.startOffset();
        attributeDto.partOfSpeech = partOfSpeech.getPartOfSpeech();
        attributeDto.inflectionType = inflection.getInflectionType();
        attributeDto.inflectionForm = inflection.getInflectionForm();
        attributeDto.baseForm = baseForm.getBaseForm();
        attributeDto.reading = reading.getReading();
        attributeDto.pronunciation = reading.getPronunciation();
        tokenizerAttributeDtoList.add(attributeDto);
      }
      tokenizer.close();
      return tokenizerAttributeDtoList.stream();
    } catch (IOException e) {
      e.printStackTrace();
      List<TokenizerAttributeDto> tokenizerAttributeDtoList = Collections.emptyList();
      return tokenizerAttributeDtoList.stream();
    }

  }
}
