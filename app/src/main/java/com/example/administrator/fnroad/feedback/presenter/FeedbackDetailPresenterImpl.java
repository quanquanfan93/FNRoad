package com.example.administrator.fnroad.feedback.presenter;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.administrator.fnroad.R;
import com.example.administrator.fnroad.feedback.view.IFeedbackDetailView;
import com.example.administrator.fnroad.feedback.view.PictureAdapter;
import com.example.administrator.fnroad.main.model.Feedback;
import com.example.administrator.fnroad.utils.PictureViewerUtils.PicturePagerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public class FeedbackDetailPresenterImpl implements IFeedbackDetailPresenter{
    private static final String TAG = "FeedbackDetailPresenter";
    private IFeedbackDetailView mFeedbackDetailView;
    private String[] paths;
    private PictureAdapter mPictureAdapter;
//    private Feedback mFeedback;
    private String baseUrl="";

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
//        this.mFeedback=feedback;
        baseUrl=mFeedbackDetailView.getActivity().getString(R.string.php_service_picture_url) + "Uploads/"
                + feedback.getProjectId() + "/feedback_picture/" + feedback.getId() + "/";
    }

    @Override
    public void onGVItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final int finalCurrentPicIndex=i;
        List<Object> pictures = new ArrayList<>();
        for (int index = 0; index < paths.length; index++) {
            pictures.add(baseUrl+paths[index]);
        }
        try {
            PicturePagerUtils picturePagerUtils =
                    new PicturePagerUtils(mFeedbackDetailView.getActivity(), pictures, finalCurrentPicIndex);
            picturePagerUtils.show();
        }catch (Exception e){
            Log.e(TAG, "onGVItemClick: "+e );
        }
    }

//    private String createUrl(){
//        String baseUrl = mFeedbackDetailView.getActivity().getString(R.string.php_service_picture_url) + "Uploads/"
//                + mFeedback.getProjectId() + "/feedback_picture/" + mFeedback.getId() + "/";
//        return baseUrl;
//    }
}
