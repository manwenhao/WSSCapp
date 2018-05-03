package com.example.chen.wsscapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.adapter.OrderFragmentPagerAdapter;
import com.example.chen.wsscapp.fragment.CompleteManagerShopFragment;
import com.example.chen.wsscapp.fragment.WaitManagerGetShopFragment;
import com.example.chen.wsscapp.fragment.WaitManagerSendShopFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 2018/5/1.
 */

public class ShopManagerOrderActivity extends FragmentActivity {
    private TabLayout tb_tab;
    private ViewPager vp_content;
    private OrderFragmentPagerAdapter orderAdapter;
    private List<Fragment> mlist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopmanageroder);
        initView();
    }

    private void initView() {
        tb_tab = (TabLayout) findViewById(R.id.tb_tab);
        vp_content = (ViewPager) findViewById(R.id.vp_content);
        WaitManagerSendShopFragment waitsendFragment = new WaitManagerSendShopFragment();
        WaitManagerGetShopFragment waitgetFragment = new WaitManagerGetShopFragment();
        CompleteManagerShopFragment completeFragemnt = new CompleteManagerShopFragment();
        mlist = new ArrayList<>();
        mlist.add(waitsendFragment);
        mlist.add(waitgetFragment);
        mlist.add(completeFragemnt);
        orderAdapter = new OrderFragmentPagerAdapter(getSupportFragmentManager(),mlist);
        vp_content.setAdapter(orderAdapter);
        tb_tab.setupWithViewPager(vp_content);
    }
}
