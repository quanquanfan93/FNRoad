package com.example.administrator.fnroad.feedback.presenter;

import android.view.View;

import com.example.administrator.fnroad.main.model.Feedback;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public interface IFeedbackDetailPresenter {
    void onWidgetClicked(View v);

    void setPictureGV(Feedback feedback);
}
