package com.example.chen.wsscapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.ACache;
import com.example.chen.wsscapp.Util.BaseActivity;
import com.example.chen.wsscapp.Util.MyApplication;
import com.example.chen.wsscapp.Util.TopUi;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by chen on 2018/3/18.
 */

public class SetSexActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "SetSexActivity";
    private RadioGroup rg_sex;
    private RadioButton rb_man,rb_woman,radioButton;
    private Button bt_setting;
    String selectText;
    final ACache mAcache = ACache.get(MyApplication.getContext(),"userdata");
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
        setContentView(R.layout.activity_setsex);
        initView();

    }

    private void initView() {
        rg_sex = (RadioGroup) findViewById(R.id.rg_sex);
        rb_man = (RadioButton) findViewById(R.id.rb_man);
        rb_woman = (RadioButton) findViewById(R.id.rb_woman);
        bt_setting = (Button) findViewById(R.id.toolbar_right_btn);
        bt_setting.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){
            case R.id.toolbar_right_btn:
                if(rb_man.isChecked()){
                    selectText = "男";
                }else if(rb_woman.isChecked()){
                    selectText = "女";
                }
                Log.e(TAG,selectText);
                SharedPreferences pref = getSharedPreferences("user_data",MODE_PRIVATE);
                String idphone = pref.getString("phone","");
                setSex(idphone,selectText);
                break;

        }


    }


    /**
     * 修改设置性别
     *idphone 为手机号码
     *sex 为性别
     */
    private void setSex(final String idphone, final String selectText) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                OkHttpUtils.get()
                        .addParams("id",idphone)
                        .addParams("key","user_sex")
                        .addParams("value",selectText)
                        .url("http://106.14.145.208/ShopMall/UpdateForUserInfo")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                toast("更改性别失败");

                            }

                            @Override
                            public void onResponse(String response) {
                                if(response.equals("ok")){
                                    toast("更改性别成功");
                                    UserInfo userInfo= JMessageClient.getMyInfo();
                                   if (selectText.equals("男")){
                                       userInfo.setGender(UserInfo.Gender.male);
                                   }else {
                                       userInfo.setGender(UserInfo.Gender.female);
                                   }
                                   JMessageClient.updateMyInfo(UserInfo.Field.gender, userInfo, new BasicCallback() {
                                       @Override
                                       public void gotResult(int i, String s) {
                                           Log.d("修改性别",s);
                                       }
                                   });
                                    mAcache.put("user_sex",selectText);
                                    Intent intent = new Intent(SetSexActivity.this,SetUserInfoActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else if(response.toString().equals("error")){
                                    toast("更改昵称失败");
                                }

                            }
                        });
            }
        }).start();

    }


    //toast的小方法
    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SetSexActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
