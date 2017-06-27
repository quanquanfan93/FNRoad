package com.example.administrator.fnroad.login.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.administrator.fnroad.main.model.Feedback;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/10/10 0010.
 */
public class UserBean implements Parcelable{
    @SerializedName("id")
    private int mUserId;
    @SerializedName("username")
    private String mUsername;
    @SerializedName("password")
    private String mPassword;
    @SerializedName("name")
    private String mName;
    @SerializedName("telephone")
    private int mTelephone;
    @SerializedName("permission")
    private int mPermission;
    @SerializedName("organization")
    private String mOrganization;

    public UserBean() {
    }

//    public UserBean(String username, String password) {
//        mUsername = username;
//        mPassword = password;
//    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getTelephone() {
        return mTelephone;
    }

    public void setTelephone(int telephone) {
        mTelephone = telephone;
    }

    public int getPermission() {
        return mPermission;
    }

    public void setPermission(int permission) {
        mPermission = permission;
    }

    public String getOrganization() {
        return mOrganization;
    }

    public void setOrganization(String organization) {
        mOrganization = organization;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mUserId);
        parcel.writeString(mUsername);
        parcel.writeString(mPassword);
        parcel.writeString(mName);
        parcel.writeInt(mTelephone);
        parcel.writeInt(mPermission);
        parcel.writeString(mOrganization);
    }

    public static final Parcelable.Creator<UserBean> CREATOR = new Creator<UserBean>() {
        @Override
        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }

        @Override
        public UserBean createFromParcel(Parcel in) {
            return new UserBean(in);
        }
    };


    public UserBean(Parcel in){
        mUserId=in.readInt();
        mUsername=in.readString();
        mPassword=in.readString();
        mName=in.readString();
        mTelephone=in.readInt();
        mPermission=in.readInt();
        mOrganization=in.readString();
    }



}
