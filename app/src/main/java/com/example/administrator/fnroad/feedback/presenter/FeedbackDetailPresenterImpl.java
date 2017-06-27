package com.example.administrator.fnroad.feedback.presenter;

import android.view.View;
import android.widget.AdapterView;

import com.example.administrator.fnroad.R;
import com.example.administrator.fnroad.feedback.view.IFeedbackDetailView;
import com.example.administrator.fnroad.feedback.view.PictureAdapter;
import com.example.administrator.fnroad.main.model.Feedback;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public class FeedbackDetailPresenterImpl implements IFeedbackDetailPresenter{
    private IFeedbackDetailView mFeedbackDetailView;
    private String[] paths;
    private PictureAdapter mPictureAdapter;

    public FeedbackDetailPresenterImpl(IFeedbackDetailView feedbackDetailView){
        this.mFeedbackDetailView=feedbackDetailView;
    }


    @Override
    public void onWidgetClicked(View v) {
        switch (v.getId()){
            case R.id.btn_feedback_detail_back:
                mFeedbackDetailView.getActivity().finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void setPictureGV(Feedback feedback) {
        paths = feedback.getPictureName().equals("") ? new String[0] : feedback.getPictureName().split(";");
        mPictureAdapter = new PictureAdapter(mFeedbackDetailView.getActivity(),feedback.getProjectId(), feedback.getId(), paths);
        mFeedbackDetailView.setPictureGVAdapter(mPictureAdapter);
    }
}
