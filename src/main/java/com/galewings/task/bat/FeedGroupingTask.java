package com.galewings.task.bat;

import com.galewings.entity.Feed;
import com.galewings.exception.GaleWingsRuntimeException;
import com.galewings.repository.FeedGroupingRepository;
import com.galewings.repository.FeedRepository;
import jakarta.transaction.Transactional;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ja.JapaneseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

@Component
@Transactional
public class FeedGroupingTask implements Runnable {

    private final FeedRepository feedRepository;

    private final FeedGroupingRepository feedGroupingRepository;

    private static final String ANALYZED_COL_NAME = "title";

    @Autowired
    public FeedGroupingTask(FeedRepository feedRepository, FeedGroupingRepository feedGroupingRepository) {
        this.feedRepository = feedRepository;
        this.feedGroupingRepository = feedGroupingRepository;
    }

    @Override
    public void run() {
        List<Feed> allFeed = feedRepository.getAllFeed();
        String[] titles = allFeed.stream().map(Feed::getTitle).toArray(String[]::new);

        // メモリ上のインデックスディレクトリを作成（ByteBuffersDirectory）
        try (Directory dir = new ByteBuffersDirectory()) {
            // 標準分析器（英語圏などの簡易分析）
            Analyzer analyzer = new JapaneseAnalyzer();

            // インデックスライターコンフィグ設定
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            addIndex(dir, config, titles);

            // インデックスリーダーとサーチャー作成
            IndexReader reader = DirectoryReader.open(dir);
            IndexSearcher searcher = new IndexSearcher(reader);

            // MoreLikeThisによる類似文書検索の準備
            MoreLikeThis mlt = new MoreLikeThis(reader);
            mlt.setFieldNames(new String[]{ANALYZED_COL_NAME});
            mlt.setAnalyzer(analyzer);
            mlt.setMinTermFreq(1);
            mlt.setMinDocFreq(1);

            feedGroupingRepository.allDelete();

            for (int i = 0; i < allFeed.size(); i++) {
                Feed baseFeed = allFeed.get(i);

                // 類似度の高い上位3件抽出
                StringReader sr = new StringReader(baseFeed.getTitle());
                Query query = mlt.like(ANALYZED_COL_NAME, sr);
                TopDocs topDocs = searcher.search(query, 3);

                for (int j = 0; j < topDocs.scoreDocs.length; j++) {
                    ScoreDoc sd = topDocs.scoreDocs[j];
                    Document d = searcher.doc(sd.doc);

                    if (3 < j) {
                        break;
                    }

                    // 自分自身は除外
                    if (!d.get(ANALYZED_COL_NAME).equals(baseFeed.getTitle()) && sd.score > 15f) {
                        allFeed.stream().filter(feed -> feed.title.equals(d.get(ANALYZED_COL_NAME)))
                                .findFirst()
                                .ifPresent(f -> feedGroupingRepository.insert(baseFeed.uuid, f.uuid, sd.score));
                    }
                }

                sr.close();
            }

            reader.close();
        } catch (IOException e) {
            throw new GaleWingsRuntimeException(e);
        }
    }

    /**
     * インデックス登録
     *
     * @param dir    インデックスディレクトリ
     * @param config インデックスライターコンフィグ
     * @param titles 登録するタイトル一覧
     */
    private void addIndex(Directory dir, IndexWriterConfig config, String[] titles) {
        try (IndexWriter writer = new IndexWriter(dir, config)) {
            // インデックス登録
            for (String title : titles) {
                Document doc = new Document();
                doc.add(new TextField(ANALYZED_COL_NAME, title, Field.Store.YES));
                writer.addDocument(doc);
            }
        } catch (IOException e) {
            throw new GaleWingsRuntimeException(e);
        }
    }

}

