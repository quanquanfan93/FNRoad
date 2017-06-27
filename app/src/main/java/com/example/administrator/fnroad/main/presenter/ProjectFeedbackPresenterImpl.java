package com.example.administrator.fnroad.main.presenter;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.example.administrator.fnroad.ProjectApplication;
import com.example.administrator.fnroad.R;
import com.example.administrator.fnroad.main.model.Feedback;
import com.example.administrator.fnroad.main.model.Project;
import com.example.administrator.fnroad.main.view.FeedbackItemInfoAdapter;
import com.example.administrator.fnroad.main.view.IProjectFeedbackView;
import com.example.administrator.fnroad.utils.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.ProcessingInstruction;

import java.util.List;

import okhttp3.Request;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public class ProjectFeedbackPresenterImpl implements IProjectFeedbackPresenter{
    private IProjectFeedbackView projectFeedbackView;
    FeedbackItemInfoAdapter feedbackItemInfoAdapter;

    public ProjectFeedbackPresenterImpl(IProjectFeedbackView projectFeedbackView){
        this.projectFeedbackView=projectFeedbackView;
    }

    @Override
    public void onWidgetClicked(View v) {
        switch (v.getId()){
            case R.id.btn_dialog_add_feedback:
                projectFeedbackView.addNewFeedback();
                break;
            case R.id.btn_dialog_cancel_back:
                projectFeedbackView.dialogDismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void initListView(int projectId) {
        OkHttpUtils.Param[] param = new OkHttpUtils.Param[1];
        param[0] = new OkHttpUtils.Param("PROJECTID", String.valueOf(projectId));
        String url = projectFeedbackView.getActivity().getString(R.string.php_service_url)+"Feedback/project_feedback";
        OkHttpUtils.postAsyn(url, param, new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                ProjectApplication.getInstance().onNetError();
            }

            @Override
            public void onResponse(String response) {
                try {
                    Gson gson=new Gson();
                    List<Feedback> feedbackList=gson.fromJson(response,new TypeToken<List<Feedback>>(){}.getType());
                    feedbackItemInfoAdapter=new FeedbackItemInfoAdapter(projectFeedbackView.getActivity(),feedbackList);
                    projectFeedbackView.setAdapter(feedbackItemInfoAdapter);
//                    String response1=response.substring(2);
//                    JSONObject jsonObject=new JSONObject(response1);
//                    JSONArray projectArray=jsonObject.getJSONArray("project");
//                    JSONArray problemArray=jsonObject.getJSONArray("problem");
//                    mProjectName=new String[problemArray.length()];
//                    mProblems=new Problem[problemArray.length()];
//                    for(int i=0;i<=problemArray.length()-1;i++) {
//
//                        mProjectName[i]=projectArray.getJSONObject(i).getString("name");
//                        mProblems[i] = new Problem();
//                        mProblems[i].setProblem_id(Integer.valueOf(problemArray.getJSONObject(i).getString("problem_id")));
//                        mProblems[i].setProject_id(Integer.valueOf(problemArray.getJSONObject(i).getString("project_id")));
//                        mProblems[i].setTitle(problemArray.getJSONObject(i).getString("title"));
//                        mProblems[i].setDescription(problemArray.getJSONObject(i).getString("description"));
//                        mProblems[i].setPicture_name(problemArray.getJSONObject(i).getString("picture_name"));
//                        mProblems[i].setTime(problemArray.getJSONObject(i).getString("time"));
//                        mProblems[i].setRecorder(problemArray.getJSONObject(i).getString("recorder"));
//
//                    }
//                    setProblems(mProblems,mProjectName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onListViewItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Feedback feedback=(Feedback)feedbackItemInfoAdapter.getItem(i);
        Bundle bundle=new Bundle();
        bundle.putParcelable("feedback",feedback);
        projectFeedbackView.showFeedbackDetail(bundle);
    }

}
