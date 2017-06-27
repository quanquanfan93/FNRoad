package com.example.administrator.fnroad.feedback.view;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.fnroad.R;
import com.example.administrator.fnroad.feedback.presenter.FeedbackDetailPresenterImpl;
import com.example.administrator.fnroad.feedback.presenter.IFeedbackDetailPresenter;
import com.example.administrator.fnroad.main.model.Feedback;
import com.example.administrator.fnroad.spreference.SharePrefrenceHelper;

public class FeedbackDetailActivity extends AppCompatActivity implements View.OnClickListener,IFeedbackDetailView{

    private IFeedbackDetailPresenter mFeedbackDetailPresenter;
    private TextView projectNameTV;
    private TextView roadNameTV;
    private TextView projectTypeTV;
    private TextView projectDescriptionTV;
    private TextView actualAmountTV;
    private TextView updateAmountTV;
    private TextView progressTV;
    private TextView descriptionTV;
    private GridView updatePicturesGV;
    private Button backBTN;
    private Feedback mFeedback;
    private SharePrefrenceHelper sharePrefrenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_detail);
        initAllView();
    }

    private void initAllView(){
        mFeedbackDetailPresenter=new FeedbackDetailPresenterImpl(this);
        sharePrefrenceHelper = SharePrefrenceHelper.getInstance(this);
        projectNameTV=(TextView)findViewById(R.id.tv_feedback_detail_project_name);
        roadNameTV=(TextView)findViewById(R.id.tv_feedback_detail_road_name);
        projectTypeTV=(TextView)findViewById(R.id.tv_feedback_detail_project_type);
        projectDescriptionTV=(TextView)findViewById(R.id.tv_feedback_detail_project_description);
        actualAmountTV=(TextView)findViewById(R.id.tv_feedback_detail_actual_amount);
        updateAmountTV=(TextView)findViewById(R.id.tv_feedback_detail_update_amount);
        progressTV=(TextView)findViewById(R.id.tv_feedback_detail_progress);
        descriptionTV=(TextView)findViewById(R.id.tv_feedback_detail_problem_description);
        updatePicturesGV=(GridView)findViewById(R.id.gv_feedback_detail_pictures);
        backBTN=(Button)findViewById(R.id.btn_feedback_detail_back);
        Intent intent=getIntent();
        mFeedback=intent.getParcelableExtra("feedback");
        projectNameTV.setText(sharePrefrenceHelper.getStringValue("PROJECT_NAME"));
        roadNameTV.setText(sharePrefrenceHelper.getStringValue("ROAD_NAME"));
        projectTypeTV.setText(sharePrefrenceHelper.getStringValue("TYPE_NAME"));
        projectDescriptionTV.setText(sharePrefrenceHelper.getStringValue("DESCRIPTION"));
        actualAmountTV.setText(sharePrefrenceHelper.getStringValue("ACTUAL_AMOUNT"));
        updateAmountTV.setText(mFeedback.getUpdateAmount());
        progressTV.setText(mFeedback.getProgress()+"%");
        descriptionTV.setText(mFeedback.getDescription());
        if(mFeedback.getStatus()==0){
            backBTN.setText("审核中");
        }else if(mFeedback.getStatus()==1){
            backBTN.setText("已通过");
        }else if(mFeedback.getStatus()==2){
            backBTN.setText("已拒绝");
        }
        backBTN.setOnClickListener(this);
        mFeedbackDetailPresenter.setPictureGV(mFeedback);
    }

    @Override
    public void onClick(View view) {
        mFeedbackDetailPresenter.onWidgetClicked(view);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void setPictureGVAdapter(PictureAdapter pictureAdapter) {
        updatePicturesGV.setAdapter(pictureAdapter);
    }
}
