package com.example.administrator.fnroad.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 显示所有内容、无滚动条的GridView
 * 项目名称：PatrolSystem
 * Created by yanlong_lee on 2016/10/8.
 * email": 1106335691@qq.com
 * phone: 18868107459
 */

public class NoScrollGridView extends GridView {
    public NoScrollGridView(Context context) {
        super(context);
        this.setFocusable(false);
    }

    public NoScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setFocusable(false);
    }

    public NoScrollGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setFocusable(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
