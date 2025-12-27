package com.galewings.dto.rssbridge;

import java.io.Serializable;
import java.util.List;

public class ItemsBean implements Serializable {

    public String id;
    public String title;
    public AuthorBean author;
    public String date_modified;
    public String url;
    public String content_html;
    public RssbridgeBean _rssbridge;
    public List<AttachmentsBean> attachments;


}
