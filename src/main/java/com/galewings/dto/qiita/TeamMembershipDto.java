package com.galewings.dto.qiita;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class TeamMembershipDto implements Serializable {

    @JsonProperty("name")
    private String name;

    @Override
    public String toString() {
        return
                "TeamMembershipDto{" +
                        "name = '" + name + '\'' +
                        "}";
    }
}