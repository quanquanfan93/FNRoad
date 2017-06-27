package com.example.administrator.fnroad.main.view;

import android.app.Activity;
import android.location.Location;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.example.administrator.fnroad.main.model.Project;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/20 0020.
 */

public interface IProjectView{
    Activity getActivity();

    void addGraphicOnMap(Graphic graphic);

    void addNewProject();

    GraphicsLayer getGraphicLayer();

    MapView getMapView();

    void showUser();

    void searchProject(String[] projectNames);

    void showAllProject();

    void setLocationChanged(Point point);

    public Point getMapPoint(float v, float v1);

    public SpatialReference getSpatialReference();
}
