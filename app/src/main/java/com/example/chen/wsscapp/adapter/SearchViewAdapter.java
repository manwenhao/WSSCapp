package com.example.chen.wsscapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chen.wsscapp.Bean.GetProduct;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.GetTel;
import com.example.chen.wsscapp.activity.ShowshopInfoActivity;

import java.util.List;

/**
 * Created by chen on 2018/4/23.
 */

public class SearchViewAdapter extends RecyclerView.Adapter<SearchViewAdapter.ViewHolder> {

    private List<GetProduct> mproList;
    private Context context;

    public SearchViewAdapter(Context context,List<GetProduct> mproList){
        this.context = context;
        this.mproList = mproList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.searchpro_item,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = viewHolder.getAdapterPosition();
                GetProduct getProduct = mproList.get(pos);
                Intent intent = new Intent(context,ShowshopInfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                intent.putExtra("shopid",getProduct.getPro_id());
                context.startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GetProduct getProduct = mproList.get(position);
        Log.e("imgurl   ",getProduct.getPro_photo());
        String[] img = getProduct.getPro_photo().split(";");
        Glide.with(context).load("http://106.14.145.208"+img[0]).placeholder(R.drawable.shop).into(holder.iv_proPhoto);
        holder.tv_proType.setText("["+getProduct.getPro_brand()+"]");
        holder.tv_proName.setText(getProduct.getPro_name());
        holder.tv_proPrice.setText("￥"+ GetTel.getFloat(Float.parseFloat(getProduct.getPro_price())*Float.parseFloat(getProduct.getPro_discount())));
        holder.tv_prooldPrice.setText("￥"+getProduct.getPro_price());

    }

    @Override
    public int getItemCount() {
        return mproList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        View searchView;
        ImageView iv_proPhoto;
        TextView tv_proType;
        TextView tv_proName;
        TextView tv_proPrice;
        TextView tv_prooldPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            searchView = itemView;
            iv_proPhoto = (ImageView) itemView.findViewById(R.id.iv_proPhoto);
            tv_proType = (TextView) itemView.findViewById(R.id.tv_proType);
            tv_proName = (TextView) itemView.findViewById(R.id.tv_proName);
            tv_proPrice = (TextView) itemView.findViewById(R.id.tv_proPrice);
            tv_prooldPrice = (TextView) itemView.findViewById(R.id.tv_proOldprice);
        }
    }


}
