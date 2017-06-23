package com.example.administrator.fnroad.feedback.view;

import android.app.Activity;
import android.widget.SimpleAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public interface INewFeedbackView {
    Activity getActivity();

    void setPictureGVAdapter(SimpleAdapter simpleAdapter);

    void openAlbum();

    String getUpdateAmount();

    String getProgress();

    String getDescription();

    void setProgress(String progress);
}
