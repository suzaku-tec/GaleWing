package com.galewings.dto.output;

import com.galewings.entity.Feed;
import com.galewings.entity.SiteFeedCount;

import java.util.List;

/**
 * フィード更新
 */
public class FeedUpdate {

    /**
     * フィード情報
     */
    public List<Feed> feeds;

    /**
     * サイトフィード件数
     */
    public List<SiteFeedCount> siteFeedCounts;
}
