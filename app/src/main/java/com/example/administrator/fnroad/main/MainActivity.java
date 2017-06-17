package com.example.administrator.fnroad.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.esri.android.map.MapView;
import com.esri.android.map.event.OnZoomListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.Envelope;
import com.example.administrator.fnroad.R;
import com.example.administrator.fnroad.map.TDTTiledServiceLayer;

public class MainActivity extends AppCompatActivity {

    MapView mMapView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除Arcgis For Android水印
        ArcGISRuntime.setClientId("GSyp0BE8ewTdlEhI");
        setContentView(R.layout.activity_main);
        mMapView = (MapView) findViewById(R.id.mapView);;
        initMap();
    }

    public void initMap(){
        TDTTiledServiceLayer vec = new TDTTiledServiceLayer(TDTTiledServiceLayer.TDTTiledServiceType.VEC_C);
        mMapView.addLayer(vec);
        final TDTTiledServiceLayer cva = new TDTTiledServiceLayer(TDTTiledServiceLayer.TDTTiledServiceType.CVA_C);
        mMapView.addLayer(cva);
        Envelope env = new Envelope(117.553370,26.698680,122.959910,31.608534);//范围
        mMapView. setExtent(env);//设置地图显示范围
        mMapView.setOnZoomListener(new OnZoomListener() {
            @Override
            public void preAction(float paramFloat1, float paramFloat2,
                                  double paramDouble) {
                //缩放后
            }
            @Override
            public void postAction(float paramFloat1, float paramFloat2,
                                   double paramDouble) {
                //缩放前 防止标注重叠
                cva.refresh();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        mMapView.unpause();
    }

    @Override
    public void onPause(){
        super.onPause();
        mMapView.pause();
    }


}
