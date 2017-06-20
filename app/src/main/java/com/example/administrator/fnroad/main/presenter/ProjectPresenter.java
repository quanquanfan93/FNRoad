package com.example.administrator.fnroad.main.presenter;

import android.util.Log;

import com.esri.core.internal.catalog.User;
import com.example.administrator.fnroad.ProjectApplication;
import com.example.administrator.fnroad.R;
import com.example.administrator.fnroad.login.model.UserBean;
import com.example.administrator.fnroad.main.model.Project;
import com.example.administrator.fnroad.main.model.ProjectType;
import com.example.administrator.fnroad.main.view.IProjectView;
import com.example.administrator.fnroad.main.view.MainActivity;
import com.example.administrator.fnroad.utils.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import okhttp3.Request;

/**
 * Created by Administrator on 2017/6/20 0020.
 */

public class ProjectPresenter implements IProjectPresenter{
    private static final String TAG = "ProjectPresenter";
    private IProjectView mProjectView;

    public ProjectPresenter(IProjectView projectView){
        this.mProjectView=projectView;
    }

    @Override
    public void showUserProjectData(){
//        OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
        try {
            OkHttpUtils.Param[] param = new OkHttpUtils.Param[1];
            param[0] = new OkHttpUtils.Param("USERID", String.valueOf(ProjectApplication.getInstance().getUserBean().getUserId()));
            String url = mProjectView.getActivity().getString(R.string.php_service_url) + "Project/user_project";
            OkHttpUtils.postAsyn(url, param, new OkHttpUtils.ResultCallback<String>() {
                @Override
                public void onError(Request request, Exception e) {
                    ProjectApplication.getInstance().onNetError();
                }

                @Override
                public void onResponse(String response) {
                    handleProjectsData(response);
                }
            });
        }catch (Exception e){
            Log.e(TAG, "showUserProjectData: "+e);
        }
    }

    private void handleProjectsData(String response){
        Gson gson=new Gson();
//        List<Project> projectList=new ArrayList<>();
//        try {
//            JSONArray jsonArray=new JSONArray(response);
//            for(int i=0;i<jsonArray.length();i++){
//                JSONObject jsonObject=jsonArray.getJSONObject(i);
//                Project project=new Project();
//                project.setProjectId(jsonObject.getInt("project_id"));
//                project.setStatus(jsonObject.getInt("status"));
//                project.setProjectName(jsonObject.getString("project_name"));
//                project.setRoadName(jsonObject.getString("road_name"));
//                ProjectType projectType=new ProjectType();
//                JSONObject typeJSONObject=jsonObject.getJSONObject("project_type");
//                projectType.setId(typeJSONObject.getInt("id"));
//                projectType.setType(typeJSONObject.getString("type"));
//                projectType.setDescription(typeJSONObject.getString("description"));
//                project.setProjectType(projectType);
//                project.setCreateTime(jsonObject.getString("create_time"));
//                project.setEtc(jsonObject.getString("etc"));
//                project.setDescription(jsonObject.getString("description"));
//                project.setEstimatedAmount(jsonObject.getDouble("estimated_amount"));
//                project.setActualAmount(jsonObject.getDouble("actual_amount"));
//                project.setOrganization(jsonObject.getString("organization"));
//                project.setConstructionManager(jsonObject.getString("construction_manager"));
//                UserBean patrolManager=new UserBean();
//                JSONObject userJSONObject=jsonObject.getJSONObject("patrol_manager");
//                patrolManager.setUserId(userJSONObject.getInt("id"));
//                patrolManager.setUsername(userJSONObject.getString("username"));
//                patrolManager.setPassword(userJSONObject.getString("password"));
//                patrolManager.setTelephone(userJSONObject.getInt("telephone"));
//                patrolManager.setOrganization(userJSONObject.getString("organization"));
//                patrolManager.setName(userJSONObject.getString("name"));
//                patrolManager.setPermission(userJSONObject.getInt("permission"));
//                project.setPatrolManager(patrolManager);
//                project.setProgress(jsonObject.getInt("progress"));
//                project.setX(jsonObject.getDouble("x"));
//                project.setY(jsonObject.getDouble("y"));
//                projectList.add(project);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Log.e(TAG, "handleProjectsData: "+e);
//        }
        List<Project> projectList=gson.fromJson(response,new TypeToken<List<Project>>(){}.getType());
        Log.e(TAG, "handleProjectsData: "+projectList.size());

    }


}
