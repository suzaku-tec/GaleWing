package com.galewings.dto.qiita;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class UserDto implements Serializable {

    @JsonProperty("description")
    private String description;

    @JsonProperty("linkedin_id")
    private String linkedinId;

    @JsonProperty("profile_image_url")
    private String profileImageUrl;

    @JsonProperty("team_only")
    private boolean teamOnly;

    @JsonProperty("permanent_id")
    private int permanentId;

    @JsonProperty("facebook_id")
    private String facebookId;

    @JsonProperty("followees_count")
    private int followeesCount;

    @JsonProperty("items_count")
    private int itemsCount;

    @JsonProperty("twitter_screen_name")
    private String twitterScreenName;

    @JsonProperty("website_url")
    private String websiteUrl;

    @JsonProperty("followers_count")
    private int followersCount;

    @JsonProperty("organization")
    private String organization;

    @JsonProperty("name")
    private String name;

    @JsonProperty("location")
    private String location;

    @JsonProperty("github_login_name")
    private String githubLoginName;

    @JsonProperty("id")
    private String id;

    @Override
    public String toString() {
        return
                "UserDto{" +
                        "description = '" + description + '\'' +
                        ",linkedin_id = '" + linkedinId + '\'' +
                        ",profile_image_url = '" + profileImageUrl + '\'' +
                        ",team_only = '" + teamOnly + '\'' +
                        ",permanent_id = '" + permanentId + '\'' +
                        ",facebook_id = '" + facebookId + '\'' +
                        ",followees_count = '" + followeesCount + '\'' +
                        ",items_count = '" + itemsCount + '\'' +
                        ",twitter_screen_name = '" + twitterScreenName + '\'' +
                        ",website_url = '" + websiteUrl + '\'' +
                        ",followers_count = '" + followersCount + '\'' +
                        ",organization = '" + organization + '\'' +
                        ",name = '" + name + '\'' +
                        ",location = '" + location + '\'' +
                        ",github_login_name = '" + githubLoginName + '\'' +
                        ",id = '" + id + '\'' +
                        "}";
    }
}