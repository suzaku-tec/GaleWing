package com.galewings.dto.qiita;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class GroupDto implements Serializable {

    @JsonProperty("private")
    private boolean jsonMemberPrivate;

    @JsonProperty("url_name")
    private String urlName;

    @JsonProperty("updated_at")
    private String updatedAt;

    @JsonProperty("name")
    private String name;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("description")
    private String description;

    @Override
    public String toString() {
        return
                "GroupDto{" +
                        "private = '" + jsonMemberPrivate + '\'' +
                        ",url_name = '" + urlName + '\'' +
                        ",updated_at = '" + updatedAt + '\'' +
                        ",name = '" + name + '\'' +
                        ",created_at = '" + createdAt + '\'' +
                        ",description = '" + description + '\'' +
                        "}";
    }
}