package com.example.administrator.fnroad.main.presenter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.example.administrator.fnroad.ProjectApplication;
import com.example.administrator.fnroad.R;
import com.example.administrator.fnroad.login.model.UserBean;
import com.example.administrator.fnroad.main.model.Project;
import com.example.administrator.fnroad.main.model.ProjectType;
import com.example.administrator.fnroad.main.view.IProjectView;
import com.example.administrator.fnroad.main.view.ProjectFeedbackDialog;
import com.example.administrator.fnroad.spreference.SharePrefrenceHelper;
import com.example.administrator.fnroad.utils.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by Administrator on 2017/6/20 0020.
 */

public class ProjectPresenterImpl implements IProjectPresenter, OnSingleTapListener {
    private static final String TAG = "ProjectPresenterImpl";
    private IProjectView mProjectView;
    private SharePrefrenceHelper sharePrefrenceHelper;
    private MapView mMapView;

    public ProjectPresenterImpl(IProjectView projectView) {
        this.mProjectView = projectView;
        sharePrefrenceHelper = SharePrefrenceHelper.getInstance(projectView.getActivity());
        mMapView = mProjectView.getMapView();
        mMapView.setOnSingleTapListener(this);
    }

    @Override
    public void showUserProjectData() {
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
        } catch (Exception e) {
            Log.e(TAG, "showUserProjectData: " + e);
        }
    }

    @Override
    public void onWidgetClicked(View v) {
        switch (v.getId()) {
            case R.id.add_project:
                mProjectView.addNewProject();
                break;
        }
    }

    /**
     * 处理项目数据并加以显示。
     *
     * @param response
     */
    private void handleProjectsData(String response) {
        Gson gson = new Gson();
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
//                project.setPicture(jsonObject.getString("picture"));
//                projectList.add(project);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Log.e(TAG, "handleProjectsData: "+e);
//        }
        List<Project> projectList = gson.fromJson(response, new TypeToken<List<Project>>() {
        }.getType());
        Log.e(TAG, "handleProjectsData: " + projectList.size());
        for (Project project : projectList) {
            Drawable drawable = null;
            switch (project.getStatus()) {
                case 1:
                    drawable = mProjectView.getActivity().getResources().getDrawable(R.mipmap.icon_point_wait);
                    break;
                case 2:
                    drawable = mProjectView.getActivity().getResources().getDrawable(R.mipmap.icon_point_doing);
                    break;
                case 3:
                    drawable = mProjectView.getActivity().getResources().getDrawable(R.mipmap.icon_point_update);
                    break;
                case 4:
                    drawable = mProjectView.getActivity().getResources().getDrawable(R.mipmap.icon_point_done);
                    break;
                default:
                    break;
            }
            if (drawable != null) {
                PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(mProjectView.getActivity().getApplicationContext(),
                        drawable);
                Point pictureMarkerPoint = new Point(project.getX(), project.getY());
                Map<String, Object> attributes = new HashMap<>();
                attributes.put("PROJECT_ID", project.getProjectId());
                attributes.put("PROJECT_NAME", project.getProjectName());
                attributes.put("ROAD_NAME", project.getRoadName());
                attributes.put("TYPE_ID", project.getProjectType().getId());
                attributes.put("TYPE_NAME", project.getProjectType().getType());
                attributes.put("DESCRIPTION", project.getDescription());
                if (project.getActualAmount().equals(null) || project.getActualAmount().equals(""))
                    attributes.put("ACTUAL_AMOUNT", 0);
                else attributes.put("ACTUAL_AMOUNT", project.getActualAmount());
                if (project.getProgress().equals(null) || project.getProgress().equals(""))
                    attributes.put("PROGRESS", "0");
                else attributes.put("PROGRESS", project.getProgress());
                Graphic pictureMarkerGraphic = new Graphic(pictureMarkerPoint, pictureMarkerSymbol, attributes);
                mProjectView.addGraphicOnMap(pictureMarkerGraphic);
            }
        }
    }

    @Override
    public void onSingleTap(float v, float v1) {
        int[] ids = mProjectView.getGraphicLayer().getGraphicIDs(v, v1, 10);
        if (ids.length > 0) {
            Graphic selectedGraphic = mProjectView.getGraphicLayer().getGraphic(ids[0]);
            Map<String, Object> attributes = selectedGraphic.getAttributes();
            sharePrefrenceHelper.putStringValue("PROJECT_ID", attributes.get("PROJECT_ID").toString());
            sharePrefrenceHelper.putStringValue("PROJECT_NAME", attributes.get("PROJECT_NAME").toString());
            sharePrefrenceHelper.putStringValue("ROAD_NAME", attributes.get("ROAD_NAME").toString());
            sharePrefrenceHelper.putStringValue("TYPE_ID", attributes.get("TYPE_ID").toString());
            sharePrefrenceHelper.putStringValue("TYPE_NAME", attributes.get("TYPE_NAME").toString());
            sharePrefrenceHelper.putStringValue("DESCRIPTION", attributes.get("DESCRIPTION").toString());
            sharePrefrenceHelper.putStringValue("ACTUAL_AMOUNT", attributes.get("ACTUAL_AMOUNT").toString());
            sharePrefrenceHelper.putStringValue("PROGRESS", attributes.get("PROGRESS").toString());
            ProjectFeedbackDialog projectFeedbackDialog = new ProjectFeedbackDialog(mProjectView);
            projectFeedbackDialog.show();
        }
    }
}
