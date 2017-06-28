package com.example.administrator.fnroad.utils.PictureViewerUtils;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.fnroad.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;


import java.io.File;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by hornkyin on 16/12/12.
 */
public class PicturePagerUtils {
    private static final String TAG = "PicturePagerUtils";
    private Context context;
    private List<Object> pictures;//图片的路径
    private int currentIndex;//当前所在页

    private Dialog dialog;//整个Util表现出来的界面

    private LazyViewPager viewPager;
    private LinearLayout progressLL;//转动进度bar
    private TextView currentPictureIndexTV;//当前index
    private TextView picturesCountTV;//图片总数
    private TextView describeContentTV;//下方的描述性文字
    private String[] classString;
    private String type;
    private String getClassString;

    public PicturePagerUtils(Context context, List<Object> pictures) {
        this.context = context;
        this.pictures = pictures;
        this.currentIndex = 0;
        initMembers();
    }

    public PicturePagerUtils(Context context, List<Object> pictures, int currentIndex) {
        this(context, pictures);
        if (currentIndex < pictures.size() && currentIndex > 0)
            this.currentIndex = currentIndex;
    }

    /**
     * 初始化类对象
     */
    private void initMembers() {
        //为图片浏览界面的Dialog初始化View
        try {
            dialog = new Dialog(context, R.style.PicturePagerFullDialog);
            RelativeLayout contentView = (RelativeLayout) View.inflate(context, R.layout.view_community_picture_view_pager, null);
            viewPager = getView(contentView, R.id.view_pager);
            progressLL = getView(contentView, R.id.ll_progress);
            currentPictureIndexTV = getView(contentView, R.id.tv_current_picture_index);
            picturesCountTV = getView(contentView, R.id.tv_pictures_count);
            describeContentTV = getView(contentView, R.id.tv_describe_content);
            dialog.setContentView(contentView);
            progressLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            //显示当前图片index与所有图片的数量
            int size = pictures.size();
            picturesCountTV.setText(String.valueOf(size));
            currentPictureIndexTV.setText(String.valueOf(currentIndex + 1));
        }catch(Exception e){
            Log.e(TAG, "initMembers: "+e);
        }
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        viewPager.setOnPageChangeListener(new LazyViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                viewPager.getCurrentItem();
                currentPictureIndexTV.setText(String.valueOf(position+1));
            }
        });
        CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter();
        viewPager.setAdapter(customPagerAdapter);
        viewPager.setCurrentItem(currentIndex);//设定当前页
    }

    /**
     * 设置图片下方的文字
     *
     * @param content
     */
    public void setContentText(String content) {
        if (!TextUtils.isEmpty(content)) describeContentTV.setText(content);
    }

    /**
     * 显示图片大图的Dialog
     */
    public void show() {
        initViewPager();
        dialog.show();
    }

    /**
     * 此处针对Picasso的加载内容，做了4种类型转换。
     * @param imageView
     * @param object
     */
    private void showPicture(ImageView imageView, Object object) {
        //开启转动的Progress Bar
        progressLL.setVisibility(View.VISIBLE);

        getClassString=null;
        getClassString=object.getClass().toString();

        classString=new String[3];
        classString=getClassString.split("\\.");
        type=null;
        type= classString[classString.length-1];
        RequestCreator requestCreator = null;
        switch (type){
            case "String" :
                String path=(String)object;
                requestCreator=Picasso.with(context).load(path);
                break;
            case "File" :
                File file=(File)object;
                requestCreator=Picasso.with(context).load(file);
                break;
            case "Uri":
                Uri uri=(Uri)object;
                requestCreator=Picasso.with(context).load(uri);
                break;
            case "int":
                int resourceId=(int)object;
                requestCreator=Picasso.with(context).load(resourceId);
                break;
        }

        if (requestCreator != null) {
            requestCreator.error(R.mipmap.icon_app_picture_error_net).into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    progressLL.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    progressLL.setVisibility(View.GONE);
                }
            });
        }
        dialog.show();
    }

    @SuppressWarnings("unchecked")
    public static final <E extends View> E getView(View parent, int id) {
        try {
            return (E) parent.findViewById(id);
        } catch (ClassCastException ex) {
            Log.e("ImagPageUtil", "Could not cast View to concrete class \n" + ex.getMessage());
            throw ex;
        }
    }

    /*********************************************内部类********************************************/

    /**
     * 内部类，自定义的PagerAdapter
     */
    class CustomPagerAdapter extends PagerAdapter {
        PhotoView tempImageView;

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            tempImageView = new PhotoView(context);
            tempImageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float v, float v1) {
                    dialog.dismiss();
                }

                @Override
                public void onOutsidePhotoTap() {
                    dialog.dismiss();
                }
            });
            showPicture(tempImageView, pictures.get(position));
            container.addView(tempImageView);
            return tempImageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(tempImageView);
        }

        @Override
        public int getCount() {
            if (null == pictures || pictures.size() <= 0) {
                return 0;
            }
            return pictures.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
