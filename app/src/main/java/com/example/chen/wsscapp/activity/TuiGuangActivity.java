package com.example.chen.wsscapp.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.*;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import  com.example.chen.wsscapp.Util.BaseActivity;

/**
 * Created by chen on 2018/5/15.
 */
public class TuiGuangActivity extends BaseActivity{
    private TextView tv_tuiguangcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //判断是否为小米或魅族手机，如果是则将状态栏文字改为
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
        setContentView(R.layout.activity_tuiguang);
        tv_tuiguangcode = (TextView) findViewById(R.id.tv_tuiguangcode);
        getCode();

    }

    public void getCode() {
       new Thread(new Runnable() {
           @Override
           public void run() {
               OkHttpUtils.get()
                       .addParams("phone_num", GetTel.gettel())
                       .url("http://106.14.145.208/ShopMall/BackAppTGCode")
                       .build()
                       .execute(new StringCallback() {
                           @Override
                           public void onError(Request request, Exception e) {
                               uitoast("获取失败");
                           }

                           @Override
                           public void onResponse(final String response) {
                               if("error".equals(response)){
                                   uitoast("获取失败");
                               }else{
                                   runOnUiThread(new Runnable() {
                                       @Override
                                       public void run() {
                                           tv_tuiguangcode.setText(response.toString());
                                       }
                                   });
                               }

                           }
                       });
           }
       }).start();
    }
}
