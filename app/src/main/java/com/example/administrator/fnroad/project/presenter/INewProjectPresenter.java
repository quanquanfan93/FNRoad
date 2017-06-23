package com.example.administrator.fnroad.project.presenter;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by Administrator on 2017/6/22 0022.
 */

public interface INewProjectPresenter {
    void onWidgetClicked(View v);

    void getTypeData();

    void initPictureGV();

    void onGVItemClick(AdapterView<?> adapterView, View view, int i, long l);

    void addImagePathAndRefresh(String imagePath);
}
