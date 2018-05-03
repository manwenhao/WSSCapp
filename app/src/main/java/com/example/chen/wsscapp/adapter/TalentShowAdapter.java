package com.example.chen.wsscapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chen.wsscapp.Bean.TalentShow;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.GetTel;
import com.example.chen.wsscapp.activity.ShowshopInfoActivity;

import java.util.List;

/**
 * create by chen
 * 主页显示的商品item adapter
 * */


public class TalentShowAdapter extends RecyclerView.Adapter<TalentShowAdapter.TalentShowViewHolder> {
    private List<TalentShow> talentShowList;
    private Context context;

    static class TalentShowViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        ImageView iv_menushopimg;
        TextView tv_menushoptype;
        TextView tv_menushopname;
        TextView tv_menushopprice;
        TextView tv_menushopoldprice;


        public TalentShowViewHolder(View view) {
            super(view);
            itemView = view;
            iv_menushopimg = (ImageView) view.findViewById(R.id.iv_menushopimg);
            tv_menushoptype = (TextView) view.findViewById(R.id.tv_menushoptype);
            tv_menushopname = (TextView) view.findViewById(R.id.tv_menushopname);
            tv_menushopprice = (TextView) view.findViewById(R.id.tv_menushopprice);
            tv_menushopoldprice = (TextView) view.findViewById(R.id.tv_menushopoldprice);
            tv_menushopoldprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG );
        }
    }


    public TalentShowAdapter(Context context, List<TalentShow> talentShowList) {
        this.context = context;
        this.talentShowList=talentShowList;
    }



    @Override
    public TalentShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_home_talent_item, parent, false);
        final TalentShowViewHolder holder = new TalentShowViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition()-2;
                Log.e("pos",""+position);
                TalentShow talentShow=talentShowList.get(position);
               // Toast.makeText(v.getContext(),"点击的是："+talentShow.getPro_id()+talentShow.getPro_name(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,ShowshopInfoActivity.class);
                intent.putExtra("shopid",talentShow.getPro_id());
                context.startActivity(intent);

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(TalentShowViewHolder holder, int position) {
        TalentShow talentShow=talentShowList.get(position);
        holder.tv_menushopname.setText(talentShow.getPro_name());
        holder.tv_menushoptype.setText("["+talentShow.getPro_brand()+"]");
        holder.tv_menushopoldprice.setText("原价 ￥"+talentShow.getPro_price());
        holder.tv_menushopprice.setText("￥"+ GetTel.getFloat(talentShow.getPro_discount()*talentShow.getPro_price()));
        Glide.with(context).load("http://106.14.145.208"+talentShow.getPro_photo()).placeholder(R.drawable.shop).into(holder.iv_menushopimg);
    }

    @Override
    public int getItemCount() {
        return talentShowList.size();
    }



}
