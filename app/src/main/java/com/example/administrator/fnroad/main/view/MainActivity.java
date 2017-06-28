package com.example.administrator.fnroad.main.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnZoomListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.internal.tasks.ags.c;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.example.administrator.fnroad.R;
import com.example.administrator.fnroad.main.model.Project;
import com.example.administrator.fnroad.main.presenter.IProjectPresenter;
import com.example.administrator.fnroad.main.presenter.ProjectPresenterImpl;
import com.example.administrator.fnroad.map.TDTTiledServiceLayer;
import com.example.administrator.fnroad.project.view.NewProjectActivity;
import com.example.administrator.fnroad.search.view.SearchActivity;
import com.example.administrator.fnroad.user.view.UserActivity;
import com.example.administrator.fnroad.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements IProjectView,View.OnClickListener{
    private static final String TAG = "MainActivity";

    private MapView mMapView ;
    private ImageView addProjectIV;
//    private FloatingActionButton mLocationFab;
    private ImageView userIV;
    private ImageView locationIV;
    private ImageView allProjectIV;
    private ImageView searchIV;
    private LocationDisplayManager mLocationDisplayManager;
    private static Point mLocation = null;
    private IProjectPresenter projectPresenter;
    private static Point changedLocation=null;
    private final SpatialReference egs = SpatialReference.create(4326);
    private GraphicsLayer mGraphicsLayer=new GraphicsLayer();
    private MyLocationListener mMyLocationListener;
    private Graphic locationGraphic=null;
    private int locationGraphicId=-1;
    public static final int REQUEST_NEW_PROJECT=1,REQUEST_NEW_FEEDBACK=2,REQUEST_SEARCH=3;
    private static boolean isExit = false;//退出应用
    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    isExit = false;
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除Arcgis For Android水印
        ArcGISRuntime.setClientId("GSyp0BE8ewTdlEhI");
        setContentView(R.layout.activity_main);
        mMapView = (MapView) findViewById(R.id.mapView);;
        addProjectIV=(ImageView)findViewById(R.id.add_project);
//        mLocationFab=(FloatingActionButton)findViewById(R.id.fab);
        userIV=(ImageView)findViewById(R.id.user);
        locationIV=(ImageView)findViewById(R.id.iv_main_location);
        allProjectIV=(ImageView)findViewById(R.id.iv_main_all_project);
        searchIV=(ImageView)findViewById(R.id.iv_main_search);
        projectPresenter=new ProjectPresenterImpl(this);
        initMap();
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
            setLocation();
//            initAllMembers();
        }
        projectPresenter.showUserProjectData();
        addProjectIV.setOnClickListener(this);
        userIV.setOnClickListener(this);
        allProjectIV.setOnClickListener(this);
        searchIV.setOnClickListener(this);
        Point point=new Point(118.3843557054605,28.856365950917002);
        mMapView.zoomToResolution(point,0.001373291015625);
    }

    private void initAllMembers(){
        mMapView = (MapView) findViewById(R.id.mapView);;
        addProjectIV=(ImageView)findViewById(R.id.add_project);
//        mLocationFab=(FloatingActionButton)findViewById(R.id.fab);
        userIV=(ImageView)findViewById(R.id.user);
        locationIV=(ImageView)findViewById(R.id.iv_main_location);
        allProjectIV=(ImageView)findViewById(R.id.iv_main_all_project);
        searchIV=(ImageView)findViewById(R.id.iv_main_search);
        projectPresenter=new ProjectPresenterImpl(this);
        initMap();
        projectPresenter.showUserProjectData();
        addProjectIV.setOnClickListener(this);
        userIV.setOnClickListener(this);
        allProjectIV.setOnClickListener(this);
        searchIV.setOnClickListener(this);
        Point point=new Point(118.3843557054605,28.856365950917002);
        mMapView.zoomToResolution(point,0.001373291015625);
        setLocation();
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
//        mLocationDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.OFF);
        mMyLocationListener=new MyLocationListener();
        locationIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Toast.makeText(MainActivity.this, "采用定位坐标", Toast.LENGTH_SHORT).show();
                    if (locationGraphicId != -1) {
                        mGraphicsLayer.removeGraphic(locationGraphicId);
                        locationGraphicId = -1;
                    }
                    mLocationDisplayManager = mMapView.getLocationDisplayManager();
                    mLocationDisplayManager.setLocationListener(mMyLocationListener);
                    mLocationDisplayManager.setAllowNetworkLocation(true);
                    mLocationDisplayManager.setUseCourseSymbolOnMovement(true);
                    mLocationDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);//设置模式
                    mLocationDisplayManager.start();
                    Point point = (Point) GeometryEngine.project(mLocation, egs, egs);
                    mMapView.zoomToResolution(point, 4.29153442382814E-05);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,"定位失败,请查看相关设置",Toast.LENGTH_SHORT).show();
                }
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

    @Override
    public void addNewProject() {
//        if(mLocationDisplayManager!=null&&mLocationDisplayManager.getLocation()!=null) {
            Bundle bundle = new Bundle();
            if(locationGraphicId==-1) {
                if(mLocationDisplayManager!=null&&mLocationDisplayManager.getLocation()!=null) {
                    bundle.putDouble("x", mLocationDisplayManager.getLocation().getLongitude());
                    bundle.putDouble("y", mLocationDisplayManager.getLocation().getLatitude());
                }else {
                    Toast.makeText(this,"请先定位或者选择点位",Toast.LENGTH_SHORT).show();
                    return;
                }
            }else {
                bundle.putDouble("x",changedLocation.getX());
                bundle.putDouble("y",changedLocation.getY());
            }
            Intent intent = new Intent(MainActivity.this, NewProjectActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, REQUEST_NEW_PROJECT);
//        }else{
//            Toast.makeText(this,"请先定位或者选择点位",Toast.LENGTH_SHORT).show();
//        }
//        this.finish();
    }

    @Override
    public GraphicsLayer getGraphicLayer() {
        return this.mGraphicsLayer;
    }

    @Override
    public MapView getMapView() {
        return mMapView;
    }

    @Override
    public void showUser() {
        Intent intent = new Intent(MainActivity.this, UserActivity.class);
        startActivity(intent);
    }

    @Override
    public void searchProject(String[] projectNames) {
        Intent intent=new Intent(MainActivity.this, SearchActivity.class);
        intent.putExtra("projectNames",projectNames);
        startActivityForResult(intent,REQUEST_SEARCH);
    }

    @Override
    public void showAllProject() {
        Point point=new Point(118.3843557054605,28.856365950917002);
        mMapView.zoomToResolution(point,0.001373291015625);
    }

    @Override
    public void setLocationChanged(Point point) {
        if(mLocationDisplayManager!=null) {
            mLocationDisplayManager.stop();
            mLocationDisplayManager.setLocationListener(new MyChangedLocationListener());
            mLocationDisplayManager.start();
            mLocationDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.OFF);//设置模式
        }
        addLocationGraphic(point);
    }

    private void addLocationGraphic(Point point){
        if(locationGraphicId!=-1)
            mGraphicsLayer.removeGraphic(locationGraphicId);
        else Toast.makeText(this,"采用点击坐标",Toast.LENGTH_SHORT).show();
        changedLocation=point;
        Drawable drawable=this.getResources().getDrawable(R.drawable.ic_add_location_deep_purple_900_48dp);
        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(getApplicationContext(),
                drawable);
        locationGraphic= new Graphic(changedLocation, pictureMarkerSymbol);
        locationGraphicId=mGraphicsLayer.addGraphic(locationGraphic);
//      mMyLocationListener.onLocationChanged(location);
    }



    @Override
    public Point getMapPoint(float v, float v1) {
        Point clickPoint = mMapView.toMapPoint(v, v1);
        return clickPoint;
    }

    @Override
    public SpatialReference getSpatialReference() {
        return mMapView.getSpatialReference();
    }

    @Override
    public void onClick(View view) {
        projectPresenter.onWidgetClicked(view);
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
                Log.e(TAG, "onLocationChanged: "+ p.toString());
            }
        }

        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: "+provider);
            Toast.makeText(getApplicationContext(), "GPS Disabled",
                    Toast.LENGTH_SHORT).show();
        }

        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "GPS Enabled",
                    Toast.LENGTH_SHORT).show();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: "+provider);
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
//                    initAllMembers();
                    setLocation();
                }else {
                    Toast.makeText(this,"发生未知错误",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //双击返回键退出应用
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isExit) {
                finish();
                System.exit(0);
            } else {
                isExit = true;
                ToastUtils.showShort(getApplicationContext(), "再按一次后退键退出程序");
                mHandler.sendEmptyMessageDelayed(0, 1000);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_NEW_PROJECT:
                if (resultCode == RESULT_OK) {
                    mGraphicsLayer.removeAll();
                    projectPresenter.showUserProjectData();
                }
                break;
            case REQUEST_NEW_FEEDBACK:
                if (resultCode == RESULT_OK) {
                    mGraphicsLayer.removeAll();
                    projectPresenter.showUserProjectData();
                }
                break;
            case REQUEST_SEARCH:
                if (resultCode == RESULT_OK) {
                    String projectName = data.getStringExtra("projectName");
                    Point projectLocation = projectPresenter.locateProject(projectName);
                    mMapView.zoomToResolution(projectLocation, 4.29153442382814E-05);
                }
        }
    }

    /**
     * 定位内部类
     */
    private class MyChangedLocationListener implements LocationListener {

        public MyChangedLocationListener() {
            super();
        }

        /**
         * If location changes, update our current location. If being found for
         * the first time, zoom to our current position with a resolution of 20
         */
        public void onLocationChanged(Location loc) {
//            if (loc == null)
//                return;
//            boolean zoomToMe = (changedLocation != null) ? true : false;
//            changedLocation = new Point(loc.getLongitude(), loc.getLatitude());
//            if (zoomToMe) {
//                Point p = (Point) GeometryEngine.project(changedLocation, egs, egs);
//                mMapView.zoomToResolution(p,4.29153442382814E-05);
//            }
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

}
