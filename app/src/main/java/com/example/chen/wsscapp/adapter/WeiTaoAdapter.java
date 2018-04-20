package com.example.chen.wsscapp.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chen.wsscapp.Bean.WeiTaoBean;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.MyApplication;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by xuxuxiao on 2018/4/16.
 */

public class WeiTaoAdapter extends BaseAdapter {
    private List<WeiTaoBean> list;
    private LayoutInflater inflater;

    public WeiTaoAdapter() {
    }

    public WeiTaoAdapter(List<WeiTaoBean> list, Context context) {
        this.list = list;
       inflater=LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if (view==null){
            view=inflater.inflate(R.layout.weitao_item,viewGroup,false);
            vh=new ViewHolder();
            vh.image=(CircleImageView)view.findViewById(R.id.wt_item_touxiang);
            vh.name=(TextView)view.findViewById(R.id.wt_item_name);
            vh.content=(TextView)view.findViewById(R.id.wt_words_cont);
            vh.time=(TextView)view.findViewById(R.id.wt_item_time);
            vh.gridView=(GridView)view.findViewById(R.id.wt_gridView);
            view.setTag(vh);
        }else {
            vh=(ViewHolder)view.getTag();
        }
        if (TextUtils.isEmpty(list.get(i).getMster_touxiang())){
            Glide.with(MyApplication.getContext())
                    .load(R.drawable.imgerror)
                    .into(vh.image);
        }else {
            Glide.with(MyApplication.getContext())
                    .load("http://106.14.145.208:80"+list.get(i).getMster_touxiang())
                    .into(vh.image);
        }
        if (TextUtils.isEmpty(list.get(i).getMster_name())){
            vh.name.setText("暂无姓名");
        }else {
            vh.name.setText(list.get(i).getMster_name());
        }
        if (TextUtils.isEmpty(list.get(i).getUptime())){
            vh.time.setText("暂无时间");
        }else {
            vh.time.setText(list.get(i).getUptime());
        }
        if (TextUtils.isEmpty(list.get(i).getDyn_content())){
            vh.content.setText("暂无内容");
        }else {
            vh.content.setText(list.get(i).getDyn_content());
        }
        if (!TextUtils.isEmpty(list.get(i).getDyn_photots())){
            String a[]=list.get(i).getDyn_photots().split(";");
            for (int ii = 0; ii < a.length; ii++) {
                String http = "http://106.14.145.208:80";
                a[ii] = http + a[ii];
            }

            WTGridViewAdapter adapter=new WTGridViewAdapter(a,MyApplication.getContext());
            vh.gridView.setAdapter(adapter);
        }

        return view;
    }
    class ViewHolder{
        CircleImageView image;
        TextView name,time,content;
        GridView gridView;
    }
    public void setdate(List<WeiTaoBean> list){
        this.list=list;
    }
}
