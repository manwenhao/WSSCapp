package com.example.chen.wsscapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.MyApplication;
import com.github.chrisbanes.photoview.PhotoView;

/**
 * Created by xuxuxiao on 2018/4/17.
 */

public class WTGridviewPhotoviewAdapter extends BaseAdapter {
    private String listUrls[];
    private LayoutInflater inflater;

    public WTGridviewPhotoviewAdapter() {
    }

    public WTGridviewPhotoviewAdapter(String listUrls[] , Context context) {
        this.listUrls = listUrls;
        inflater = LayoutInflater.from(context);
    }

    public int getCount(){
        return listUrls.length;
    }
    @Override
    public String getItem(int position) {
        return listUrls[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            //convertView=inflater.inflate(R.layout.item)
            convertView = inflater.inflate(R.layout.wt_gridview_photoview_item, parent,false);
            holder.image = (PhotoView) convertView.findViewById(R.id.wt_photo_imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        if (listUrls!=null){

            Glide.with(MyApplication.getContext())
                    .load(listUrls[position])
                    .crossFade()
                    .centerCrop()
                    .placeholder(R.drawable.loading)
                    .animate(android.R.anim.slide_in_left)
                    .error(R.drawable.imgerror)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.image);

        }
        return convertView;
    }
    class ViewHolder {
        PhotoView image;
    }
}
