package com.example.chen.wsscapp.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.BaseActivity;
import com.example.chen.wsscapp.Util.TopUi;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;

/**
 * Created by chen on 2018/4/29.
 * 修改商品信息是获取商品分类
 */

public class ChangeGetTypeActivity extends BaseActivity {
    private static final String TAG = "ChangeGetTypeActivity";
    private ListView ll_changetype;
    private ArrayAdapter typeadapter;
    private ArrayList<String> typedata;

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
        setContentView(R.layout.activity_changegettype);
        ll_changetype =  (ListView) findViewById(R.id.ll_changetype);
        getTypeJson();
        ll_changetype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemkey = typedata.get(position);
                Intent intent = new Intent(ChangeGetTypeActivity.this,ChangeShopListActivity.class);
                intent.putExtra("itemkey",itemkey);
                startActivity(intent);
            }
        });

    }

    public void getTypeJson() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .url("http://106.14.145.208/ShopMall/BackAppClassify")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                uitoast("获取商品分类失败");
                            }

                            @Override
                            public void onResponse(final String response) {
                                if("error".equals(response.toString())){
                                    uitoast("获取商品分类失败");
                                }else{
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            typedata = new ArrayList<String>();
                                            String[] data = response.toString().split(",");
                                            for(int i =0;i<data.length;i++){
                                                typedata.add(data[i]);
                                                Log.e(TAG,data[i]);
                                            }
                                            typeadapter = new ArrayAdapter(getApplicationContext(), R.layout.simple_list_item_change,typedata);
                                            ll_changetype.setAdapter(typeadapter);
                                        }
                                    });
                                }

                            }
                        });

            }
        }).start();

    }
}
