package com.example.administrator.fnroad.main.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.esri.core.internal.catalog.User;
import com.example.administrator.fnroad.login.model.UserBean;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Administrator on 2017/6/17 0017.
 */

public class Project implements Parcelable{
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


    public Project(){
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(projectId);
        parcel.writeInt(status);
        parcel.writeString(projectName);
        parcel.writeString(roadName);
        parcel.writeParcelable(projectType,PARCELABLE_WRITE_RETURN_VALUE);
        parcel.writeString(createTime);
        parcel.writeString(etc);
        parcel.writeString(description);
        parcel.writeString(estimatedAmount);
        parcel.writeString(actualAmount);
        parcel.writeString(organization);
        parcel.writeString(constructionManager);
        parcel.writeParcelable(patrolManager,PARCELABLE_WRITE_RETURN_VALUE);
        parcel.writeString(progress);
        parcel.writeDouble(x);
        parcel.writeDouble(y);
        parcel.writeString(picture);
    }

    public static final Parcelable.Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }

        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }
    };

    public Project(Parcel in){
        projectId=in.readInt();
        status=in.readInt();
        projectName=in.readString();
        roadName=in.readString();
        projectType=in.readParcelable(ProjectType.class.getClassLoader());
        createTime=in.readString();
        etc=in.readString();
        description=in.readString();
        estimatedAmount=in.readString();
        actualAmount=in.readString();
        organization=in.readString();
        constructionManager=in.readString();
        patrolManager=in.readParcelable(UserBean.class.getClassLoader());
        progress=in.readString();
        x=in.readDouble();
        y=in.readDouble();
        picture=in.readString();
    }
}
