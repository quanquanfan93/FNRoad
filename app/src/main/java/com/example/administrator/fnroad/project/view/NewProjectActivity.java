package com.example.administrator.fnroad.project.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.administrator.fnroad.R;
import com.example.administrator.fnroad.project.presenter.INewProjectPresenter;
import com.example.administrator.fnroad.project.presenter.NewProjectPresenterImpl;

import java.util.List;

public class NewProjectActivity extends AppCompatActivity implements INewProjectView,View.OnClickListener,GridView.OnItemClickListener {

    private EditText projectNameET;
    private Spinner roadTypeSP;
    private Spinner projectTypeSP;
    private EditText projectDescriptionET;
    private GridView picturesGV;
    private Button addProjectBTN;
    private Button cancelProjectBTN;
    private INewProjectPresenter newProjectPresenter;
    private double x;
    private double y;
    private final int IMAGE_OPEN = 1;        //打开图片标记
    private String  pathImage=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);
        projectNameET=(EditText)findViewById(R.id.edt_project_name);
        roadTypeSP=(Spinner)findViewById(R.id.sp_road_type);
        projectTypeSP=(Spinner)findViewById(R.id.sp_project_type);
        projectDescriptionET=(EditText)findViewById(R.id.edt_project_description);
        picturesGV=(GridView)findViewById(R.id.gv_pictures);
        addProjectBTN=(Button)findViewById(R.id.btn_add_project);
        cancelProjectBTN=(Button)findViewById(R.id.btn_cancel_project);
        newProjectPresenter=new NewProjectPresenterImpl(this);
        addProjectBTN.setOnClickListener(this);
        cancelProjectBTN.setOnClickListener(this);
        Bundle bundle=new Bundle();
        bundle=getIntent().getExtras();
        x=bundle.getDouble("x");
        y=bundle.getDouble("y");
        newProjectPresenter.getTypeData();
        newProjectPresenter.initPictureGV();
        picturesGV.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        newProjectPresenter.onWidgetClicked(view);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    /**
     * 设置两个SP的item
     * @param roadTypeList
     * @param projectTypeList
     */
    @Override
    public void setAdapter(List<String> roadTypeList,List<String> projectTypeList) {
        ArrayAdapter<String> roadAdapter = new ArrayAdapter<String>(this.getApplicationContext(), android.R.layout.simple_spinner_item, roadTypeList);
        roadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roadTypeSP.setAdapter(roadAdapter);
        ArrayAdapter<String> projectAdapter = new ArrayAdapter<String>(this.getApplicationContext(), android.R.layout.simple_spinner_item, projectTypeList);
        projectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        projectTypeSP.setAdapter(projectAdapter);
    }

    @Override
    public void setPictureGVAdapter(SimpleAdapter simpleAdapter) {
        picturesGV.setAdapter(simpleAdapter);
    }

    @Override
    public void openAlbum() {
        Toast.makeText(getActivity(), "添加图片", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_OPEN);
    }

    @Override
    public String getProjectName() {
        return projectNameET.getText().toString();
    }

    @Override
    public String getRoadName() {
        return roadTypeSP.getSelectedItem().toString();
    }

    @Override
    public String getProjectType() {
        return String.valueOf(projectTypeSP.getSelectedItemPosition()+1);
    }

    @Override
    public String getProjectDescription() {
        return projectDescriptionET.getText().toString();
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        newProjectPresenter.onGVItemClick(adapterView,view,i,l);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //打开图片
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_OPEN) {
            //判断手机版本号
            if(Build.VERSION.SDK_INT>=19){
                handleImageOnKitKat(data);
            }else{
                handleImageBeforeKitKat(data);
            }
        }  //end if 打开图片
    }

    /**
     * 4.4及以上系统使用这个方法处理图片
     * @param data
     */
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            //如果document类型的Uri,则通过document id处理
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                pathImage = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                pathImage=getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的Uri，则使用普通方式处理
            pathImage=getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的Uri，直接获取图片路径即可
            pathImage=uri.getPath();
        }
        newProjectPresenter.addImagePathAndRefresh(pathImage);
//        communityNewPostPresenter.addImagePath(pathImage);
//        communityNewPostPresenter.refreshPicture();
    }

    /**
     * 4.4以下系统使用这个方法处理图片
     * @param data
     */
    private void handleImageBeforeKitKat(Intent data){
        Uri uri=data.getData();
        pathImage=getImagePath(uri,null);
        newProjectPresenter.addImagePathAndRefresh(pathImage);
//      communityNewPostPresenter.addImagePath(pathImage);
//      communityNewPostPresenter.refreshPicture();
    }

    /**
     * 获取图片路径
     * @param externalContentUri
     * @param selection
     * @return
     */
    private String getImagePath(Uri externalContentUri, String selection) {
        String path=null;
        Cursor cursor=getActivity().getContentResolver().query(externalContentUri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

}
