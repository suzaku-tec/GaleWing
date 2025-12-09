package com.galewings.task.bat;

import com.galewings.entity.Feed;
import com.galewings.repository.FeedGroupingRepository;
import com.galewings.repository.FeedRepository;
import com.galewings.util.stream.Result;
import com.github.pemistahl.lingua.api.Language;
import com.github.pemistahl.lingua.api.LanguageDetector;
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ja.JapaneseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class FeedGroupingTask3 implements Runnable {

    // 類似度検索の対象となるフィールド名
    private static final String FIELD_CONTENT = "content";

    private final FeedRepository feedRepository;

    private final FeedGroupingRepository feedGroupingRepository;

    private static final Logger logger = LoggerFactory.getLogger(FeedGroupingTask3.class);

    // LanguageDetectorの構築はリソースを消費するため、一度だけ行うのがベストです。
    private static final LanguageDetector detector = LanguageDetectorBuilder.fromLanguages(
                    Language.JAPANESE,
                    Language.ENGLISH
            )
            // 検出精度を高めるための設定を追加することも可能です (Optional)
            .withPreloadedLanguageModels() // モデルをすぐにロードする
            .build();

    @Autowired
    public FeedGroupingTask3(FeedRepository feedRepository, FeedGroupingRepository feedGroupingRepository) {
        this.feedRepository = feedRepository;
        this.feedGroupingRepository = feedGroupingRepository;
    }

    @Override
    public void run() {

        feedGroupingRepository.allDelete();

        List<Feed> allList = feedRepository.getAllFeed();
        List<String> list = allList.stream().filter(feed -> {
            String title = feed.getTitle();
            Language lang = detector.detectLanguageOf(title);
            return lang == Language.JAPANESE;
        }).map(feed -> feed.title).toList();

        // 1. インデックスの準備 (メモリ上にインデックスを作成) 💾
        try (Directory directory = new RAMDirectory()) {

            Analyzer analyzer = new JapaneseAnalyzer();

            createIndex(directory, analyzer, list);

            // 2. IndexSearcherの準備 🔍
            try (DirectoryReader reader = DirectoryReader.open(directory)) {
                IndexSearcher searcher = new IndexSearcher(reader);

                // 3. MoreLikeThis (MLT) オブジェクトの準備と設定 ⚙️
                MoreLikeThis mlt = createMoreLikeThis(searcher, analyzer);

                // 4. 任意のテキスト文字列による類似度検索の実行
                allList.stream().map(feed -> Result.runCatching(() -> searchSimilarDocuments(searcher, mlt, feed.title, feed, allList)))
                        .filter(Result::isFailure)
                        .findFirst()
                        .ifPresent(result -> logger.error("類似度検索中にエラーが発生しました。", result.getException()));

            }

        } catch (IOException e) {
            logger.error("", e);
        }
    }


    /**
     * インデックスを作成し、テスト用のドキュメントを追加します。
     */
    private void createIndex(Directory directory, Analyzer analyzer, List<String> list) throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        try (IndexWriter writer = new IndexWriter(directory, config)) {

            for (String text : list) {
                Document doc = new Document();
                doc.add(new TextField(FIELD_CONTENT, text, Field.Store.YES));
                writer.addDocument(doc);
            }
            writer.commit();
            logger.info("✅ インデックスの作成とドキュメントの追加が完了しました。");
        }
    }

    /**
     * MoreLikeThisオブジェクトを初期化し、必要なパラメータを設定します。
     */
    private MoreLikeThis createMoreLikeThis(IndexSearcher searcher, Analyzer analyzer) throws IOException {
        MoreLikeThis mlt = new MoreLikeThis(searcher.getIndexReader());

        // ★ インデックス作成時と同じアナライザーを設定 (日本語解析に必須)
        mlt.setAnalyzer(analyzer);

        // 類似度検索の対象フィールドを設定
        mlt.setFieldNames(new String[]{FIELD_CONTENT});

        // パラメータ設定 (適宜調整してください)
        mlt.setMinTermFreq(1);      // ドキュメント内での単語の最小出現頻度
        mlt.setMinDocFreq(1);       // インデックス全体での単語の最小ドキュメント頻度
        mlt.setMaxQueryTerms(15);   // MLTクエリに含める単語の最大数

        logger.info("MoreLikeThisオブジェクトの準備が完了しました。");
        return mlt;
    }

    /**
     * 指定されたテキスト文字列に類似したドキュメントを検索し、結果を出力します。
     */
    private void searchSimilarDocuments(
            IndexSearcher searcher,
            MoreLikeThis mlt,
            String rawTextToSearch, Feed tergetFeed, List<Feed> allList) throws IOException {

        logger.info("--- 類似度検索を実行 ---");
        logger.info("ターゲットテキスト: {}", rawTextToSearch);

        // 1. テキストデータをStringReaderに変換
        try (StringReader reader = new StringReader(rawTextToSearch)) {

            // 2. MLTクエリを生成 (生のテキストを使用)
            Query query = mlt.like(FIELD_CONTENT, reader);

            // 3. クエリの実行
            TopDocs hits = searcher.search(query, 3); // 上位5件を取得

            // 4. 結果の処理
            logger.info("--- 検索結果 (スコア順) ---");
            if (hits.totalHits.value == 0) {
                logger.info("類似文書は見つかりませんでした。");
                return;
            }

            for (ScoreDoc hit : hits.scoreDocs) {
                Document similarDoc = searcher.doc(hit.doc);

                if (10.0f <= hit.score) {
                    allList.stream().filter(feed -> feed.title.equals(similarDoc.get(FIELD_CONTENT)))
                            .findFirst().ifPresent(feed -> {
                                feedGroupingRepository.insert(tergetFeed.uuid, feed.uuid, hit.score);
                            });
                }

                String text = Optional.of(similarDoc.get(FIELD_CONTENT)).orElse(StringUtils.EMPTY);
                logger.info("[ID: {}] スコア: {}", hit.doc, hit.score);
                logger.info("内容: {}", text);
            }
        }
    }
}

