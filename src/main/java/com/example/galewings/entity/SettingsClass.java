package com.example.galewings.entity;

import com.miragesql.miragesql.annotation.Column;
import com.miragesql.miragesql.annotation.Table;

import java.util.List;

@SuppressWarnings("unused")
@Table(name = "settings_class")
public class SettingsClass {

    @Column(name = "id")
    public String id;

    @Column(name = "name")
    public String name;

    @Column(name = "parendId")
    public String parendId;

    public List<SettingsClass> settings;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getParendId() {
        return parendId;
    }

}
