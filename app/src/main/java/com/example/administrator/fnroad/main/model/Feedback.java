package com.example.administrator.fnroad.main.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public class Feedback {
    public int id;
    @SerializedName("project_id")
    public int project_id;
    public int status;
    @SerializedName("update_amount")
    public String updateAmount;
    public String progress;
    public String description;
    @SerializedName("create_time")
    public String createTime;
    @SerializedName("picture_name")
    public String pictureName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUpdateAmount() {
        return updateAmount;
    }

    public void setUpdateAmount(String updateAmount) {
        this.updateAmount = updateAmount;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }
}
