package com.example.chen.wsscapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chen.wsscapp.Bean.ShopOrder;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.ChooseSendDialog;
import com.example.chen.wsscapp.Util.GetTel;
import com.example.chen.wsscapp.activity.ShowKuaiDiActivity;
import com.example.chen.wsscapp.activity.ShowSeeaddrActivity;

import java.util.List;

/**
 * Created by chen on 2018/5/2.
 */

public class ManagerOrderAdapter extends RecyclerView.Adapter {
    private static final int TOP = 1;
    private static final int ITEM = 2;
    private static final int BOTTOM = 3;
    private static final int BOTTOM2 = 4;
    private static final int BOTTOM3 = 5;
    private List<ShopOrder> list;
    private Context context;
    LayoutInflater mlayt;

    public ManagerOrderAdapter(List<ShopOrder> list, Context context) {
        this.list = list;
        this.context = context;
        mlayt = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TOP){
            return new TopViewHolder(mlayt.inflate(R.layout.shopordertop,parent,false));
        }else if(viewType == ITEM){
            return new ItemHolder(mlayt.inflate(R.layout.rvitem,parent,false));
        }else if(viewType == BOTTOM){
            return new BottomHolder(mlayt.inflate(R.layout.shoporderbottom,parent,false));
        }else if(viewType == BOTTOM2){
            return new BottomHolder2(mlayt.inflate(R.layout.shoporderbottom2,parent,false));
        }else if(viewType == BOTTOM3){
            return new BottomHolder3(mlayt.inflate(R.layout.shopoderbottom3,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof TopViewHolder){
            Glide.with(context).load("http://106.14.145.208"+list.get(position).getUser_touxiang()).placeholder(R.drawable.defalutuser).into(((TopViewHolder) holder).item_touxiang);
            ((TopViewHolder) holder).item_name.setText(list.get(position).getUser_name());
            ((TopViewHolder) holder).tv_ordid.setText(list.get(position).getOrd_id());
            ((TopViewHolder) holder).tv_ordphone.setText(list.get(position).getUser_phone());
        }else if(holder instanceof ItemHolder){
            ((ItemHolder) holder).tv_proname.setText(list.get(position).getOrd_name());
            ((ItemHolder) holder).tv_price.setText(list.get(position).getPro_price());
            ((ItemHolder) holder).tv_buy_num.setText("X"+list.get(position).getOrd_num());
            Glide.with(context).load("http://106.14.145.208"+list.get(position).getOrd_photo()).placeholder(R.drawable.shop).into(((ItemHolder) holder).iv_photo);
            ((ItemHolder) holder).tv_discount_price.setText(GetTel.getFloat(Float.parseFloat(list.get(position).getPro_price())*Float.parseFloat(list.get(position).getPro_discount()))+"");
            ((ItemHolder) holder).tv_color_size.setText("规格："+list.get(position).getOrd_color()+" , "+list.get(position).getOrd_size());
        }else if(holder instanceof  BottomHolder){
            ((BottomHolder) holder).bt_seeaddr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShowSeeaddrActivity.class);
                    intent.putExtra("statue",list.get(position).getOrd_status());
                    intent.putExtra("addr",list.get(position).getRev_address());
                    intent.putExtra("phone",list.get(position).getRev_phone());
                    intent.putExtra("name",list.get(position).getRev_name());
                    context.startActivity(intent);

                }
            });
            ((BottomHolder) holder).bt_sendshop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   ChooseSendDialog dialog = new ChooseSendDialog(context,list.get(position).getUser_phone(),list.get(position).getOrd_id());
                    dialog.show();
                }
            });
            ((BottomHolder) holder).tv_summoney.setText("总计:￥"+list.get(position).getOrd_money());
        }else if(holder instanceof BottomHolder2){
            ((BottomHolder2) holder).bt_seewl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String com = list.get(position).getOrd_expressname();
                    String nu = list.get(position).getOrd_expressid();
                    if(!("".equals(com)||com==null) && !("".equals(nu)||nu==null)){
                        Intent intent = new Intent(context,ShowKuaiDiActivity.class);
                        intent.putExtra("com",com);
                        intent.putExtra("nu",nu);
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(v.getContext(),"物流信息为空",Toast.LENGTH_SHORT).show();
                    }

                }
            });

            ((BottomHolder2) holder).bt_seeaddr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShowSeeaddrActivity.class);
                    intent.putExtra("statue",list.get(position).getOrd_status());
                    intent.putExtra("addr",list.get(position).getRev_address());
                    intent.putExtra("phone",list.get(position).getRev_phone());
                    intent.putExtra("name",list.get(position).getRev_name());
                    context.startActivity(intent);
                }
            });

            ((BottomHolder2) holder).tv_summoney.setText("总计:￥"+list.get(position).getOrd_money());
        }else if(holder instanceof BottomHolder3){
            ((BottomHolder3) holder).bt_seewl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String com = list.get(position).getOrd_expressname();
                    String nu = list.get(position).getOrd_expressid();
                    if(!("".equals(com)||com==null) && !("".equals(nu)||nu==null)){
                        Intent intent = new Intent(context,ShowKuaiDiActivity.class);
                        intent.putExtra("com",com);
                        intent.putExtra("nu",nu);
                        context.startActivity(intent);
                    }else{
                        Toast.makeText(v.getContext(),"物流信息为空",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            ((BottomHolder3) holder).bt_seeaddr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShowSeeaddrActivity.class);
                    intent.putExtra("statue",list.get(position).getOrd_status());
                    intent.putExtra("addr",list.get(position).getRev_address());
                    intent.putExtra("phone",list.get(position).getRev_phone());
                    intent.putExtra("name",list.get(position).getRev_name());
                    context.startActivity(intent);
                }
            });

            ((BottomHolder3) holder).tv_summoney.setText("总计:￥"+list.get(position).getOrd_money());

        }


    }

    @Override
    public int getItemViewType(int position) {
        switch (list.get(position).getType()){
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
    public int getItemCount() {
        return list.size();
    }


    private class TopViewHolder extends RecyclerView.ViewHolder {
        private ImageView item_touxiang;
        private TextView item_name;
        private TextView tv_ordid;
        private TextView tv_ordphone;
        public TopViewHolder(View view)
        {
            super(view);
            item_touxiang = (ImageView) view.findViewById(R.id.item_touxiang);
            item_name = (TextView) view.findViewById(R.id.item_name);
            tv_ordid = (TextView) view.findViewById(R.id.tv_ordid);
            tv_ordphone = (TextView) view.findViewById(R.id.tv_ordphone);
        }
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        private ImageView iv_photo;
        private TextView tv_proname;
        private TextView tv_color_size;
        private TextView tv_price;
        private TextView tv_discount_price;
        private TextView tv_buy_num;
        public ItemHolder(View view) {
            super(view);
            iv_photo = (ImageView) view.findViewById(R.id.iv_photo);
            tv_proname = (TextView) view.findViewById(R.id.tv_proname);
            tv_color_size = (TextView) view.findViewById(R.id.tv_color_size);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
            tv_discount_price = (TextView) view.findViewById(R.id.tv_discount_price);
            tv_buy_num = (TextView) view.findViewById(R.id.tv_buy_num);
        }
    }

    private class BottomHolder extends RecyclerView.ViewHolder {
        private Button bt_sendshop;
        private Button bt_seeaddr;
        private TextView tv_summoney;
        public BottomHolder(View view) {
            super(view);
            bt_sendshop = (Button) view.findViewById(R.id.bt_sendshop);
            bt_seeaddr = (Button) view.findViewById(R.id.bt_seeaddr);
            tv_summoney =  (TextView) view.findViewById(R.id.tv_summoney);
        }
    }

    private class BottomHolder2 extends RecyclerView.ViewHolder {
        private Button bt_seewl;
        private Button bt_seeaddr;
        private TextView tv_summoney;
        public BottomHolder2(View view) {
            super(view);
            bt_seewl = (Button) view.findViewById(R.id.bt_seewl);
            bt_seeaddr = (Button) view.findViewById(R.id.bt_seeaddr);
            tv_summoney =  (TextView) view.findViewById(R.id.tv_summoney);
        }
    }

    private class BottomHolder3 extends RecyclerView.ViewHolder {
        private Button bt_seewl;
        private Button bt_seeaddr;
        private TextView tv_summoney;
        public BottomHolder3(View view) {
            super(view);
            bt_seewl = (Button) view.findViewById(R.id.bt_seewl);
            bt_seeaddr = (Button) view.findViewById(R.id.bt_seeaddr);
            tv_summoney =  (TextView) view.findViewById(R.id.tv_summoney);
        }
    }


}
