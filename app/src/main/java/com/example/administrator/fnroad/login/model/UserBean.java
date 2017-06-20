package com.example.administrator.fnroad.login.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/10/10 0010.
 */
public class UserBean {
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
}
