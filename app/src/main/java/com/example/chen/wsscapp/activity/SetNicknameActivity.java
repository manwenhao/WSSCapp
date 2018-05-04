package com.example.chen.wsscapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import static android.R.attr.id;

/**
 * Created by chen on 2018/3/17.
 */

public class SetNicknameActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "setNicknameActivity";
    private Button bt_setnickname;
    private EditText et_setnickname;
    final ACache mAcahe = ACache.get(MyApplication.getContext(),"userdata");
    public static final String action = "jason.broadcast.action";


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
        setContentView(R.layout.activity_setnickname);
        bt_setnickname = (Button) findViewById(R.id.toolbar_right_btn);
        et_setnickname = (EditText) findViewById(R.id.et_setnickname);
        et_setnickname.setText(mAcahe.getAsString("user_name"));
        bt_setnickname.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_right_btn:
                String user_name = et_setnickname.getText().toString();
                SharedPreferences pref = getSharedPreferences("user_data",MODE_PRIVATE);
                String idphone = pref.getString("phone","");
                setNickname(idphone,user_name);
                break;
        }
    }



    /**
     * 修改设置昵称
     *idphone 为手机号码
     *user_name 为昵称
     */
    private void setNickname(final String idphone,final String user_name) {
        if(TextUtils.isEmpty(user_name)){
            showToast("请输入昵称");
        }else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpUtils.get()
                            .addParams("id",idphone)
                            .addParams("key","user_name")
                            .addParams("value",user_name)
                            .url("http://106.14.145.208/ShopMall/UpdateForUserInfo")
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Request request, Exception e) {
                                    toast("更改昵称失败，网络出错");
                                }

                                @Override
                                public void onResponse(String response) {
                                    if(response.toString().equals("ok")){
                                        toast("更改昵称成功");
                                        UserInfo userInfo=JMessageClient.getMyInfo();
                                        userInfo.setNickname(user_name);
                                        JMessageClient.updateMyInfo(UserInfo.Field.nickname, userInfo, new BasicCallback() {
                                            @Override
                                            public void gotResult(int i, String s) {
                                                Log.d("修改nickname",s);
                                            }
                                        });
                                        mAcahe.put("user_name",user_name);
                                        Intent intent = new Intent(SetNicknameActivity.this,SetUserInfoActivity.class);
                                        startActivity(intent);
                                        //通知主页面昵称
                                        Intent intent1 = new Intent(action);
                                        sendBroadcast(intent1);
                                        finish();

                                    }else {
                                        toast("更改昵称失败");
                                        Log.e(TAG,response.toString());
                                    }

                                }
                            });

                }
            }).start();
        }

    }

    //toast的小方法
    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SetNicknameActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }
}