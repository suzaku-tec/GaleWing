package com.galewings.dto.ai.googleai.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.galewings.dto.ai.googleai.request.PartsDto;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContentDto implements Serializable {
    private List<PartsDto> parts;
    private String role;

    public void setParts(List<PartsDto> parts) {
        this.parts = parts;
    }

    public List<PartsDto> getParts() {
        return parts;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return
                "ContentDto{" +
                        "parts = '" + parts + '\'' +
                        ",role = '" + role + '\'' +
                        "}";
    }
}