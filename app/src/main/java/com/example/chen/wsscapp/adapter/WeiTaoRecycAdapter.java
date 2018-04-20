package com.example.chen.wsscapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chen.wsscapp.Bean.WeiTaoBean;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.MyApplication;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by xuxuxiao on 2018/4/17.
 */

public class WeiTaoRecycAdapter extends RecyclerView.Adapter<WeiTaoRecycAdapter.ViewHolder> {
    private List<WeiTaoBean> list;
    private LayoutInflater inflater;
    private OnItemClickListener mOnItemClickListener;

    public WeiTaoRecycAdapter() {
    }

    public WeiTaoRecycAdapter(List<WeiTaoBean> list, Context context) {
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }
    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this. mOnItemClickListener=onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.weitao_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, final int i) {
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
            vh.gridView.setVisibility(View.VISIBLE);
            String a[]=list.get(i).getDyn_photots().split(";");
            for (int ii = 0; ii < a.length; ii++) {
                String http = "http://106.14.145.208:80";
                a[ii] = http + a[ii];
            }
            WTGridViewAdapter adapter=new WTGridViewAdapter(a,MyApplication.getContext());
            vh.gridView.setAdapter(adapter);
        }else {
            vh.gridView.setVisibility(View.GONE);
        }

        vh.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int ii, long l) {
                mOnItemClickListener.onClick(i);
            }
        });
        if (mOnItemClickListener!=null){
            vh.itv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onClick(i);
                }
            });
            vh.itv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemClickListener.onLongClick(i);
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setdate(List<WeiTaoBean> list){
        this.list=list;
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        View itv;
        CircleImageView image;
        TextView name,time,content;
        GridView gridView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itv=itemView;
            this.image = (CircleImageView)itemView.findViewById(R.id.wt_item_touxiang);
            this.name = (TextView)itemView.findViewById(R.id.wt_item_name);
            this.time = (TextView)itemView.findViewById(R.id.wt_item_time);
            this.content = (TextView)itemView.findViewById(R.id.wt_words_cont);
            this.gridView = (GridView)itemView.findViewById(R.id.wt_gridView);
        }
    }

}
