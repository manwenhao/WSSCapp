package com.example.chen.wsscapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.TopUi;

/**
 * Created by chen on 2018/5/3.
 */

public class ShowSeeaddrActivity extends Activity {
    private TextView tv_statue,tv_user,tv_phone,tv_addr;

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
        setContentView(R.layout.activity_showseeaddr);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        String statue;
        String sta = intent.getStringExtra("statue");
        if(sta.equals("0")){
            statue = "商家派送";
        }else if(sta.equals("8")){
            statue = "店铺自取";
        }else{
            statue = "未知提货方式";
        }
        String name = intent.getStringExtra("name");
        String phone = intent.getStringExtra("phone");
        String addr = intent.getStringExtra("addr");
        tv_statue = (TextView) findViewById(R.id.tv_statue);
        tv_statue.setText("收货方式：  "+statue);
        tv_user = (TextView) findViewById(R.id.tv_user);
        tv_user.setText("收货人：  "+name);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_phone.setText("电话：  "+phone);
        tv_addr = (TextView) findViewById(R.id.tv_addr);
        tv_addr.setText("收货地址：  "+addr);
    }
}
