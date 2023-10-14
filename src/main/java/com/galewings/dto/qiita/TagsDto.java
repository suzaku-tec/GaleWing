package com.galewings.dto.qiita;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class TagsDto implements Serializable {

    @JsonProperty("versions")
    public List<String> versions;

    @JsonProperty("name")
    public String name;

    @Override
    public String toString() {
        return
                "TagsDto{" +
                        "versions = '" + versions + '\'' +
                        ",name = '" + name + '\'' +
                        "}";
    }
}