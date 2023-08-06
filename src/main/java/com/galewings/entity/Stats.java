package com.galewings.entity;

import com.miragesql.miragesql.annotation.Column;
import com.miragesql.miragesql.annotation.Table;

@Table(name = "stats")
public class Stats {
    @Column(name = "id")
    public Integer id;

    @Column(name = "title")
    public String title;

    @Column(name = "visualize")
    public String visualize;

    @Column(name = "sql")
    public String sql;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVisualize() {
        return visualize;
    }

    public void setVisualize(String visualize) {
        this.visualize = visualize;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
