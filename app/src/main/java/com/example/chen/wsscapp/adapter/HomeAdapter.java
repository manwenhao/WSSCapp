package com.example.chen.wsscapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.chen.wsscapp.Bean.HomeItem;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.viewHolder.AdHolder;

import java.util.List;


/**
 * Created by cfanr on 2015/12/4.
 */
public class HomeAdapter extends BaseAdapter {
    private Context context;
    private List<HomeItem> homeItemList;
    private final static int AD=0;
    private final static int MENU=1;


    public HomeAdapter(Context context, List<HomeItem> homeItemList){
        this.context=context;
        this.homeItemList=homeItemList;
    }

    @Override
    public int getCount(){
        return homeItemList.size();
    }

    @Override
    public Object getItem(int position){
        return homeItemList== null ? null : homeItemList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup){
        HomeItem homeItem=homeItemList.get(position);
        LayoutInflater inflater= LayoutInflater.from(context);
       // MenuHolder menuHolder;
        AdHolder adHolder;
        int type=homeItem.getItemType().getValue();
        switch(type){
            case AD:
                if(convertView==null){
                    convertView=inflater.inflate(R.layout.view_home_ad,null);
                    adHolder=new AdHolder(context,convertView);
                    convertView.setTag(adHolder);
                }else{
                    adHolder=(AdHolder)convertView.getTag();
                }
                adHolder.setViewPager(homeItem);
                break;
          /*  case MENU:
                if(convertView==null){
                    convertView=inflater.inflate(R.layout.view_home_menu,null);
                    menuHolder=new MenuHolder(convertView);
                    convertView.setTag(menuHolder);
                }else{
                    menuHolder=(MenuHolder)convertView.getTag();
                }
                menuHolder.refreshUI(homeItem);
                break;*/
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position){
        if (homeItemList!= null && position < homeItemList.size()) {
            return homeItemList.get(position).getItemType().getValue();
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount(){
        return 2;
    }
}
