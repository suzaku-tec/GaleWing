package com.galewings.dto.output;

import com.miragesql.miragesql.annotation.Column;

public class YoutubeListSelectChannel {

  @Column(name = "channelId")
  public String channelId;

  @Column(name = "name")
  public String name;

  public String getChannelId() {
    return channelId;
  }

  public String getName() {
    return name;
  }

}
