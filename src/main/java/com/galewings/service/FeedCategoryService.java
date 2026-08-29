package com.galewings.service;

import com.galewings.entity.Feed;
import com.galewings.repository.FeedRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * FeedCategoryService
 */
@Service
public class FeedCategoryService {

    private final FeedRepository feedRepository;

    public FeedCategoryService(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    /**
     * カテゴリIDに基づいてフィードを取得する
     *
     * @param categoryId カテゴリID
     * @return カテゴリに関連するフィードのリスト
     */
    public List<Feed> getFeedCategories(String categoryId) {

        return feedRepository.findByCategoryId(categoryId).stream()
                .map(f -> {
                    if (!Objects.isNull(f.translateTitle)) {
                        f.title = "【翻訳】" +
                                f.translateTitle;
                    }
                    return f;
                }).toList();
    }
}
