package com.example.administrator.fnroad.main.model;

import com.esri.core.internal.catalog.User;
import com.example.administrator.fnroad.login.model.UserBean;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Administrator on 2017/6/17 0017.
 */

public class Project{
    @SerializedName("project_id")
    public int projectId;
    public int status;
    @SerializedName("project_name")
    public String projectName;
    @SerializedName("road_name")
    public String roadName;
    @SerializedName("project_type")
    public ProjectType projectType;
    @SerializedName("create_time")
    public String createTime;
    public String etc;
    public String description;
    @SerializedName("estimated_amount")
    public String estimatedAmount;
    @SerializedName("actual_amount")
    public String actualAmount;
    public String organization;
    @SerializedName("construction_manager")
    public String constructionManager;
    @SerializedName("patrol_manager")
    public UserBean patrolManager;
    public String progress;
    public double x;
    public double y;
    public String picture;

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public ProjectType getProjectType() {
        return projectType;
    }

    public void setProjectType(ProjectType projectType) {
        this.projectType = projectType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEtc() {
        return etc;
    }

    public void setEtc(String etc) {
        this.etc = etc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEstimatedAmount() {
        return estimatedAmount;
    }

    public void setEstimatedAmount(String estimatedAmount) {
        this.estimatedAmount = estimatedAmount;
    }

    public String getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(String actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getConstructionManager() {
        return constructionManager;
    }

    public void setConstructionManager(String constructionManager) {
        this.constructionManager = constructionManager;
    }

    public UserBean getPatrolManager() {
        return patrolManager;
    }

    public void setPatrolManager(UserBean patrolManager) {
        this.patrolManager = patrolManager;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
