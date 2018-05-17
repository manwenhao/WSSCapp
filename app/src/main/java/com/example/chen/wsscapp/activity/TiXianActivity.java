package com.example.chen.wsscapp.activity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.example.chen.wsscapp.Bean.TiXian;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.BaseActivity;
import com.example.chen.wsscapp.Util.SimpleDividerItemDecoration;
import com.example.chen.wsscapp.Util.TopUi;
import com.example.chen.wsscapp.adapter.TiXianAdapter;
import com.example.chen.wsscapp.adapter.TixianManagerAdapter;
import com.github.nuptboyzhb.lib.SuperSwipeRefreshLayout;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 2018/5/14.
 */
public class TiXianActivity extends BaseActivity{

    private RecyclerView rv_tixianmanager;
    private List<TiXian> list = new ArrayList<>();
    private TixianManagerAdapter adapter;
    private SuperSwipeRefreshLayout ssr_tixianlist;
    private Boolean isrefresh = false;

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
        setContentView(R.layout.activity_tixianmanager);
        rv_tixianmanager = (RecyclerView) findViewById(R.id.rv_tixianmanager);
        rv_tixianmanager.setLayoutManager(new LinearLayoutManager(this));
        ssr_tixianlist = (SuperSwipeRefreshLayout) findViewById(R.id.ssr_tixianlist);
        ssr_tixianlist.setHeaderViewBackgroundColor(0xffcccccc);
        ssr_tixianlist.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                isrefresh = true;
                getTiXianData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ssr_tixianlist.setRefreshing(false);
                    }
                }, 1000);
            }

            @Override
            public void onPullDistance(int i) {

            }

            @Override
            public void onPullEnable(boolean b) {

            }
        });
        getTiXianData();

    }

    public void getTiXianData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .url("http://106.14.145.208/ShopMall/BackUserReflect")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                uitoast("获取订单失败");
                            }

                            @Override
                            public void onResponse(String response) {
                                if("error".equals(response)){
                                    uitoast("获取订单失败");
                                }else{
                                    final List<TiXian> tiXien = JSONArray.parseArray(response.toString(), TiXian.class);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter = new TixianManagerAdapter(TiXianActivity.this,tiXien);
                                            rv_tixianmanager.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                            if(isrefresh==false) {
                                                Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.line_divider);
                                                rv_tixianmanager.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext(), drawable, 2));
                                            }
                                            rv_tixianmanager.setAdapter(adapter);

                                        }
                                    });


                                }

                            }
                        });

            }
        }).start();
    }
}
