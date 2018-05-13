package com.example.chen.wsscapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chen.wsscapp.Bean.WaitOrder;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.GetTel;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

/**
 * Created by chen on 2018/4/30.
 */

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TOP = 1;
    private static final int ITEM = 2;
    private static final int BOTTOM = 3;
    private static final int BOTTOM2 = 4;
    private static final int BOTTOM3 = 5;
    private static final String TAG = "OrderAdapter";
    List<WaitOrder> mlist;
    Context context;
    LayoutInflater mlayt;

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

    public OrderAdapter(){

    }


    public OrderAdapter(Context context,List<WaitOrder> list){
        this.context = context;
        this.mlist = list;
        this.mlayt = LayoutInflater.from(context);
    }

    public void setData(List<WaitOrder> list){
        this.mlist = list;
    }

    @Override
    public int getItemViewType(int position) {
        switch (mlist.get(position).getType()){
            case "1":
                return TOP;

            case "2":
                return ITEM;

            case "3":
                return BOTTOM;

            case "4":
                return BOTTOM2;

            case "5":
                return BOTTOM3;

            default:
                return 0;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TOP){
            return new TopViewHolder(mlayt.inflate(R.layout.rvtop,parent,false));
        }else if(viewType==ITEM){
            return new ItemViewHolder(mlayt.inflate(R.layout.rvitem,parent,false));
        }else if(viewType==BOTTOM){
            return  new BottomViewHolder(mlayt.inflate(R.layout.rvbottom,parent,false));
        }else if(viewType==BOTTOM2){
            return  new BottomViewHolder2(mlayt.inflate(R.layout.rvbottom2,parent,false));
        }
        else if(viewType==BOTTOM3){
            return  new BottomViewHolder3(mlayt.inflate(R.layout.rvbottom3,parent,false));
        }else{
        return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof TopViewHolder){
            ((TopViewHolder) holder).tv_orderid.setText(mlist.get(position).getOrd_time());
        }else if(holder instanceof  ItemViewHolder){
           ((ItemViewHolder) holder).tv_proname.setText(mlist.get(position).getOrd_name());
            ((ItemViewHolder) holder).tv_price.setText(mlist.get(position).getPro_price());
            ((ItemViewHolder) holder).tv_buy_num.setText("X"+mlist.get(position).getOrd_num());
            Glide.with(context).load("http://106.14.145.208"+mlist.get(position).getOrd_photo()).placeholder(R.drawable.shop).into(((ItemViewHolder) holder).iv_photo);
            ((ItemViewHolder) holder).tv_discount_price.setText(GetTel.getFloat(Float.parseFloat(mlist.get(position).getPro_price())*Float.parseFloat(mlist.get(position).getPro_discount()))+"");
            ((ItemViewHolder) holder).tv_color_size.setText(mlist.get(position).getOrd_color()+","+mlist.get(position).getOrd_size());
        }else if(holder instanceof  BottomViewHolder){
            ((BottomViewHolder) holder).tv_summoney.setText("总计:￥"+mlist.get(position).getOrd_money());
        }else if(holder instanceof  BottomViewHolder2){
            ((BottomViewHolder2) holder).bt_seewl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),"查看物流",Toast.LENGTH_SHORT).show();
                }
            });
            ((BottomViewHolder2) holder).bt_suregetshop.setOnClickListener(new View.OnClickListener() {
                String ord_id = mlist.get(position).getOrd_id();
                @Override
                public void onClick(final View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            OkHttpUtils.get()
                                    .addParams("ord_id",ord_id)
                                    .addParams("ord_phone",GetTel.gettel())
                                    .url("http://106.14.145.208/ShopMall/OrderWC")
                                    .build()
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onError(Request request, Exception e) {

                                        }

                                        @Override
                                        public void onResponse(String response) {
                                            if("ok".equals(response)){
                                                for(int i = mlist.size()-1;i>=0;i--){
                                                    if(mlist.get(i).getOrd_id().equals(ord_id)){
                                                        mlist.remove(i);
                                                        notifyDataSetChanged();
                                                    }
                                                }
                                                Log.e(TAG,"确认收货");

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
            ((BottomViewHolder2) holder).tv_summoney.setText("总计:￥"+mlist.get(position).getOrd_money());
        }else if(holder instanceof  BottomViewHolder3){
            ((BottomViewHolder3) holder).bt_seewl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),"查看物流",Toast.LENGTH_SHORT).show();
                }
            });
            ((BottomViewHolder3) holder).tv_summoney.setText("总计:￥"+mlist.get(position).getOrd_money());
        }

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    private class TopViewHolder extends RecyclerView.ViewHolder {
        TextView tv_orderid;
        public TopViewHolder(View view) {
            super(view);
            tv_orderid = (TextView) view.findViewById(R.id.tv_orderid);
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_photo;
        private TextView tv_proname;
        private TextView tv_color_size;
        private TextView tv_price;
        private TextView tv_discount_price;
        private TextView tv_buy_num;
        public ItemViewHolder(View view) {
            super(view);
            iv_photo = (ImageView) view.findViewById(R.id.iv_photo);
            tv_proname = (TextView) view.findViewById(R.id.tv_proname);
            tv_color_size = (TextView) view.findViewById(R.id.tv_color_size);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
            tv_discount_price = (TextView) view.findViewById(R.id.tv_discount_price);
            tv_buy_num = (TextView) view.findViewById(R.id.tv_buy_num);
        }
    }

    private class BottomViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_summoney;
        public BottomViewHolder(View view) {
            super(view);
            tv_summoney = (TextView) view.findViewById(R.id.tv_summoney);

        }
    }

    private class BottomViewHolder2 extends RecyclerView.ViewHolder {
        private Button bt_seewl;
        private Button bt_suregetshop;
        private TextView tv_summoney;
        public BottomViewHolder2(View view) {
            super(view);
            bt_seewl = (Button) view.findViewById(R.id.bt_seewl);
            tv_summoney = (TextView) view.findViewById(R.id.tv_summoney);
            bt_suregetshop = (Button) view.findViewById(R.id.bt_suregetshop);
        }
    }

    private class BottomViewHolder3 extends RecyclerView.ViewHolder {
        private Button bt_seewl;
        private TextView tv_summoney;
        public BottomViewHolder3(View view) {
            super(view);
            bt_seewl = (Button) view.findViewById(R.id.bt_seewl);
            tv_summoney = (TextView) view.findViewById(R.id.tv_summoney);
        }
    }
}
