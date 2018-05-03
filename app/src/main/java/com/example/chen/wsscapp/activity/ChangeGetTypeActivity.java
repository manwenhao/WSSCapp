package com.example.chen.wsscapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.BaseActivity;
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
