package com.example.administrator.fnroad.feedback.presenter;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public interface INewFeedbackPresenter {
    void onWidgetClicked(View v);

    void initPictureGV();

    void onGVItemClick(AdapterView<?> adapterView, View view, int i, long l);

    void addImagePathAndRefresh(String imagePath);
}
