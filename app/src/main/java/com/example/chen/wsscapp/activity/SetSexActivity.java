package com.example.chen.wsscapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

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
