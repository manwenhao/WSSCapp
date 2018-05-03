package com.example.chen.wsscapp.activity;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.example.chen.wsscapp.R;
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
