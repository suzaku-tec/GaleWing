package com.example.galewings.entity;

import com.miragesql.miragesql.annotation.Column;
import com.miragesql.miragesql.annotation.Table;

@Table(name = "setting")
public class Settings {

    @Column(name = "id")
    public String id;

    @Column(name = "name")
    public String name;

    @Column(name = "setting")
    public String setting;

}
