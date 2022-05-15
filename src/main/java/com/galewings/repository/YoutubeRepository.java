package com.galewings.repository;

import com.galewings.dto.output.YoutubeListSelectChannel;
import com.miragesql.miragesql.ClasspathSqlResource;
import com.miragesql.miragesql.SqlManager;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * youtube系のDB操作を担当する
 * <p>
 * 対象DB
 * <p>
 * ・youtube_list
 */
@Component
public class YoutubeRepository {

  @Autowired
  SqlManager sqlManager;

  @Transactional
  public List<YoutubeListSelectChannel> selectChannelList() {
    return sqlManager.getResultList(YoutubeListSelectChannel.class,
        new ClasspathSqlResource("sql/youtube_list/select_channel_list.sql"));
  }
}
