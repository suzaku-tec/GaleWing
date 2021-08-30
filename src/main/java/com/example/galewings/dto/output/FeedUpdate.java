package com.example.galewings.dto.output;

import com.example.galewings.entity.Feed;
import com.example.galewings.entity.SiteFeedCount;
import java.io.Serializable;
import java.util.List;

public class FeedUpdate implements Serializable {

  public List<Feed> feeds;

  public List<SiteFeedCount> siteFeedCounts;
}
