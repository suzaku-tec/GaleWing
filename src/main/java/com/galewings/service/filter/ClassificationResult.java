package com.galewings.service.filter;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassificationResult {

    private static final int RELATED_CATEGORY_THRESHOLD = 3;

    /**
     * 主要カテゴリ
     */
    public String primaryCategory;

    /**
     * タイトルに関連すると判断されたカテゴリの集合
     * 初期版では関連カテゴリを持ってない
     */
    public Set<String> categories;

    /**
     * 分類結果の確信度
     * <p>
     * 0.0 ~ 1.0 の範囲で表現
     * 0.0  → ほぼ判断できない
     * 0.5  → やや曖昧
     * 0.8  → 比較的確実
     * 1.0  → 非常に確実
     */
    public double confidence;

    /**
     * 一致したルールのIDを保持
     */
    public List<String> matchedRules;

    /**
     * 各カテゴリに何点入ったかを保持
     */
    public Map<String, Integer> scores;

    public ClassificationResult(String primaryCategory, Set<String> categories, double confidence, List<String> matchedRules) {
        this.confidence = confidence;
        this.primaryCategory = primaryCategory;
        this.categories = categories;
        this.matchedRules = matchedRules;
    }

    private ClassificationResult() {
    }

    /**
     * 分類結果を生成する
     *
     * @param scores
     * @return
     */
    public static ClassificationResult from(Map<String, Integer> scores) {
        ClassificationResult result =
                new ClassificationResult();

        if (scores == null || scores.isEmpty()) {
            result.primaryCategory = "other";
            result.categories = Set.of("other");
            result.scores = Map.of();
            result.matchedRules = List.of();
            result.confidence = 0.0;
            return result;
        }

        Map.Entry<String, Integer> entry = scores.entrySet().stream().max(Map.Entry.comparingByKey()).orElseThrow();
        result.primaryCategory = entry.getKey();
        result.categories = scores.entrySet().stream()
                .filter(e -> e.getValue() > RELATED_CATEGORY_THRESHOLD)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        if (result.categories.isEmpty()) {
            result.categories = Set.of("other");
        }
        result.scores = scores;
        result.matchedRules = List.of();
        result.confidence = calculateConfidence(scores);
        return result;
    }

    /**
     * 分類結果の確信度を計算する
     *
     * @param scores
     * @return
     */
    private static double calculateConfidence(
            Map<String, Integer> scores) {

        int maxScore = scores.values()
                .stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);

        return Math.min(maxScore / 10.0, 1.0);
    }
}
