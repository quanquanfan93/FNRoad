package com.example.administrator.fnroad.feedback.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.fnroad.R;
import com.example.administrator.fnroad.feedback.presenter.INewFeedbackPresenter;
import com.example.administrator.fnroad.feedback.presenter.NewFeedbackPresenterImpl;
import com.example.administrator.fnroad.main.model.Feedback;
import com.example.administrator.fnroad.main.view.IProjectFeedbackView;
import com.example.administrator.fnroad.spreference.SharePrefrenceHelper;

import org.w3c.dom.Text;

public class NewFeedbackActivity extends AppCompatActivity implements INewFeedbackView,GridView.OnItemClickListener,View.OnClickListener{

    private INewFeedbackPresenter mNewFeedbackPresenter;
    private TextView projectNameTV;
    private TextView roadNameTV;
    private TextView projectTypeTV;
    private TextView projectDescriptionTV;
    private TextView actualAmountTV;
    private EditText updateAmountET;
    private ImageView smallProgressIV;
    private EditText progressET;
    private ImageView bigProgressIV;
    private EditText descriptionET;
    private GridView updatePicturesGV;
    private Button addFeedbackBTN;
    private Button cancelFeedbackBTN;
    private SharePrefrenceHelper sharePrefrenceHelper;
    private final int IMAGE_OPEN = 1;        //打开图片标记
    private String  pathImage=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_feedback);
        initAllView();
    }

    private void initAllView(){
        sharePrefrenceHelper = SharePrefrenceHelper.getInstance(this);
        projectNameTV=(TextView)findViewById(R.id.tv_new_feedback_project_name);
        roadNameTV=(TextView)findViewById(R.id.tv_new_feedback_road_name);
        projectTypeTV=(TextView)findViewById(R.id.tv_new_feedback_project_type);
        projectDescriptionTV=(TextView)findViewById(R.id.tv_new_feedback_project_description);
        actualAmountTV=(TextView)findViewById(R.id.tv_new_feedback_actual_amount);
        updateAmountET=(EditText)findViewById(R.id.edt_new_feedback_update_amount);
        smallProgressIV=(ImageView)findViewById(R.id.iv_new_feedback_small_progress);
        progressET=(EditText)findViewById(R.id.edt_new_feedback_progress);
        bigProgressIV=(ImageView)findViewById(R.id.iv_new_feedback_big_progress);
        descriptionET=(EditText)findViewById(R.id.edt_new_feedback_description);
        updatePicturesGV=(GridView)findViewById(R.id.gv_new_feedback_update_pictures);
        addFeedbackBTN=(Button)findViewById(R.id.btn_new_feedback_add_feedback);
        cancelFeedbackBTN=(Button)findViewById(R.id.btn_new_feedback_cancel_feedback);
        projectNameTV.setText(sharePrefrenceHelper.getStringValue("PROJECT_NAME"));
        roadNameTV.setText(sharePrefrenceHelper.getStringValue("ROAD_NAME"));
        projectTypeTV.setText(sharePrefrenceHelper.getStringValue("TYPE_NAME"));
        projectDescriptionTV.setText(sharePrefrenceHelper.getStringValue("DESCRIPTION"));
        actualAmountTV.setText(sharePrefrenceHelper.getStringValue("ACTUAL_AMOUNT"));
        progressET.setText(sharePrefrenceHelper.getStringValue("PROGRESS")+"%");
        smallProgressIV.setOnClickListener(this);
        bigProgressIV.setOnClickListener(this);
        addFeedbackBTN.setOnClickListener(this);
        cancelFeedbackBTN.setOnClickListener(this);
        mNewFeedbackPresenter=new NewFeedbackPresenterImpl(this);
        mNewFeedbackPresenter.initPictureGV();
        updatePicturesGV.setOnItemClickListener(this);
    }


    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void setPictureGVAdapter(SimpleAdapter simpleAdapter) {
        updatePicturesGV.setAdapter(simpleAdapter);
    }

    @Override
    public void openAlbum() {
        Toast.makeText(getActivity(), "添加图片", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_OPEN);
    }

    @Override
    public String getUpdateAmount() {
        return updateAmountET.getText().toString();
    }

    @Override
    public String getProgress() {
        return progressET.getText().toString();
    }

    @Override
    public String getDescription() {
        return descriptionET.getText().toString();
    }

    @Override
    public void setProgress(String progress) {
        progressET.setText(progress);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mNewFeedbackPresenter.onGVItemClick(adapterView,view,i,l);
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
        mNewFeedbackPresenter.addImagePathAndRefresh(pathImage);
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
        mNewFeedbackPresenter.addImagePathAndRefresh(pathImage);
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

    @Override
    public void onClick(View view) {
        mNewFeedbackPresenter.onWidgetClicked(view);
    }
}
