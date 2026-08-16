package com.galewings.service.filter;

import com.galewings.entity.Feed;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

class GwRuleBasedNewsClassifierTest {

    private GwRuleBasedNewsClassifier classifier;

    @BeforeEach
    void setUp() {

        ClassificationCategory technology =
                new ClassificationCategory();

        technology.setId("technology");

        ClassificationKeyword ai =
                new ClassificationKeyword();

        ai.setValue("生成ai");
        ai.setWeight(6);

        technology.setKeywords(
                List.of(ai)
        );

        classifier = new GwRuleBasedNewsClassifier();
        classifier.setCategories(
                List.of(technology)
        );
    }

    @Test
    void getCategories() {
        List<ClassificationCategory> categories = classifier.getCategories();

        Assertions.assertEquals(1, categories.size());
        Assertions.assertEquals("technology", categories.get(0).getId());
        Assertions.assertEquals(1, categories.get(0).getKeywords().size());
        Assertions.assertEquals("生成ai", categories.get(0).getKeywords().get(0).getValue());
        Assertions.assertEquals(6, categories.get(0).getKeywords().get(0).getWeight());
    }

    @Test
    void setCategories() {
        classifier.setCategories(List.of());
        Assertions.assertEquals(0, classifier.getCategories().size());
    }

    @Test
    void classify_単一カテゴリに分類される() {
        Feed feed = createTestFeed("企業が生成AIの新サービスを発表");
        ClassificationResult classify = classifier.classify(feed);
        Assertions.assertNotNull(classify);
        Assertions.assertEquals("technology", classify.primaryCategory);
    }

    @Test
    void classify_複数カテゴリに分類される() {

        List<ClassificationCategory> classificationCategory = List.of(
                createClassificationCategory("technology", List.of("生成ai"), 6),
                createClassificationCategory("business", List.of("企業"), 6)
        );

        Feed feed = createTestFeed("企業が生成AIの新サービスを発表");
        classifier.setCategories(classificationCategory);

        ClassificationResult classify = classifier.classify(feed);

        Assertions.assertNotNull(classify);
        Assertions.assertEquals("technology", classify.primaryCategory);
        Assertions.assertTrue(classify.categories.contains("technology"));
        Assertions.assertTrue(classify.categories.contains("business"));
    }

    @Test
    void classify_どのルールにも一致しない() {
        Feed feed = createTestFeed("これは技術に関するニュースではありません");
        ClassificationResult classify = classifier.classify(feed);
        Assertions.assertNotNull(classify);
        Assertions.assertEquals("other", classify.primaryCategory);
    }

    @Test
    void classify_同じカテゴリの複数キーワードに一致する() {
        List<ClassificationCategory> classificationCategory = List.of(
                createClassificationCategory("technology", List.of("生成ai"), 6),
                createClassificationCategory("technology", List.of("サービス"), 5)
        );
        classifier.setCategories(classificationCategory);

        Feed feed = createTestFeed("企業が生成AIの新サービスを発表");
        ClassificationResult classify = classifier.classify(feed);

        Assertions.assertNotNull(classify);
        Assertions.assertEquals("technology", classify.primaryCategory);
        Assertions.assertTrue(classify.categories.contains("technology"));
        Assertions.assertEquals(1, classify.categories.size());
    }

    @Test
    void classify_空タイトルでエラーにならない() {
        ClassificationResult classify = classifier.classify(createTestFeed(""));
        Assertions.assertNotNull(classify);
    }


    private Feed createTestFeed(String title) {
        Feed feed = new Feed();
        feed.title = title;
        return feed;
    }

    /**
     * テスト用のClassificationCategoryオブジェクト作成
     * <p>
     * 重みは一律同じのキーワードリストを保持するClassificationCategoryを作成する
     *
     * @param id          キーワードを紐づけるカテゴリ
     * @param keywordList キーワードリスト
     * @param weight      重み
     * @return ClassificationCategory
     */
    private ClassificationCategory createClassificationCategory(String id, List<String> keywordList, int weight) {
        ClassificationCategory category = new ClassificationCategory();
        category.setId(id);

        List<ClassificationKeyword> classificationKeywords = keywordList.stream().map(keyword -> {
            ClassificationKeyword classificationKeyword = new ClassificationKeyword();
            classificationKeyword.setValue(keyword);
            classificationKeyword.setWeight(weight);
            return classificationKeyword;
        }).collect(Collectors.toList());
        category.setKeywords(classificationKeywords);
        return category;
    }
}