package com.example.administrator.fnroad.feedback.presenter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.administrator.fnroad.ProjectApplication;
import com.example.administrator.fnroad.R;
import com.example.administrator.fnroad.feedback.view.INewFeedbackView;
import com.example.administrator.fnroad.project.presenter.INewProjectPresenter;
import com.example.administrator.fnroad.project.view.INewProjectView;
import com.example.administrator.fnroad.spreference.SharePrefrenceHelper;
import com.example.administrator.fnroad.utils.DateUtils;
import com.example.administrator.fnroad.utils.ImageTools;
import com.example.administrator.fnroad.utils.OkHttpUtils;

import org.codehaus.jackson.node.MissingNode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public class NewFeedbackPresenterImpl implements INewFeedbackPresenter {
    private static final String TAG = "NewFeedbackPresenterImp";
    private INewFeedbackView mNewFeedbackView;
    private int SCALE = 5;                     //照片缩小倍数
    private ArrayList<HashMap<String, Object>> imageItem;
    private int addable = 1;                   //标记是否可继续添加图片.0为否，1为是
    private String[] paths = new String[9];    //保存图片的路径
    private int indexOfImage = 0;              //paths中图片的索引
    private ProgressDialog progressDialog;
    private SharePrefrenceHelper sharePrefrenceHelper;
    private int progress;

    public NewFeedbackPresenterImpl(INewFeedbackView newFeedbackView){
        this.mNewFeedbackView=newFeedbackView;
        setProgressbar();
        sharePrefrenceHelper = SharePrefrenceHelper.getInstance(mNewFeedbackView.getActivity());
    }

    /**
     * 设置评论回复进度条
     */
    private void setProgressbar() {
        progressDialog = new ProgressDialog(mNewFeedbackView.getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("发送中，请稍候...");
    }

    @Override
    public void onWidgetClicked(View v) {
        switch (v.getId()){
            case R.id.btn_new_feedback_add_feedback:
                progressDialog.show();
                addFeedback();
                break;
            case R.id.btn_new_feedback_cancel_feedback:
                cancelFeedback();
                break;
            case R.id.iv_new_feedback_small_progress:
                smallProgress();
                break;
            case R.id.iv_new_feedback_big_progress:
                bigProgress();
                break;
            default:
                break;
        }
    }

    @Override
    public void initPictureGV() {
        Bitmap bitmap= BitmapFactory.decodeResource(mNewFeedbackView.getActivity().getResources(),R.mipmap.icon_add_picture);
        Bitmap newBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
        bitmap.recycle();
        HashMap<String, Object> map = new HashMap<>();
        map.put("itemImage", newBitmap);
        imageItem = new ArrayList<>();
        imageItem.add(map);
        SimpleAdapter simpleAdapter = new SimpleAdapter(mNewFeedbackView.getActivity(),
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
        mNewFeedbackView.setPictureGVAdapter(simpleAdapter);
    }

    @Override
    public void onGVItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == imageItem.size() - 1 && addable == 1){
            mNewFeedbackView.openAlbum();
        }
    }

    @Override
    public void addImagePathAndRefresh(String imagePath) {
        paths[indexOfImage] = imagePath;
        indexOfImage++;
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
            SimpleAdapter simpleAdapter = new SimpleAdapter(mNewFeedbackView.getActivity(),
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
            mNewFeedbackView.setPictureGVAdapter(simpleAdapter);
//            gridView.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
            //刷新后释放防止手机休眠后自动添加
            imagePath = null;
        }
    }

    private void addFeedback(){
        OkHttpUtils.Param[] params=new OkHttpUtils.Param[6];
        params[0]=new OkHttpUtils.Param("STATUS","1");
        params[1]=new OkHttpUtils.Param("PROJECTID",sharePrefrenceHelper.getStringValue("PROJECT_ID"));
        params[2]=new OkHttpUtils.Param("UPDATE_AMOUNT",mNewFeedbackView.getUpdateAmount());
        params[3]=new OkHttpUtils.Param("PROGRESS",mNewFeedbackView.getProgress());
        params[4]=new OkHttpUtils.Param("DESCRIPTION",mNewFeedbackView.getDescription());
        params[5]=new OkHttpUtils.Param("CREATE_TIME", DateUtils.getCurrentTime());
        String url= mNewFeedbackView.getActivity().getString(R.string.php_service_url)+ "Feedback/add_feedback";
        OkHttpUtils.postAsyn(url, params, new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                ProjectApplication.getInstance().onNetError();
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String feedbackId=jsonObject.getString("feedback_id");
                    String projectId=sharePrefrenceHelper.getStringValue("PROJECT_ID");
                    if(paths[0]!=null){
                        final String urlOfImage=mNewFeedbackView.getActivity().getString(R.string.php_service_url)+"Feedback/upload_pic";
                        Map<String,String> paramsOfImage=new HashMap<String, String>();
                        paramsOfImage.put("PROJECTID",projectId);
                        paramsOfImage.put("FEEDBACKID",feedbackId);
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
                                Toast.makeText(mNewFeedbackView.getActivity(),"照片上传失败",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(mNewFeedbackView.getActivity(),"照片上传成功",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                mNewFeedbackView.getActivity().setResult(Activity.RESULT_OK);
                                mNewFeedbackView.getActivity().finish();
//                                newProjectView.getActivity().finish();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "onResponse: "+e);
                }
            }
        });
    }

    /**
     * 取消反馈
     */
    private void cancelFeedback(){
        mNewFeedbackView.getActivity().finish();
    }

    private void smallProgress(){
        progress=Integer.valueOf(mNewFeedbackView.getProgress().split("%")[0]);
        if(progress>0){
            progress=progress-10;
            mNewFeedbackView.setProgress(progress+"%");
        }
    }

    private void bigProgress(){
        progress=Integer.valueOf(mNewFeedbackView.getProgress().split("%")[0]);
        if(progress<100){
            progress=progress+10;
            mNewFeedbackView.setProgress(progress+"%");
        }
    }
}
