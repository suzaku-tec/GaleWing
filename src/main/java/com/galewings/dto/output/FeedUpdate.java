package com.galewings.dto.output;

import com.galewings.entity.Feed;
import com.galewings.entity.SiteFeedCount;
import java.io.Serializable;
import java.util.List;

public class FeedUpdate implements Serializable {

  public List<Feed> feeds;

  public List<SiteFeedCount> siteFeedCounts;
}
