package com.example.chen.wsscapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.chen.wsscapp.Bean.Addr;
import com.example.chen.wsscapp.R;

import java.util.List;

/**
 * Created by chen on 2018/5/8.
 */

public class AddrAdapter extends BaseAdapter {
    private Context context;
    private List<Addr> list;

    public AddrAdapter(){
        super();
    }

    public AddrAdapter(Context context,List<Addr> list){
        this.context = context;
        this.list = list;
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
        Addr addr = list.get(position);
        ViewHoder vh;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_addr,parent,false);
            vh = new ViewHoder();
            vh.tv_user = (TextView) convertView.findViewById(R.id.tv_user);
            vh.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
            vh.tv_addr = (TextView) convertView.findViewById(R.id.tv_addr);
            convertView.setTag(vh);
        }else{
           vh = (ViewHoder) convertView.getTag();
        }
        vh.tv_addr.setText(addr.getRev_address());
        vh.tv_phone.setText(addr.getRev_phone());
        vh.tv_user.setText(addr.getRev_name());
        return convertView;
    }


    class ViewHoder{
        private TextView tv_user;
        private TextView tv_phone;
        private TextView tv_addr;
    }
}
