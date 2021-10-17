package com.galewings.dto;

import java.io.Serializable;

/**
 * 追加フィード情報
 */
public class AddFeedDto implements Serializable {

    /**
     * リンク
     */
    private String link;

    /**
     * リンク取得
     *
     * @return リンク
     */
    public String getLink() {
        return link;
    }

    /**
     * リンク設定
     *
     * @param link リンク
     */
    public void setLink(String link) {
        this.link = link;
    }

}
