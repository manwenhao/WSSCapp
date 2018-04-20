package com.example.chen.wsscapp.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.CustomViewPager;
import com.example.chen.wsscapp.adapter.MyFragmentPagerAdapter;
import com.example.chen.wsscapp.fragment.ChatFragment;
import com.example.chen.wsscapp.fragment.MeFragment;
import com.example.chen.wsscapp.fragment.MenuFragment;
import com.example.chen.wsscapp.fragment.ShoppingCarFragment;
import com.example.chen.wsscapp.fragment.WeiTaoFragment;

import java.util.ArrayList;
import java.util.List;

import qiu.niorgai.StatusBarCompat;


public class MainActivity extends FragmentActivity {
    private String TAG = "mainActivity";
    private CustomViewPager viewPager;
    private RadioGroup radioGroup;
    private RadioButton rbChat,rbWeitao, rbContacts, rbDiscovery, rbMe;
    MenuFragment weMenuFragment = new MenuFragment();
    ChatFragment chatFragment = new ChatFragment();
    WeiTaoFragment weiTaoFragment = new WeiTaoFragment();
    ShoppingCarFragment shoppingCarFragment = new ShoppingCarFragment();
    MeFragment meFragment = new MeFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  StatusBarCompat.translucentStatusBar(this);//透明 浸入式标题栏
        setContentView(R.layout.activity_main);
        //传登录时用户数据
        Intent intent = getIntent();
        String jsondata = intent.getStringExtra("user_json");
        Log.e(TAG,jsondata);
        Bundle bundle = new Bundle();
        bundle.putString("data",jsondata);
        meFragment.setArguments(bundle);
        initView();

    }

    private void initView() {
        /**
         * RadioGroup部分
         */
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        rbChat = (RadioButton) findViewById(R.id.rb_chat);
        rbWeitao = (RadioButton) findViewById(R.id.rb_weitao);
        rbContacts = (RadioButton) findViewById(R.id.rb_contacts);
        rbDiscovery = (RadioButton) findViewById(R.id.rb_discovery);
        rbMe = (RadioButton) findViewById(R.id.rb_me);
        //RadioGroup选中状态改变监听
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_chat:
                        /**
                         * setCurrentItem第二个参数控制页面切换动画
                         * true:打开/false:关闭
                         */
                        viewPager.setCurrentItem(0, false);
                        break;
                    case R.id.rb_weitao:
                        viewPager.setCurrentItem(1,false);
                        break;
                    case R.id.rb_contacts:
                        viewPager.setCurrentItem(2, false);
                        break;
                    case R.id.rb_discovery:
                        viewPager.setCurrentItem(3, false);
                        break;
                    case R.id.rb_me:
                        viewPager.setCurrentItem(4, false);
                        break;
                }
            }
        });

        /**
         * ViewPager部分
         */
        viewPager = (CustomViewPager) findViewById(R.id.viewPager);
        viewPager.setScanScroll(false);
        final List<Fragment> alFragment = new ArrayList<Fragment>();
        alFragment.add(weMenuFragment);
        alFragment.add(weiTaoFragment);
        alFragment.add(chatFragment);
        alFragment.add(shoppingCarFragment);
        alFragment.add(meFragment);
        //ViewPager设置适配器
        final MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), alFragment);
        viewPager.setAdapter(adapter);
        //ViewPager显示第一个Fragment
        viewPager.setCurrentItem(0);
        //ViewPager页面切换监听
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        radioGroup.check(R.id.rb_chat);
                        break;
                    case 1:
                        radioGroup.check(R.id.rb_weitao);
                        break;
                    case 2:
                        radioGroup.check(R.id.rb_contacts);
                        break;
                    case 3:
                        radioGroup.check(R.id.rb_discovery);
                        break;
                    case 4:
                        radioGroup.check(R.id.rb_me);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
