package com.example.administrator.fnroad.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/6/17 0017.
 */

public class ProjectType extends DataSupport{
    private int id;
    private String type;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
