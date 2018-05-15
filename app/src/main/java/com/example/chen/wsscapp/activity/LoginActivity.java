package com.example.chen.wsscapp.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chen.wsscapp.Bean.User;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.ACache;
import com.example.chen.wsscapp.Util.BaseActivity;
import com.example.chen.wsscapp.Util.CommonAction;
import com.example.chen.wsscapp.Util.MyApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;


/**
 * Created by chen on 2018/3/10.
 * 登陆
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    String TAG = "LoginActivity";
    private EditText et_account,et_password;
    private TextView tv_register,tv_rempwd;
    private Button bt_login;
    private SharedPreferences.Editor editor;




    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonAction.getInstance().addActivity(this);
        if(Build.VERSION.SDK_INT>=21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_login);
        initView();
        autoLogin();
    }


    //自动登陆
    private void autoLogin() {
        SharedPreferences pref = getSharedPreferences("user_data",MODE_PRIVATE);
        String account = pref.getString("phone","");
        String password = pref.getString("password","");
        if(!((account == null || "".equals(account))&&((password == null || "".equals(password))))){
            Log.e(TAG+"autoLogin",account);
            Log.e(TAG+"autoLogin",password);
            login(account,password);
        }

    }

    private void initView() {
        et_account = (EditText) findViewById(R.id.et_account);
        et_password = (EditText) findViewById(R.id.et_password);
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_register.setOnClickListener(this);
        tv_rempwd = (TextView) findViewById(R.id.tv_rempwd);
        tv_rempwd.setOnClickListener(this);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_login.setOnClickListener(this);
        SharedPreferences pref = getSharedPreferences("user_data",MODE_PRIVATE);
        String account = pref.getString("phone","");
        String password = pref.getString("password","");
        if(!((account == null || "".equals(account))&&((password == null || "".equals(password))))){
            et_account.setText(account);
            et_password.setText(password);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_register:
                Intent register = new Intent(this,RegisterActivity.class);
                startActivity(register);
                break;
            case R.id.bt_login:
                Log.e(TAG,"login____________+++++++++");
                String account  = et_account.getText().toString().trim();
                String password = et_password.getText().toString();
                if(TextUtils.isEmpty(account)){
                    showToast("请输入账号");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    showToast("请输入密码");
                    return;
                }
                    Log.e(TAG,"login____________");
                    login(account,password);
                break;
            case R.id.tv_rempwd:
                lossPwd();
                break;
        }



    }



    //忘记密码
    private void lossPwd() {
        showToast("尚未开发");
    }


    //登陆判断
    private void login(final String account, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .addParams("id",account)
                        .addParams("password",password)
                        .url("http://106.14.145.208/ShopMall/JudgeAppLogin")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(final Request request, Exception e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showToast("登陆超时,请检查设备网络连接");
                                    }
                                });
                            }

                            @Override
                            public void onResponse(String response) {
                                String msg = response.toString();
                                Log.e(TAG, msg);
                                if (msg.equals("error")) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            showToast("账户密码错误");
                                        }
                                    });

                                }
                                else{
                                    JMessageClient.login(account, account, new BasicCallback() {
                                        @Override
                                        public void gotResult(int i, String s) {
                                            Log.d("登陆jmsg",i+"；"+s);
                                        }
                                    });
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            OkHttpUtils.get()
                                                    .url("http://106.14.145.208/ShopMall/BackShopTalkMaster")
                                                    .build()
                                                    .execute(new StringCallback() {
                                                        @Override
                                                        public void onError(Request request, Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                        @Override
                                                        public void onResponse(String response) {
                                                            MyApplication.setBoss_id(response);
                                                       }
                                                    });
                                        }
                                    }).start();
                                    //解析json过去mainActivity中 或 直接传过去  intend
                                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                    intent.putExtra("user_json",msg);
                                    startActivity(intent);
                                    MyApplication.setUser_id(account);
                                    JPushInterface.setAlias(getApplicationContext(),1,account);
                                    //存储到本地文件中同时给他默认的头像路径并不上传至服务端
                                    SharedPreferences.Editor editor = getSharedPreferences("user_data",MODE_PRIVATE).edit();
                                    editor.putString("phone",account);
                                    editor.putString("password",password);
                                    editor.apply();


                                    Gson gson = new Gson();
                                    String data = response.toString();
                                    List<User> list = new ArrayList<User>();
                                    Type listType = new TypeToken<List<User>>() {}.getType();
                                    list = gson.fromJson(data, listType);
                                     ACache mAcache = ACache.get(MyApplication.getContext(),account);
                                        for(User user :list){
                                            if("".equals(user.getUser_name())||user.getUser_name()==null){
                                             return ;
                                            }else {
                                                SharedPreferences.Editor ed = getSharedPreferences("share_touxiang",MODE_PRIVATE).edit();
                                                ed.putString("tou",user.getUser_touxiang());
                                                ed.apply();
                                                mAcache.put("user_name", user.getUser_name());
                                            }
                                            mAcache.put("user_jifen",user.getUser_jifen());  //积分默认为0
                                            if("".equals(user.getUser_sex())||user.getUser_sex()==null){
                                                return ;
                                            }else {
                                                mAcache.put("user_sex", user.getUser_sex());
                                            }
                                            if("".equals(user.getUser_addr())||user.getUser_addr()==null){
                                                return ;
                                            }else {
                                                mAcache.put("user_addr", user.getUser_addr());
                                            }
                                            if("".equals(user.getUser_touxiang())||user.getUser_touxiang()==null){
                                                return ;
                                            }else{
                                                mAcache.put("user_touxiang",user.getUser_touxiang());
                                            }
                                            if("".equals(user.getUser_birth())||user.getUser_birth()==null){
                                               return ;
                                            }else{
                                                mAcache.put("user_birth",user.getUser_birth());
                                            }
                                            if("".equals(user.getUser_phone())||user.getUser_phone()==null){
                                                return ;
                                            }else{
                                                mAcache.put("user_phone",user.getUser_phone());
                                                Log.e(TAG+"  ACAHCE",mAcache.getAsString("user_phone"));
                                            }
                                        }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            showToast("登陆成功");
                                        }
                                    });
                                }
                            }
                        });

            }
        }).start();
    }
}
