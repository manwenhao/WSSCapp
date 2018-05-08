package com.example.chen.wsscapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chen.wsscapp.Bean.TiXian;
import com.example.chen.wsscapp.R;

import java.util.List;

/**
 * Created by chen on 2018/5/6.
 */

public class TiXianAdapter extends RecyclerView.Adapter<TiXianAdapter.ViewHolder> {

    private Context context;
    private List<TiXian> list;


    public TiXianAdapter(){
        super();
    }

    public TiXianAdapter(List<TiXian> list, Context context){
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tixian,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String statuestr = null;
        holder.tv_txphone.setText("手 机 号："+list.get(position).getRef_user());
        if("0".equals(list.get(position).getRef_status())){
            statuestr = "待完成";
        }else if("1".equals(list.get(position).getRef_status())){
            statuestr = "已完成";
        }
        holder.tv_statue.setText(statuestr);
        holder.tv_txzfb.setText("支付宝账号："+list.get(position).getRef_zfbid());
        holder.tv_txmoney.setText("提现金额： "+list.get(position).getRef_money());
        holder.tv_txtime.setText("提交时间： "+list.get(position).getRef_time());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_txphone,tv_txzfb,tv_txmoney,tv_txtime,tv_statue;

       public ViewHolder(View itemView) {
           super(itemView);
           tv_txphone = (TextView) itemView.findViewById(R.id.tv_txphone);
           tv_txzfb = (TextView) itemView.findViewById(R.id.tv_txzfb);
           tv_txmoney = (TextView) itemView.findViewById(R.id.tv_txmoney);
           tv_txtime = (TextView) itemView.findViewById(R.id.tv_txtime);
           tv_statue = (TextView) itemView.findViewById(R.id.tv_statue);

       }
   }
}
