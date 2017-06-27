package com.example.administrator.fnroad.main.presenter;

import android.view.View;

import com.esri.core.geometry.Point;

/**
 * Created by Administrator on 2017/6/20 0020.
 */

public interface IProjectPresenter {
    void showUserProjectData();

    void onWidgetClicked(View v);

    Point locateProject(String projectName);
}
