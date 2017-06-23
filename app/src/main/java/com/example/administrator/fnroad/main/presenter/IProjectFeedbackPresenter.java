package com.example.administrator.fnroad.main.presenter;

import android.view.View;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public interface IProjectFeedbackPresenter{
    void onWidgetClicked(View v);

    void initListView(int projectId);
}
