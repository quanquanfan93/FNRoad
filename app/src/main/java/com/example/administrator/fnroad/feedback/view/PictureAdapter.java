package com.example.administrator.fnroad.feedback.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.example.administrator.fnroad.R;
import com.example.administrator.fnroad.utils.PixelUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/24 0024.
 */

public class PictureAdapter extends BaseAdapter {
    private Context context;
    private List<String> data;
    private String baseUrl;
    private ViewHolder viewHolder;

    /**
     * @param context   当前活动的上下文
     * @param projectId 当前需查记录所属项目的id
     * @param paths     巡查记录的照片名
     */
    public PictureAdapter(Context context, int projectId, int feedbackId, String[] paths) {
        this.context = context;
        this.data = new ArrayList<>();
        for (String item : paths)
            this.data.add(item);
        baseUrl = context.getString(R.string.php_service_picture_url) + "Uploads/" + projectId + "/feedback_picture/" + feedbackId + "/";
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.grid_item_picture, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.ivPhoto = (ImageView) view.findViewById(R.id.iv_grid_photo);
//            viewHolder.ivDelete = (ImageView) view.findViewById(R.id.iv_detail_delete);
            view.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) view.getTag();
        final ImageView iv = viewHolder.ivPhoto;
        Target<Bitmap> target = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                ViewGroup.LayoutParams params = iv.getLayoutParams();
                params.width = PixelUtils.getWindowWidth() / 5;
                params.height = params.width;
                iv.setImageBitmap(resource);
            }
        };
        Glide.with(context).load(baseUrl + data.get(i)).asBitmap().thumbnail(0.05f).into(target);
        return view;
    }

    /**
     * 移除位置position的图片并刷新视图
     *
     * @param position 待移除图片的位置
     */
    public void remove(int position) {
        data.remove(position);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        ImageView ivPhoto;
//        ImageView ivDelete;
    }
}
