package com.example.chen.wsscapp.activity;


import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.TopUi;
import com.example.chen.wsscapp.adapter.OrderFragmentPagerAdapter;
import com.example.chen.wsscapp.fragment.CompleteShopFragment;
import com.example.chen.wsscapp.fragment.WaitGetShopFragment;
import com.example.chen.wsscapp.fragment.WaitSendShopFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 2018/4/24.
 */

public class OrderActivity extends FragmentActivity {

    private TabLayout tb_tab;
    private ViewPager vp_content;
    private OrderFragmentPagerAdapter orderAdapter;
    private List<Fragment> mlist;


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

        setContentView(R.layout.activity_order);
        initView();

    }

    private void initView() {
        tb_tab = (TabLayout) findViewById(R.id.tb_tab);
        vp_content = (ViewPager) findViewById(R.id.vp_content);
        WaitSendShopFragment waitsendFragment = new WaitSendShopFragment();
        WaitGetShopFragment waitgetFragment = new WaitGetShopFragment();
        CompleteShopFragment completeFragemnt = new CompleteShopFragment();
        mlist = new ArrayList<>();
        mlist.add(waitsendFragment);
        mlist.add(waitgetFragment);
        mlist.add(completeFragemnt);
        orderAdapter = new OrderFragmentPagerAdapter(getSupportFragmentManager(),mlist);
        vp_content.setAdapter(orderAdapter);
        tb_tab.setupWithViewPager(vp_content);
    }
}
