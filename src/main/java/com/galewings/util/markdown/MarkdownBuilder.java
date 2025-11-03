package com.galewings.util.markdown;

import org.apache.commons.lang3.StringUtils;

import java.util.StringJoiner;
import java.util.stream.Collector;

public class MarkdownBuilder {

    private enum MarkdownStr {
        list("- "),
        lineBreak("  "),
        ;

        public final String str;

        MarkdownStr(String str) {
            this.str = str;
        }

        @Override
        public String toString() {
            return str;
        }

        public enum Link {
            altStart("["),
            altEnd("]"),
            urlStart("("),
            urlEnd(")");

            public final String str;

            Link(String str) {
                this.str = str;
            }

            @Override
            public String toString() {
                return str;
            }
        }
    }

    private final StringBuilder sb;

    private MarkdownBuilder() {
        sb = new StringBuilder();
    }

    public static MarkdownBuilder create() {
        return new MarkdownBuilder();
    }

    public MarkdownBuilder link(String altTitle, String title, String url) {
        sb.append(MarkdownStr.Link.altStart).append(altTitle).append(MarkdownStr.Link.altEnd).append(MarkdownStr.Link.urlStart).append(url).append(StringUtils.SPACE).append(title).append(MarkdownStr.Link.urlEnd);
        return this;
    }

    public MarkdownBuilder link(String title, String url) {
        sb.append(MarkdownStr.Link.altStart).append(title).append(MarkdownStr.Link.altEnd).append(MarkdownStr.Link.urlStart).append(url).append(MarkdownStr.Link.urlEnd);
        return this;
    }

    /**
     *
     * @return
     */
    public MarkdownBuilder newline() {
        sb.append(System.lineSeparator());
        return this;
    }

    public MarkdownBuilder list() {
        sb.append(MarkdownStr.list);
        return this;
    }

    public String build() {
        return sb.toString();
    }

    public static Collector<String, StringJoiner, String> lineBreaks() {

        return Collector.of(
                () -> new StringJoiner("  "),                // supplier: 空白2つ区切りのStringJoiner生成
                StringJoiner::add,                           // accumulator: 要素をStringJoinerに追加
                StringJoiner::merge,                         // combiner: 並列処理時の結合方法
                StringJoiner::toString,                      // finisher: StringJoinerを文字列に変換
                Collector.Characteristics.UNORDERED          // characteristics: 順序を問わない
        );
    }

    public static Collector<String, StringJoiner, String> paragraph() {
        return Collector.of(
                () -> new StringJoiner(System.lineSeparator() + System.lineSeparator()),                // supplier: 空行
                StringJoiner::add,                           // accumulator: 要素をStringJoinerに追加
                StringJoiner::merge,                         // combiner: 並列処理時の結合方法
                StringJoiner::toString,                      // finisher: StringJoinerを文字列に変換
                Collector.Characteristics.UNORDERED          // characteristics: 順序を問わない
        );
    }

}
