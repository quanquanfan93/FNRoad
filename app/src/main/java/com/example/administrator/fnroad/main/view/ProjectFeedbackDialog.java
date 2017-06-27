package com.example.administrator.fnroad.main.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.fnroad.R;
import com.example.administrator.fnroad.feedback.view.FeedbackDetailActivity;
import com.example.administrator.fnroad.feedback.view.NewFeedbackActivity;
import com.example.administrator.fnroad.main.presenter.IProjectFeedbackPresenter;
import com.example.administrator.fnroad.main.presenter.IProjectPresenter;
import com.example.administrator.fnroad.main.presenter.ProjectFeedbackPresenterImpl;
import com.example.administrator.fnroad.main.presenter.ProjectPresenterImpl;
import com.example.administrator.fnroad.project.view.NewProjectActivity;
import com.example.administrator.fnroad.spreference.SharePrefrenceHelper;

import static com.example.administrator.fnroad.main.view.MainActivity.REQUEST_NEW_FEEDBACK;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public class ProjectFeedbackDialog extends Dialog implements IProjectFeedbackView,View.OnClickListener,ListView.OnItemClickListener{
    private IProjectView projectView;
    private TextView projectFeedbackTileTV;
    private Button addFeedbackBTN;
    private Button cancelFeedbackBTN;
    private ListView feedbackLV;
    private SharePrefrenceHelper sharePrefrenceHelper;
    private IProjectFeedbackPresenter projectFeedbackPresenter;
    private int projectId=0;
    private FeedbackItemInfoAdapter mFeedbackItemInfoAdapter;

    public ProjectFeedbackDialog(IProjectView projectView) {
        super(projectView.getActivity());
        this.projectView=projectView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(projectView.getActivity(),R.layout.dialog_project_feedback, null);
        setContentView(view, params);
        this.setCanceledOnTouchOutside(true);
        projectFeedbackTileTV=(TextView)findViewById(R.id.tv_project_feedback_name);
        addFeedbackBTN=(Button)findViewById(R.id.btn_dialog_add_feedback);
        cancelFeedbackBTN=(Button)findViewById(R.id.btn_dialog_cancel_back);
        feedbackLV=(ListView)findViewById(R.id.lv_feedback);
        sharePrefrenceHelper = SharePrefrenceHelper.getInstance(projectView.getActivity());
        projectFeedbackPresenter=new ProjectFeedbackPresenterImpl(this);
        addFeedbackBTN.setOnClickListener(this);
        cancelFeedbackBTN.setOnClickListener(this);
        String projectName=sharePrefrenceHelper.getStringValue("PROJECT_NAME");
        projectFeedbackTileTV.setText(projectName);
        projectId= Integer.valueOf(sharePrefrenceHelper.getStringValue("PROJECT_ID"));
        projectFeedbackPresenter.initListView(projectId);
        if(sharePrefrenceHelper.getStringValue("STATUS").equals("1")){
            addFeedbackBTN.setVisibility(View.GONE);
            cancelFeedbackBTN.setText("立项审核中");
        }
        if(sharePrefrenceHelper.getStringValue("STATUS").equals("3")){
            addFeedbackBTN.setVisibility(View.GONE);
            cancelFeedbackBTN.setText("反馈审核中");
        }
        if(sharePrefrenceHelper.getStringValue("STATUS").equals("4")){
            addFeedbackBTN.setVisibility(View.GONE);
            cancelFeedbackBTN.setText("项目已完工");
        }
        feedbackLV.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        projectFeedbackPresenter.onWidgetClicked(view);
    }

    @Override
    public Activity getActivity() {
        return projectView.getActivity();
    }

    @Override
    public void setAdapter(FeedbackItemInfoAdapter feedbackItemInfoAdapter) {
        feedbackLV.setAdapter(feedbackItemInfoAdapter);
    }

    @Override
    public void dialogDismiss() {
        this.dismiss();
    }

    @Override
    public void addNewFeedback() {
        Intent intent = new Intent(getActivity(), NewFeedbackActivity.class);
        this.dismiss();
        getActivity().startActivityForResult(intent,REQUEST_NEW_FEEDBACK);
    }

    @Override
    public void showFeedbackDetail(Bundle bundle) {
        Intent intent=new Intent(getActivity(), FeedbackDetailActivity.class);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        projectFeedbackPresenter.onListViewItemClick(adapterView,view,i,l);
    }
}
