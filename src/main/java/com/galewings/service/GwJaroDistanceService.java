package com.galewings.service;

import com.galewings.entity.Feed;
import com.galewings.repository.FeedRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.spell.JaroWinklerDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

@Component
public class GwJaroDistanceService {


  private final float jaroWinklerThreshold = 0.8f;

  @Autowired
  private FeedRepository feedRepository;

  /**
   * 類似タイトル抽出（ジャロ・ウィンクラー距離）
   */
  @ResponseBody
  public List<Feed> resembleTitleForJaroWinkler(String title) {
    return resembleTitleForJaroWinkler(title, feedRepository.getAllFeed());
  }

  /**
   * 類似タイトル抽出（ジャロ・ウィンクラー距離）
   *
   * @param title    タイトル
   * @param feedList フィードリスト
   * @return 類似フィードリスト
   */
  public List<Feed> resembleTitleForJaroWinkler(String title, List<Feed> feedList) {

    JaroWinklerDistance jaroWinklerDistance = new JaroWinklerDistance();

    return feedList.parallelStream()
        .filter(feed -> StringUtils.isNotEmpty(feed.title) && !feed.title.equals(title))
        .filter(feed -> jaroWinklerThreshold <= jaroWinklerDistance.getDistance(feed.title, title))
        .collect(Collectors.toList());
  }
}
