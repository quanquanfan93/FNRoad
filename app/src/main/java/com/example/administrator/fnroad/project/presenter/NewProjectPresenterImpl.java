package com.example.administrator.fnroad.project.presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.administrator.fnroad.ProjectApplication;
import com.example.administrator.fnroad.R;
import com.example.administrator.fnroad.main.model.Project;
import com.example.administrator.fnroad.main.model.ProjectType;
import com.example.administrator.fnroad.project.view.INewProjectView;
import com.example.administrator.fnroad.utils.DateUtils;
import com.example.administrator.fnroad.utils.ImageTools;
import com.example.administrator.fnroad.utils.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by Administrator on 2017/6/22 0022.
 */

public class NewProjectPresenterImpl implements INewProjectPresenter{
    private static final String TAG = "NewProjectPresenterImpl";
    private INewProjectView newProjectView;
    private List<ProjectType> mProjectTypeList=new ArrayList<>();
    private int SCALE = 5;                     //照片缩小倍数
    private ArrayList<HashMap<String, Object>> imageItem;
    private int addable = 1;                   //标记是否可继续添加图片.0为否，1为是
    private String[] paths = new String[9];    //保存图片的路径
    private int indexOfImage = 0;              //paths中图片的索引
    private ProgressDialog progressDialog;
    private SimpleAdapter simpleAdapter;

    public NewProjectPresenterImpl(INewProjectView newProjectView){
        this.newProjectView=newProjectView;
        setProgressbar();
    }

    /**
     * 设置评论回复进度条
     */
    private void setProgressbar() {
        progressDialog = new ProgressDialog(newProjectView.getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("发送中，请稍候...");
    }

    @Override
    public void onWidgetClicked(View v) {
        switch (v.getId()){
            case R.id.btn_add_project:
                progressDialog.show();
                addProject();
                break;
            case R.id.btn_cancel_project:
                cancelProject();
                break;

        }
    }

    @Override
    public void getTypeData() {
        try {
//            OkHttpUtils.Param[] param = new OkHttpUtils.Param[1];
            String url = newProjectView.getActivity().getString(R.string.php_service_url) + "ProjectType/all_type";
            OkHttpUtils.getAsyn(url, new OkHttpUtils.ResultCallback<String>() {
                @Override
                public void onError(Request request, Exception e) {
                    ProjectApplication.getInstance().onNetError();
                }

                @Override
                public void onResponse(String response) {
                    Gson gson=new Gson();
                   mProjectTypeList=gson.fromJson(response,new TypeToken<List<ProjectType>>(){}.getType());
                    setSpinnerItem();
                }
            });
        }catch (Exception e){
            Log.e(TAG, "showUserProjectData: "+e);
        }
    }

    /**
     * 设置照片GridView的Adapter
     */
    @Override
    public void initPictureGV() {
        Bitmap bitmap= BitmapFactory.decodeResource(newProjectView.getActivity().getResources(),R.mipmap.icon_add_picture);
        Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
        bitmap.recycle();
        HashMap<String, Object> map = new HashMap<>();
        map.put("itemImage", newBitmap);
        imageItem = new ArrayList<>();
        imageItem.add(map);
        simpleAdapter = new SimpleAdapter(newProjectView.getActivity(),
                imageItem, R.layout.grid_item_picture,
                new String[]{"itemImage"}, new int[]{R.id.iv_grid_photo});
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView i = (ImageView) view;
                    i.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        newProjectView.setPictureGVAdapter(simpleAdapter);
    }

    @Override
    public void onGVItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == imageItem.size() - 1 && addable == 1){
            newProjectView.openAlbum();
        }else {
            dialog(i);
        }
    }

    @Override
    public void addImagePathAndRefresh(String imagePath) {
        paths[indexOfImage] = imagePath;
        indexOfImage++;
        refresh(imagePath);
    }

    private void refresh(String imagePath){
        if (!TextUtils.isEmpty(imagePath)) {
            //根据图片地址获得bitmap
            Bitmap addbmp = BitmapFactory.decodeFile(imagePath);
            Bitmap newAddBmp = ImageTools.zoomBitmap(addbmp, addbmp.getWidth() / SCALE, addbmp.getHeight() / SCALE);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("itemImage", newAddBmp);
            imageItem.add(map);
            //size为imageItem中图片的张数
            int size = imageItem.size();
            if (size < 10) {
                //交换imageItem中倒数的两张图片
                HashMap<String, Object> temp;
                temp = imageItem.get(size - 1);
                imageItem.set(size - 1, imageItem.get(size - 2));
                imageItem.set(size - 2, temp);
            } else if (size == 10) {
                imageItem.remove(size - 2);//移除倒数第二张，也就是+
                addable = 0;
            }
            simpleAdapter = new SimpleAdapter(newProjectView.getActivity(),
                    imageItem, R.layout.grid_item_picture,
                    new String[]{"itemImage"}, new int[]{R.id.iv_grid_photo});
            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data,
                                            String textRepresentation) {
                    if (view instanceof ImageView && data instanceof Bitmap) {
                        ImageView i = (ImageView) view;
                        i.setImageBitmap((Bitmap) data);
                        return true;
                    }
                    return false;
                }
            });
            newProjectView.setPictureGVAdapter(simpleAdapter);
//            gridView.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
            //刷新后释放防止手机休眠后自动添加
            newProjectView.setImagePath(null);
//            imagePath = null;
        }
    }



    /**
     * 设置两个spinner对应的list
     */
    private void setSpinnerItem() {
        ArrayList<String> roadTypeList=new ArrayList<>();
        roadTypeList.add("330国道");
        roadTypeList.add("22省道");
        roadTypeList.add("37省道");
        roadTypeList.add("39省道");
        roadTypeList.add("62省道");
        roadTypeList.add("106县道");
        roadTypeList.add("410县道");
        roadTypeList.add("507县道");
        roadTypeList.add("517县道");
        roadTypeList.add("525县道");
        roadTypeList.add("803县道");
        roadTypeList.add("804县道");
        roadTypeList.add("811县道");
        roadTypeList.add("819县道");
        ArrayList<String> projectTypeList=new ArrayList<>();
        for(ProjectType projectType:mProjectTypeList){
            projectTypeList.add(projectType.getType());
        }
        newProjectView.setAdapter(roadTypeList,projectTypeList);
    }

    /**
     * 提交立项申请
     */
    private void addProject(){
        OkHttpUtils.Param[] params=new OkHttpUtils.Param[11];
        params[0]=new OkHttpUtils.Param("STATUS","1");
        params[1]=new OkHttpUtils.Param("PROJECT_NAME",newProjectView.getProjectName());
        params[2]=new OkHttpUtils.Param("ROAD_NAME",newProjectView.getRoadName());
        params[3]=new OkHttpUtils.Param("PROJECT_TYPE",newProjectView.getProjectType(mProjectTypeList));
        params[4]=new OkHttpUtils.Param("DESCRIPTION",newProjectView.getProjectDescription());
        params[5]=new OkHttpUtils.Param("CREATE_TIME",DateUtils.getCurrentTime());
        params[6]=new OkHttpUtils.Param("X", String.valueOf(newProjectView.getX()));
        params[7]=new OkHttpUtils.Param("Y",String.valueOf(newProjectView.getY()));
        params[8]=new OkHttpUtils.Param("PATROL_MANAGER",String.valueOf(ProjectApplication.getInstance().getUserBean().getUserId()));
        params[9]=new OkHttpUtils.Param("ACTUAL_AMOUNT","0");
        params[10]=new OkHttpUtils.Param("ESTIMATED_AMOUNT","0");
        String url= newProjectView.getActivity().getString(R.string.php_service_url)+ "Project/add_project";
        OkHttpUtils.postAsyn(url, params, new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                ProjectApplication.getInstance().onNetError();
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String projectId=jsonObject.getString("project_id");
                    if(paths[0]!=null){
                        final String urlOfImage=newProjectView.getActivity().getString(R.string.php_service_url)+"Project/upload_pic";
                        Map<String,String> paramsOfImage=new HashMap<String, String>();
                        paramsOfImage.put("PROJECTID",projectId);
                        int j=0;
                        while (paths[j]!=null){
                            j++;
                        }
                        String[] paths2=new String[j];
                        for(int i=0;i<j;i++){
                            paths2[i]=paths[i];
                        }
                        List<String> pathList=new ArrayList<String>();
                        Collections.addAll(pathList,paths2);

                        OkHttpUtils.postAsyn(urlOfImage, pathList, paramsOfImage, 5, new OkHttpUtils.ResultCallback<String>() {
                            @Override
                            public void onError(Request request, Exception e) {
                                ProjectApplication.getInstance().onNetError();
                                Toast.makeText(newProjectView.getActivity(),"照片上传失败",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(newProjectView.getActivity(),"照片上传成功",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                newProjectView.getActivity().setResult(Activity.RESULT_OK);
                                newProjectView.getActivity().finish();
//                                newProjectView.getActivity().finish();
                            }
                        });
                    }else{
                        progressDialog.dismiss();
                        newProjectView.getActivity().setResult(Activity.RESULT_OK);
                        newProjectView.getActivity().finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "onResponse: "+e);
                    progressDialog.dismiss();
                    newProjectView.getActivity().setResult(Activity.RESULT_OK);
                    newProjectView.getActivity().finish();
                }
            }
        });
    }

    /**
     * 取消立项申请
     */
    private void cancelProject(){
        newProjectView.getActivity().finish();
    }

    /*
 * Dialog对话框提示用户删除操作
 * position为删除图片位置
 */
    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(newProjectView.getActivity());
        builder.setMessage("确认移除已添加图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (position == 8) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("itemImage", R.mipmap.icon_add_picture);
                    imageItem.remove(position);
                    imageItem.add(map);
                    simpleAdapter.notifyDataSetChanged();
                    addable = 1;
                } else {
                    dialog.dismiss();
                    imageItem.remove(position);
                    simpleAdapter.notifyDataSetChanged();
                }
                //从删除的图片的位置起始，后面的图片地址向前移
                if (position < 8) {
                    for (int i = position + 1; i <= 8; i++) {
                        paths[i - 1] = paths[i];
                    }
                    paths[8] = null;
                } else if (position == 8) {
                    paths[8] = null;
                }
                indexOfImage--;
//                refresh(newProjectView.getImagePath());
//                addImagePathAndRefresh(newProjectView.getImagePath());
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
