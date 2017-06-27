package com.example.administrator.fnroad.main.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/6/17 0017.
 */

public class ProjectType implements Parcelable{
    private int id;
    private String type;
    private String description;

    public ProjectType(){
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(type);
        parcel.writeString(description);
    }

    public static final Parcelable.Creator<ProjectType> CREATOR = new Creator<ProjectType>() {
        @Override
        public ProjectType[] newArray(int size) {
            return new ProjectType[size];
        }

        @Override
        public ProjectType createFromParcel(Parcel in) {
            return new ProjectType(in);
        }
    };

    public ProjectType(Parcel in){
        id=in.readInt();
        type=in.readString();
        description=in.readString();
    }
}
