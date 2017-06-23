package com.example.administrator.fnroad.main.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.fnroad.R;
import com.example.administrator.fnroad.main.model.Feedback;

import java.util.List;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public class FeedbackItemInfoAdapter extends BaseAdapter {
    private Context context;
    private List<Feedback> data;
//    private Feedback[] data;

    public FeedbackItemInfoAdapter(Context context, List<Feedback> records) {
        this.context = context;
        this.data = records;
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
        return data.get(i).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.view_feedback_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_feedback_item_time);
            viewHolder.tvFeedback = (TextView) convertView.findViewById(R.id.tv_feedback_item_feedback);
            viewHolder.tvProgress = (TextView) convertView.findViewById(R.id.tv_feedback_item_progress);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.tvTime.setText(data.get(position).getCreateTime());
        viewHolder.tvFeedback.setText(data.get(position).getDescription());
        viewHolder.tvProgress.setText(data.get(position).getProgress());
        return convertView;
    }

    private static class ViewHolder {
        TextView tvTime;
        TextView tvFeedback;
        TextView tvProgress;
    }
}
