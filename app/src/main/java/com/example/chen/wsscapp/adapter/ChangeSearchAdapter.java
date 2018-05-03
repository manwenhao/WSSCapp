package com.example.chen.wsscapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chen.wsscapp.Bean.GetProduct;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.GetTel;

import java.util.List;

/**
 * Created by chen on 2018/4/29.
 */

public class ChangeSearchAdapter extends BaseAdapter {
    private List<GetProduct> list;
    private Context context;

    public ChangeSearchAdapter() {

    }

    public ChangeSearchAdapter(List<GetProduct> list, Context context) {
        super();
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        ViewHolder viewHolder;
        if(convertView==null){
            view = LayoutInflater.from(context).inflate(R.layout.searchpro_item,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.searchView = view;
            viewHolder.iv_proPhoto = (ImageView) view.findViewById(R.id.iv_proPhoto);
            viewHolder.tv_proType = (TextView) view.findViewById(R.id.tv_proType);
            viewHolder.tv_proName = (TextView) view.findViewById(R.id.tv_proName);
            viewHolder.tv_proPrice = (TextView) view.findViewById(R.id.tv_proPrice);
            viewHolder.tv_prooldPrice = (TextView) view.findViewById(R.id.tv_proOldprice);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        GetProduct getProduct = list.get(position);
        Log.e("imgurl   ",getProduct.getPro_photo());
        String[] img = getProduct.getPro_photo().split(";");
        Glide.with(context).load("http://106.14.145.208"+img[0]).placeholder(R.drawable.shop).into(viewHolder.iv_proPhoto);
        viewHolder.tv_proType.setText("["+getProduct.getPro_brand()+"]");
        viewHolder.tv_proName.setText(getProduct.getPro_name());
        viewHolder.tv_proPrice.setText("￥"+ GetTel.getFloat(Float.parseFloat(getProduct.getPro_price())*Float.parseFloat(getProduct.getPro_discount())));
        viewHolder.tv_prooldPrice.setText("￥"+getProduct.getPro_price());
        return view;
    }

    class ViewHolder{
        View searchView;
        ImageView iv_proPhoto;
        TextView tv_proType;
        TextView tv_proName;
        TextView tv_proPrice;
        TextView tv_prooldPrice;
    }
}
