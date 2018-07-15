package com.example.chen.wsscapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.CommonAction;
import com.example.chen.wsscapp.Util.CustomViewPager;
import com.example.chen.wsscapp.adapter.MyFragmentPagerAdapter;
import com.example.chen.wsscapp.fragment.ChatFragment;
import com.example.chen.wsscapp.fragment.MeFragment;
import com.example.chen.wsscapp.fragment.MenuFragment;
import com.example.chen.wsscapp.fragment.ShoppingCarFragment;
import com.example.chen.wsscapp.fragment.WeiTaoFragment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
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
    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonAction.getInstance().addActivity(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //判断是否为小米或魅族手机，如果是则将状态栏文字改为黑色
            if (MIUISetStatusBarLightMode(this, true) || FlymeSetStatusBarLightMode(this, true)) {
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

    static boolean MIUISetStatusBarLightMode(Activity activity, boolean darkmode) {
        boolean result = false;
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    static boolean FlymeSetStatusBarLightMode(Activity activity, boolean darkmode) {
        boolean result = false;
        try {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class
                    .getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (darkmode) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            activity.getWindow().setAttributes(lp);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            JMessageClient.logout();
            CommonAction.getInstance().OutSign();
            System.exit(0);
        }
    }





}
