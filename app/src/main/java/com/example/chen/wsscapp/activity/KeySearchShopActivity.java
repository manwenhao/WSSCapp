package com.example.chen.wsscapp.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.chen.wsscapp.Bean.GetProduct;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.BaseActivity;
import com.example.chen.wsscapp.Util.TopUi;
import com.example.chen.wsscapp.adapter.SearchViewAdapter;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 2018/4/20.
 * 根据关键字进行搜索
 */

public class KeySearchShopActivity extends BaseActivity {
    private static final String TAG = "KeySearchShopActivity";
    private EditText et_search;
   // private TextView tv_test;
    private RecyclerView rv_search;
    private List<GetProduct> list = new ArrayList<>();
    private SearchViewAdapter adapter;
    private String keySearch,key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //判断是否为小米或魅族手机，如果是则将状态栏文字改为黑色
            if (TopUi.MIUISetStatusBarLightMode(this, true) || TopUi.FlymeSetStatusBarLightMode(this, true)) {
                //设置状态栏为指定颜色
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0
                    this.getWindow().setStatusBarColor(getResources().getColor(R.color.topbackgroud));
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4
                    //调用修改状态栏颜色的方法
                    this.getWindow().setStatusBarColor(getResources().getColor(R.color.topbackgroud));
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //如果是6.0以上将状态栏文字改为黑色，并设置状态栏颜色
                this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                this.getWindow().setStatusBarColor(getResources().getColor(R.color.topbackgroud));

            }
        }
        setContentView(R.layout.activity_keysearchshop);
        initView();
    }

    private void initView() {
        //tv_test = (TextView) findViewById(R.id.tv_test);
        et_search = (EditText) findViewById(R.id.et_searchshop);
        rv_search = (RecyclerView) findViewById(R.id.rv_search);

        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        if(!TextUtils.isEmpty(key)){
            et_search.setText(key);
            getKeyJsonData(key);
        }
        rv_search.setLayoutManager(new LinearLayoutManager(this));
        //item 下划线
        rv_search.addItemDecoration(new DividerItemDecoration(getApplication(), DividerItemDecoration.VERTICAL));
       // et_search.setOnClickListener(this);
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                et_search.setCursorVisible(true);
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    keySearch = et_search.getText().toString();
                    if(TextUtils.isEmpty(keySearch)){
                        showToast("请输入商品名称");
                        return false;
                    }else {
                        //showToast(keySearch);
                        getKeyJsonData(keySearch);

                    }
                }
                return false;
            }
        });
    }


    //获取关键字搜索json数据
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
                                toast("搜索失败，检查网络");
                            }

                            @Override
                            public void onResponse(String response) {
                                if("error".equals(response.toString())){
                                   toast(response.toString());
                                }else{
                                    initListView(response.toString());
                                }

                            }
                        });


            }
        }).start();
    }


    //初始化view的值与刷新
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
                adapter = new SearchViewAdapter(getApplication(),list);
                rv_search.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }

    //toast的小方法
    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(KeySearchShopActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
