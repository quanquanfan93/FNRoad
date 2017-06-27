package com.example.administrator.fnroad.main.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.administrator.fnroad.feedback.view.FeedbackDetailActivity;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public class Feedback implements Parcelable{
    public int id;
    @SerializedName("project_id")
    public int projectId;
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

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int project_id) {
        this.projectId = project_id;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(projectId);
        parcel.writeInt(status);
        parcel.writeString(updateAmount);
        parcel.writeString(progress);
        parcel.writeString(description);
        parcel.writeString(createTime);
        parcel.writeString(pictureName);
    }

    public static final Parcelable.Creator<Feedback> CREATOR = new Creator<Feedback>() {
        @Override
        public Feedback[] newArray(int size) {
            return new Feedback[size];
        }

        @Override
        public Feedback createFromParcel(Parcel in) {
            return new Feedback(in);
        }
    };

    public Feedback(Parcel in){
        id=in.readInt();
        projectId=in.readInt();
        status=in.readInt();
        updateAmount=in.readString();
        progress=in.readString();
        description=in.readString();
        createTime=in.readString();
        pictureName=in.readString();
    }

}
