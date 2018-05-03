package com.example.chen.wsscapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.example.chen.wsscapp.Bean.Items;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.BaseActivity;
import com.example.chen.wsscapp.adapter.GridViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 2018/4/22.
 * 商家管理
 */

public class ShopManagerActivity extends BaseActivity {
    private GridView managergrid;
    private List<Items> item= new ArrayList<>();
    private BaseAdapter itemAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopmanager);
        initView();
    }

    private void initView() {
        managergrid = (GridView) findViewById(R.id.managergrid);
        initData();
        itemAdapter = new GridViewAdapter(this, R.layout.item,item);
        // 添加控件适配器
        managergrid.setAdapter(itemAdapter);

        managergrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent intent = new Intent(ShopManagerActivity.this,ShopManagerOrderActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(ShopManagerActivity.this,BecomeShopActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(ShopManagerActivity.this,ChangeGetTypeActivity.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(ShopManagerActivity.this,XcMenuActivity.class);
                        startActivity(intent3);
                        break;
                    case 4:
                        //showToast("店铺动态");
                        Intent intent4 = new Intent(ShopManagerActivity.this,ShopDynamicActivity.class);
                        startActivity(intent4);
                        break;
                    case 5:
                       // showToast("短信公告");
                        Intent intent5 = new Intent(ShopManagerActivity.this,SendMsgActivity.class);
                        startActivity(intent5);
                        break;
                }
            }
        });

    }

    private void initData() {
        Items item1 = new Items("订单管理", R.mipmap.becomeshop);
        item.add(item1);

        Items item2 = new Items("商品上架", R.mipmap.becomeshop);
        item.add(item2);

        Items item3 = new Items("商品管理", R.mipmap.becomeshop);
        item.add(item3);

        Items item4 = new Items("首页宣传", R.mipmap.becomeshop);
        item.add(item4);

        Items item5 = new Items("店铺动态", R.mipmap.becomeshop);
        item.add(item5);

        Items item6 = new Items("短信公告", R.mipmap.becomeshop);
        item.add(item6);
    }
}
