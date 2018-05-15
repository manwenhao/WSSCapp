package com.example.chen.wsscapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.wsscapp.Bean.TiXian;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.MyApplication;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

/**
 * Created by chen on 2018/5/14.
 */

public class TixianManagerAdapter  extends RecyclerView.Adapter<TixianManagerAdapter.ViewHolder>{

    private Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Toast.makeText(context,"确认失败",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

        }
    };



    private Activity context;
    private List<TiXian> list;

    public TixianManagerAdapter(){
        super();
    }

    public  TixianManagerAdapter(Activity context, List<TiXian> list){
        this.context = context;
        this.list = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_tixianmanager,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TiXian item = list.get(position);
        String statuestr = null;
        holder.tv_txphone.setText("手 机 号："+item.getRef_user());
        if("0".equals(item.getRef_status())){
            holder.tv_statue.setVisibility(View.INVISIBLE);
            holder.bt_tixian.setVisibility(View.VISIBLE);
            holder.bt_tixian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            OkHttpUtils.get()
                                    .addParams("ref_id",item.getRef_id())
                                    .url("http://106.14.145.208/ShopMall/UpdateReflectStatus")
                                    .build()
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onError(Request request, Exception e) {
                                            Message msg = new Message();
                                            msg.what = 1;
                                            mhandler.sendMessage(msg);
                                        }

                                        @Override
                                        public void onResponse(String response) {
                                            if("ok".equals(response.toString())){
                                                context.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        holder.bt_tixian.setVisibility(View.INVISIBLE);
                                                        holder.tv_statue.setVisibility(View.INVISIBLE);
                                                        notifyDataSetChanged();
                                                    }
                                                });

                                            }else{
                                                Message msg = new Message();
                                                msg.what = 1;
                                                mhandler.sendMessage(msg);
                                            }

                                        }
                                    });

                        }
                    }).start();

                }
            });
        }else if("1".equals(item.getRef_status())){
            statuestr = "已完成";
            holder.tv_statue.setVisibility(View.VISIBLE);
            holder.bt_tixian.setVisibility(View.INVISIBLE);
        }
        holder.tv_statue.setText(statuestr);
        holder.tv_txzfb.setText("支付宝账号："+item.getRef_zfbid());
        holder.tv_txmoney.setText("提现金额： "+item.getRef_money());
        holder.tv_txtime.setText("提交时间： "+item.getRef_time());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_txphone;
        private TextView tv_txzfb;
        private TextView tv_txmoney;
        private TextView tv_txtime;
        private TextView tv_statue;
        private Button bt_tixian;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_txphone = (TextView) itemView.findViewById(R.id.tv_txphone);
            tv_txzfb = (TextView) itemView.findViewById(R.id.tv_txzfb);
            tv_txmoney = (TextView) itemView.findViewById(R.id.tv_txmoney);
            tv_txtime = (TextView) itemView.findViewById(R.id.tv_txtime);
            tv_statue = (TextView) itemView.findViewById(R.id.tv_statue);
            bt_tixian = (Button) itemView.findViewById(R.id.bt_tixian);
        }
    }


}
