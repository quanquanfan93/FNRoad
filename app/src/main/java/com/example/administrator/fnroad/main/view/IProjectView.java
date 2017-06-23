package com.example.administrator.fnroad.main.view;

import android.app.Activity;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.map.Graphic;

/**
 * Created by Administrator on 2017/6/20 0020.
 */

public interface IProjectView{
    Activity getActivity();

    void addGraphicOnMap(Graphic graphic);

    void addNewProject();

    GraphicsLayer getGraphicLayer();

    MapView getMapView();
}
