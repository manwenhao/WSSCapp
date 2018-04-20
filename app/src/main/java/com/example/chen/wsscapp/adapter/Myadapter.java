package com.example.chen.wsscapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.chen.wsscapp.Bean.GridItem;
import com.example.chen.wsscapp.R;

import java.util.ArrayList;


/**
 * Created by chen on 2018/4/9.
 */

public class Myadapter extends ArrayAdapter {
    String TAG = "TESTS";
    private int id;
    Context context;
    private ArrayList<GridItem> mGridData = new ArrayList<GridItem>();

    public Myadapter(Context context, int resource, ArrayList<GridItem> mGridData) {
        super(context, resource,mGridData);
        this.context = context;
        id = resource;
        this.mGridData = mGridData;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(id, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.iv_tu);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GridItem item = mGridData.get(position);
        Log.e(TAG,mGridData.size()+"长度");
        Log.e(TAG,mGridData.toString());
        Log.e(TAG,item.getImageView()+"");
        Glide.with(getContext()).load(item.getImageView()).into(holder.imageView);
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
    }


}
