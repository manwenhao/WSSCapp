package com.example.chen.wsscapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.ACache;
import com.example.chen.wsscapp.Util.BaseActivity;
import com.example.chen.wsscapp.Util.GetTel;
import com.example.chen.wsscapp.Util.MyApplication;
import com.example.chen.wsscapp.Util.TopUi;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by chen on 2018/3/26.
 */

public class SetAddrActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "setAddrActivity";
    private Button bt_setaddr;
    private EditText et_setaddr;
    final ACache mAcahe = ACache.get(MyApplication.getContext(), GetTel.gettel());

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
        setContentView(R.layout.activity_setaddr);
        bt_setaddr = (Button) findViewById(R.id.toolbar_right_btn);
        et_setaddr = (EditText) findViewById(R.id.et_setaddr);
        et_setaddr.setText(mAcahe.getAsString("user_addr"));
        bt_setaddr.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_right_btn:
                String addr = et_setaddr.getText().toString();
                SharedPreferences pref = getSharedPreferences("user_data",MODE_PRIVATE);
                String idphone = pref.getString("phone","");
                setAddr(idphone,addr);
                break;
        }
    }



    /**
     * 修改设置地址
     *idphone 为手机号码
     *addr 为地址
     */
    private void setAddr(final String idphone, final String addr) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpUtils.get()
                        .addParams("id",idphone)
                        .addParams("key","user_addr")
                        .addParams("value",addr)
                        .url("http://106.14.145.208/ShopMall/UpdateForUserInfo")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                toast("地址设置失败");
                            }

                            @Override
                            public void onResponse(String response) {
                                if(response.toString().equals("ok")){
                                    toast("地址修改成功");
                                    UserInfo userInfo= JMessageClient.getMyInfo();
                                    userInfo.setAddress(addr);
                                    JMessageClient.updateMyInfo(UserInfo.Field.address, userInfo, new BasicCallback() {
                                        @Override
                                        public void gotResult(int i, String s) {
                                            Log.d("修改地址",s);
                                        }
                                    });
                                    mAcahe.put("user_addr",addr);
                                    Intent intent = new Intent(SetAddrActivity.this,SetUserInfoActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else if(response.toString().equals("error")){
                                    toast("地址修改失败");
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
                Toast.makeText(SetAddrActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
