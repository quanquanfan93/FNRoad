package com.example.administrator.fnroad.db;

import org.litepal.crud.DataSupport;
import org.w3c.dom.ProcessingInstruction;

import java.util.Date;

/**
 * Created by Administrator on 2017/6/17 0017.
 */

public class Project extends DataSupport{
    private int projectId;
    private int status;
    private String projectName;
    private String roadName;
    private int projectType;
    private Date createTime;
    private Date etc;
    private String description;
    private double estimatedAmount;
    private double actualAmount;
    private String organization;
    private String constructionManager;
    private int patrolManager;
    private int progress;
    private double x;
    private double y;

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

    public int getProjectType() {
        return projectType;
    }

    public void setProjectType(int projectType) {
        this.projectType = projectType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEtc() {
        return etc;
    }

    public void setEtc(Date etc) {
        this.etc = etc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getEstimatedAmount() {
        return estimatedAmount;
    }

    public void setEstimatedAmount(double estimatedAmount) {
        this.estimatedAmount = estimatedAmount;
    }

    public double getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(double actualAmount) {
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

    public int getPatrolManager() {
        return patrolManager;
    }

    public void setPatrolManager(int patrolManager) {
        this.patrolManager = patrolManager;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
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
}
