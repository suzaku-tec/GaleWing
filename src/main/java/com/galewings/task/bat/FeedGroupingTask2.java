package com.galewings.task.bat;

import com.galewings.entity.Feed;
import com.galewings.repository.FeedGroupingRepository;
import com.galewings.repository.FeedRepository;
import jakarta.transaction.Transactional;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
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
public class FeedGroupingTask2 implements Runnable {

    private final FeedRepository feedRepository;

    private final FeedGroupingRepository feedGroupingRepository;

    @Autowired
    public FeedGroupingTask2(FeedRepository feedRepository, FeedGroupingRepository feedGroupingRepository) {
        this.feedRepository = feedRepository;
        this.feedGroupingRepository = feedGroupingRepository;
    }

    @Override
    public void run() {
        List<Feed> allFeed = feedRepository.getAllFeed();
        String[] titles = allFeed.stream().map(Feed::getTitle).toArray(String[]::new);

        // メモリ上のインデックスディレクトリを作成（ByteBuffersDirectory）
        Directory dir = new ByteBuffersDirectory();
        try {
            // 標準分析器（英語圏などの簡易分析）
            StandardAnalyzer analyzer = new StandardAnalyzer();

            // インデックスライターコンフィグ設定
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            try (IndexWriter writer = new IndexWriter(dir, config)) {
                // インデックス登録
                for (int i = 0; i < titles.length; i++) {
                    Document doc = new Document();
                    doc.add(new TextField("title", titles[i], Field.Store.YES));
                    writer.addDocument(doc);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // インデックスリーダーとサーチャー作成
            IndexReader reader = DirectoryReader.open(dir);
            IndexSearcher searcher = new IndexSearcher(reader);

            // MoreLikeThisによる類似文書検索の準備
            MoreLikeThis mlt = new MoreLikeThis(reader);
            mlt.setFieldNames(new String[]{"title"});
            mlt.setAnalyzer(analyzer);
            mlt.setMinTermFreq(1);
            mlt.setMinDocFreq(1);

            for (int i = 0; i < 2; i++) {
                Feed baseFeed = allFeed.get(i + 100);

                // 類似度の高い上位3件抽出
                StringReader sr = new StringReader(baseFeed.getTitle());
                Query query = mlt.like("title", sr);
                TopDocs topDocs = searcher.search(query, 3);

                for (ScoreDoc sd : topDocs.scoreDocs) {
                    Document d = searcher.doc(sd.doc);

                    System.out.println("Base Feed: " + baseFeed.getTitle());
                    System.out.println("Similar Feed: " + d.get("title"));
                    System.out.println("Score: " + sd.score);
                    System.out.println("-----------------------------");
                }

                sr.close();
            }
            reader.close();
            dir.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (dir != null) {
                    dir.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

