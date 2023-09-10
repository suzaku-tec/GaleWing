package com.galewings.dto.input;

import org.springframework.stereotype.Component;

@Component
public class TaskExecuteDto {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
