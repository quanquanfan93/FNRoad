package com.example.administrator.fnroad.main.presenter;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public interface IProjectFeedbackPresenter{
    void onWidgetClicked(View v);

    void initListView(int projectId);

    void onListViewItemClick(AdapterView<?> adapterView, View view, int i, long l);
}
