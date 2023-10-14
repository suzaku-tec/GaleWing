package com.galewings.dto.qiita;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class QiitaItemsDto implements Serializable {

    public int ranking;

    @JsonProperty("rendered_body")
    public String renderedBody;

    @JsonProperty("private")
    public boolean jsonMemberPrivate;

    @JsonProperty("organization_url_name")
    public String organizationUrlName;

    @JsonProperty("stocks_count")
    public int stocksCount;

    @JsonProperty("created_at")
    public String createdAt;

    @JsonProperty("team_membership")
    public TeamMembershipDto teamMembership;

    @JsonProperty("body")
    public String body;

    @JsonProperty("title")
    public String title;

    @JsonProperty("url")
    public String url;

    @JsonProperty("tags")
    public List<TagsDto> tags;

    @JsonProperty("likes_count")
    public int likesCount;

    @JsonProperty("updated_at")
    public String updatedAt;

    @JsonProperty("comments_count")
    public int commentsCount;

    @JsonProperty("reactions_count")
    public int reactionsCount;

    @JsonProperty("slide")
    public boolean slide;

    @JsonProperty("page_views_count")
    public int pageViewsCount;

    @JsonProperty("id")
    public String id;

    @JsonProperty("coediting")
    public boolean coediting;

    @JsonProperty("user")
    public UserDto user;

    @JsonProperty("group")
    public GroupDto group;

    @Override
    public String toString() {
        return
                "QiitaItemsDto{" +
                        "rendered_body = '" + renderedBody + '\'' +
                        ",jsonMemberPrivate = '" + jsonMemberPrivate + '\'' +
                        ",organization_url_name = '" + organizationUrlName + '\'' +
                        ",stocks_count = '" + stocksCount + '\'' +
                        ",created_at = '" + createdAt + '\'' +
                        ",team_membership = '" + teamMembership + '\'' +
                        ",body = '" + body + '\'' +
                        ",title = '" + title + '\'' +
                        ",url = '" + url + '\'' +
                        ",tags = '" + tags + '\'' +
                        ",likes_count = '" + likesCount + '\'' +
                        ",updated_at = '" + updatedAt + '\'' +
                        ",comments_count = '" + commentsCount + '\'' +
                        ",reactions_count = '" + reactionsCount + '\'' +
                        ",slide = '" + slide + '\'' +
                        ",page_views_count = '" + pageViewsCount + '\'' +
                        ",id = '" + id + '\'' +
                        ",coediting = '" + coediting + '\'' +
                        ",user = '" + user + '\'' +
                        ",group = '" + group + '\'' +
                        "}";
    }
}