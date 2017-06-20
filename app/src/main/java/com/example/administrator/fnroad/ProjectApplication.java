package com.example.administrator.fnroad;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.example.administrator.fnroad.crash.ProjectCrash;
import com.example.administrator.fnroad.login.model.UserBean;
import com.example.administrator.fnroad.utils.ToastUtils;


/**
 * Created by hornkyin on 16/9/24.
 */
public class ProjectApplication extends Application {
    private static ProjectApplication instance;

    public static ProjectApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.instance = this;
        appColorMatching = getResources().getColor(R.color.app_color_matching);//得在onCreate中初始化，否则getResources()NULL-POINTER
        //初始化崩溃日志收集器
        ProjectCrash projectCrash = ProjectCrash.getInstance();
        projectCrash.setCustomCrashInfo(this);
    }

    /**
     * APP的整体背景色。set-自定义APP背景色可以使用到
     */
    public int appColorMatching;//得使用getColor，否则无法setTintColor

    public int getAppColorMatching() {
        return appColorMatching;
    }

    public void setAppColorMatching(int appColorMatching) {
        this.appColorMatching = appColorMatching;
    }

    /**
     * 对网络请求的判断。成功则判断数据是否为空；失败则提供提示消息；判断数据是否为JSONOBJECT格式
     */
    public boolean onResponseCheck(String response) {
        if (response.equals("")) {
            ToastUtils.showLong(getApplicationContext(), getResources().getString(R.string.app_toast_none_data));
            return false;
        } else return true;
    }

    public void onNetError() {
        ToastUtils.showLong(getApplicationContext(), getResources().getString(R.string.app_toast_net_wrong));
    }

    public void onDataError() {
        ToastUtils.showLong(getApplicationContext(), getResources().getString(R.string.app_toast_service_wrong));
    }

    public void onReturnSuccess() {
        ToastUtils.showLong(getApplicationContext(), getResources().getString(R.string.app_toast_return_success));
    }


    /**
     * 包含用户所有信息的实例对象。供全局存取
     */
    public UserBean userBean=null;

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public UserBean getUserBean() {
        return this.userBean;
    }

    /**
     * 释放本APP采用的Picasso类库（尤其在ListView中）每次读取网络图片加载进ImageView的bitmap资源，回收内存。
     * <p>
     * 注：为什么只回收"PicassoDrawable"类型的内存（"PicassoDrawable"是通过Picasso类库生成的Drawable，
     * 配合MemoryPolicy.NO_CACHE可以既清除Picasso产生的内存Cache，也同时回收了残留在ImageView中的
     * bitmap。如果是Picasso.error()显示图片产生的Drawable，则是BitmapDrawable类型的。如果也对error()的
     * bitmap回收，则会产生与只bitmap.recycle()不设定MemoryPolicy.NO_CACHE一样的错误，ImageView
     * 最终不会显示任何东西为空白状态，错误内容————Canvas: trying to use a recycled bitmap，原因自查）。
     *
     * @param imageView
     */
    public void releasePicassoResouceInImageView(ImageView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable && "PicassoDrawable".equals(drawable.getClass().getSimpleName())) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }
}
