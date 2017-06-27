package com.example.administrator.fnroad.main.view;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public interface IProjectFeedbackView {

    Activity getActivity();

    void setAdapter(FeedbackItemInfoAdapter feedbackItemInfoAdapter);

    void dialogDismiss();

    void addNewFeedback();

    void showFeedbackDetail(Bundle bundle);
}
