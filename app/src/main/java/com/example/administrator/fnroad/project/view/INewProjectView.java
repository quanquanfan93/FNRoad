package com.example.administrator.fnroad.project.view;

import android.app.Activity;
import android.widget.SimpleAdapter;

import com.example.administrator.fnroad.main.model.ProjectType;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Administrator on 2017/6/22 0022.
 */

public interface INewProjectView {
    Activity getActivity();

    void setAdapter(List<String> roadTypeList, List<String> projectTypeList);

    void setPictureGVAdapter(SimpleAdapter simpleAdapter);

    void openAlbum();

    String getProjectName();

    String getRoadName();

    String getProjectType(List<ProjectType> projectTypeList);

    String getProjectDescription();

    double getX();

    double getY();

    String getImagePath();

    void setImagePath(String imagePath);
}
