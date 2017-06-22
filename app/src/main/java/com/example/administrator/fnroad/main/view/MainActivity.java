package com.example.administrator.fnroad.main.view;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

//import com.esri.android.map.LocationDisplayManager;
//import com.esri.android.map.MapView;
//import com.esri.android.map.event.OnZoomListener;
//import com.esri.android.runtime.ArcGISRuntime;
//import com.esri.core.geometry.Envelope;
//import com.esri.arcgisruntime.geometry.Envelope;
//import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnZoomListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.example.administrator.fnroad.R;
import com.example.administrator.fnroad.main.presenter.IProjectPresenter;
import com.example.administrator.fnroad.main.presenter.ProjectPresenter;
import com.example.administrator.fnroad.map.TDTTiledServiceLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements IProjectView{
    private static final String TAG = "MainActivity";

    private MapView mMapView ;
    private FloatingActionButton mLocationFab;
    private LocationDisplayManager mLocationDisplayManager;
    private static Point mLocation = null;
    private IProjectPresenter projectPresenter;
    private final SpatialReference egs = SpatialReference.create(4326);
    private GraphicsLayer mGraphicsLayer=new GraphicsLayer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除Arcgis For Android水印
        ArcGISRuntime.setClientId("GSyp0BE8ewTdlEhI");
        setContentView(R.layout.activity_main);
        mMapView = (MapView) findViewById(R.id.mapView);;
        mLocationFab=(FloatingActionButton)findViewById(R.id.fab);
        projectPresenter=new ProjectPresenter(this);
        List<String> permissionList=new ArrayList<>();
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_PHONE_STATE)!=
                PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String[] permissions=permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this,permissions,1);
        }else {
            initMap();
            setLocation();
        }
        projectPresenter.showUserProjectData();
    }

    /**
     * 初始化地图
     */
    private void initMap(){
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
        mMapView.addLayer(mGraphicsLayer);
//        mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {
//            @Override
//            public void onStatusChanged(Object o, STATUS status) {
//
//            }
//        });
    }

    /**
     * 设置定位
     */
    private void setLocation(){
//        LocationDisplayManager locationDisplayManager =  mMapView.getLocationDisplayManager();//获取定位类
//        mLocationDisplayManager=mMapView.getLocationDisplayManager();
//        mLocationDisplayManager.setShowLocation(true);
//        mLocationDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);//设置模式
//        mLocationDisplayManager.setShowPings(true);
//        mLocationDisplayManager.start();//开始定位
        mLocationDisplayManager=mMapView.getLocationDisplayManager();
        mLocationDisplayManager.setLocationListener(new MyLocationListener());
        mLocationDisplayManager.start();
        mLocationDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);//设置模式
//        mLocationDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.OFF);
        mLocationFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Point point=(Point)GeometryEngine.project(mLocation,egs,egs);
                mMapView.zoomToResolution(point,4.29153442382814E-05);
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


    @Override
    public Activity getActivity() {
        return this;
    }


    @Override
    public void addGraphicOnMap(Graphic graphic) {
        mGraphicsLayer.addGraphic(graphic);
    }


    /**
     * 定位内部类
     */
    private class MyLocationListener implements LocationListener {

        public MyLocationListener() {
            super();
        }

        /**
         * If location changes, update our current location. If being found for
         * the first time, zoom to our current position with a resolution of 20
         */
        public void onLocationChanged(Location loc) {
            if (loc == null)
                return;
            boolean zoomToMe = (mLocation == null) ? true : false;
            mLocation = new Point(loc.getLongitude(), loc.getLatitude());
            if (zoomToMe) {
                Point p = (Point) GeometryEngine.project(mLocation, egs, egs);
                mMapView.zoomToResolution(p,4.29153442382814E-05);
            }
        }

        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "GPS Disabled",
                    Toast.LENGTH_SHORT).show();
        }

        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "GPS Enabled",
                    Toast.LENGTH_SHORT).show();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    /**
     * 动态权限添加
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0){
                    for(int result:grantResults){
                        if(result!=PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"必须同意所有权限才能使用本程序",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    initMap();
                    setLocation();
                }else {
                    Toast.makeText(this,"发生未知错误",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }


}
