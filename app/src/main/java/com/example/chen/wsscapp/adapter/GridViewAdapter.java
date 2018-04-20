package com.example.chen.wsscapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chen.wsscapp.Bean.Items;
import com.example.chen.wsscapp.R;

import java.util.List;


/**
 * Created by chen on 2018/3/1.
 */

public class GridViewAdapter  extends ArrayAdapter<Items> {
    private int resourceId;
    private List<Items> data;

    public GridViewAdapter(Context context, int textViewResourceId, List<Items> data) {
        super(context,textViewResourceId,data);
        resourceId = textViewResourceId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Items items = getItem(position);
        ViewHolder holder = null;
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(
                    R.layout.item, null);
            holder = new ViewHolder();
            holder.iv = (ImageView) view
                    .findViewById(R.id.ItemImage);
            holder.tv = (TextView) view.findViewById(R.id.ItemText);
            view.setTag(holder);// 如果convertView为空就 把holder赋值进去
        } else {
            view = convertView;
            holder = (ViewHolder) convertView.getTag();// 如果convertView不为空，那么就在convertView中getTag()拿出来
        }
        holder.iv.setImageResource(items.getImageId());
        holder.tv.setText(items.getText());
        return view;
    }

     class ViewHolder {
        ImageView iv;
        TextView tv;
    }
}
