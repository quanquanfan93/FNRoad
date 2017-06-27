package com.example.administrator.fnroad.user.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/6/24 0024.
 */

public class Message {
    public int id;
    @SerializedName("project_name")
    public String projectName;
    public String description;
    @SerializedName("pass_status")
    public int passStatus;
    @SerializedName("read_status")
    public int readStatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPassStatus() {
        return passStatus;
    }

    public void setPassStatus(int passStatus) {
        this.passStatus = passStatus;
    }

    public int getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }
}
