package com.example.administrator.fnroad.project.view;

import android.app.Activity;
import android.widget.SimpleAdapter;

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

    String getProjectType();

    String getProjectDescription();

    double getX();

    double getY();
}
