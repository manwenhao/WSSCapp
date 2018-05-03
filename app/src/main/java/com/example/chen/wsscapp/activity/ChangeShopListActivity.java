package com.example.chen.wsscapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.chen.wsscapp.Bean.GetProduct;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.BaseActivity;
import com.example.chen.wsscapp.Util.MyApplication;
import com.example.chen.wsscapp.adapter.ChangeSearchAdapter;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by chen on 2018/4/29.
 * 进入商品修改列表
 */

public class ChangeShopListActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "ChangeShopListActivity";
    private ChangeSearchAdapter adapter;
    private List<GetProduct> list = new ArrayList<>();
    private EditText et_search;
    private SwipeMenuListView swipeMenuListView;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeshoplist);
        initView();
    }

    private void initView() {
        et_search = (EditText) findViewById(R.id.et_searchshop);
        swipeMenuListView=(SwipeMenuListView) findViewById(R.id.exListView);
        Intent intent = getIntent();
        key = intent.getStringExtra("itemkey");
        if(!TextUtils.isEmpty(key)){
            et_search.setText(key);
            getKeyJsonData(key);
        }
        et_search.setOnClickListener(this);
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "edit" item
                SwipeMenuItem editItem = new SwipeMenuItem(
                        MyApplication.getContext());

                // set item background
                editItem.setBackground(new ColorDrawable(Color.rgb(255,
                        170, 37)));
                // set item width
                editItem.setWidth(dp2px(90));

                editItem.setIcon(R.drawable.edit);
                // add to menu
                menu.addMenuItem(editItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        MyApplication.getContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        swipeMenuListView.setMenuCreator(creator);
    }

    private void getKeyJsonData(final String keySearch) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .addParams("pro_key",keySearch)
                        .url("http://106.14.145.208/ShopMall/BackAppQueryProduct")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                uitoast("搜索失败，检查网络");
                            }

                            @Override
                            public void onResponse(String response) {
                                if("error".equals(response.toString())){
                                    uitoast(response.toString());
                                }else{
                                    initListView(response.toString());
                                }

                            }
                        });


            }
        }).start();
    }

    private void initListView(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<GetProduct> data  = JSON.parseArray(s, GetProduct.class);
                if(data.size()>=0) {
                    list.clear();
                    for (GetProduct getProduct : data) {
                        GetProduct info = new GetProduct();
                        info.setPro_brand(getProduct.getPro_brand());
                        info.setPro_id(getProduct.getPro_id());
                        info.setPro_name(getProduct.getPro_name());
                        info.setPro_price(getProduct.getPro_price());
                        info.setPro_discount(getProduct.getPro_discount());
                        info.setPro_photo(getProduct.getPro_photo());
                        list.add(info);
                    }
                }
                adapter = new ChangeSearchAdapter(list,getApplication());
                swipeMenuListView.setAdapter(adapter);
                swipeMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        GetProduct getProduct = list.get(position);
                        Log.e(TAG,"pos "+position);
                        Log.e(TAG,getProduct.getPro_id());
                        Intent intent = new Intent(ChangeShopListActivity.this,ShowshopInfoActivity.class);
                        intent.putExtra("shopid",getProduct.getPro_id());
                        startActivity(intent);
                    }
                });
                swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                        switch (index){
                            case 0:
                                Intent intent = new Intent(ChangeShopListActivity.this,ChangeInfoActivity.class);
                                intent.putExtra("shopid",list.get(position).getPro_id());
                                startActivity(intent);
                                break;
                            case 1:
                                AlertDialog alert = new AlertDialog.Builder(ChangeShopListActivity.this).create();
                                alert.setTitle("操作提示");
                                alert.setMessage("您确定要将这些商品移除吗？");
                                alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                return;
                                            }
                                        });
                                alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                ChildDelete(position);
                                            }
                                        });
                                alert.show();
                                break;
                        }
                        return false;
                    }
                });
            }
        });
    }

    //删除选中的商品
    private void ChildDelete(final int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .addParams("good_id",list.get(position).getPro_id())
                        .url("http://106.14.145.208/ShopMall/DeleteProductById")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                uitoast("删除失败");
                            }

                            @Override
                            public void onResponse(String response) {
                                if("ok".equals(response.toString())){
                                    uitoast("删除成功");
                                    list.remove(position);
                                    adapter.notifyDataSetChanged();
                                }else{
                                    uitoast("删除失败");
                                }

                            }
                        });

            }
        }).start();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }


    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.et_searchshop){
            final String str = et_search.getText().toString();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpUtils.get()
                            .addParams("pro_key",str)
                            .url("http://106.14.145.208/ShopMall/BackAppQueryProduct")
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Request request, Exception e) {
                                    uitoast("搜索失败，检查网络");
                                }

                                @Override
                                public void onResponse(String response) {
                                    if("error".equals(response.toString())){
                                        uitoast(response.toString());
                                    }else{
                                        initListView(response.toString());
                                    }

                                }
                            });


                }
            }).start();
        }

    }
}
