package com.example.administrator.fnroad.user.view;

import android.app.Activity;

/**
 * Created by Administrator on 2017/6/24 0024.
 */

public interface IUserView {
    Activity getActivity();

    void setMessageTV(String message);

    void setMessageTile(String tile);

    void logout();
}
