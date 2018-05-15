package com.example.chen.wsscapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.MyApplication;
import com.example.chen.wsscapp.Util.TopUi;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

public class SetNewphoneActivity extends AppCompatActivity {

    private static final String TAG = "activity_setnewphone";

     private Button bt_setnewphone;
    private EditText et_setnewphone;
    private   SharedPreferences pref;

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
        setContentView(R.layout.activity_setnewphone);

        bt_setnewphone = (Button) findViewById(R.id.confirm_btn);
        et_setnewphone = (EditText) findViewById(R.id.input_phone_new);

        bt_setnewphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("===","click");
                String newnumber = et_setnewphone.getText().toString();

                pref = getSharedPreferences("user_data",MODE_PRIVATE);
                String idphone = pref.getString("phone","");
                setNewnumber(idphone,newnumber);
            }
        });
    }




    /**
     * 修改设置号码
     *idphone 为手机号码
     *new_number 为新的手机号
     */
    private void setNewnumber(final String idphone, final String new_number) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpUtils.get()
                        .addParams("id",idphone)
                        .addParams("key","user_phone")
                        .addParams("value",new_number)
                        .url("http://106.14.145.208/ShopMall/UpdateForUserInfo")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        toast("号码设置失败");
                                    }
                                });
                            }

                            @Override
                            public void onResponse(String response) {
                                Log.d("=====",response);
                                if(response.equals("ok")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            createProgressBar();
                                            JMessageClient.getUserInfo(MyApplication.getUser_id(), new GetUserInfoCallback() {
                                                @Override
                                                public void gotResult(int i, String s, UserInfo userInfo) {
                                                    String nickname=userInfo.getNickname();
                                                    File file=userInfo.getAvatarFile();
                                                    JMessageClient.logout();
                                                    JMessageClient.register(new_number, new_number, new BasicCallback() {
                                                        @Override
                                                        public void gotResult(int i, String s) {

                                                        }
                                                    });
                                                    JMessageClient.login(new_number, new_number, new BasicCallback() {
                                                        @Override
                                                        public void gotResult(int i, String s) {

                                                        }
                                                    });
                                                    JMessageClient.updateUserAvatar(file, new BasicCallback() {
                                                        @Override
                                                        public void gotResult(int i, String s) {
                                                            Log.d("修改头像",i+";"+s);
                                                        }
                                                    });
                                                    UserInfo userInfo1=JMessageClient.getMyInfo();
                                                    userInfo.setNickname(nickname);
                                                    JMessageClient.updateMyInfo(UserInfo.Field.nickname, userInfo, new BasicCallback() {
                                                        @Override
                                                        public void gotResult(int i, String s) {
                                                            Log.d("修改nickname",s);
                                                        }
                                                    });
                                                    JMessageClient.logout();

                                                }
                                            });

                                            toast("手机号码修改成功");

                                            pref.getString("phone",new_number);
                                            Intent intent = new Intent(SetNewphoneActivity.this,LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                }else if(response.equals("error")){
                                 runOnUiThread(new Runnable() {
                                     @Override
                                     public void run() {
                                         toast("手机号码修改失败");
                                     }
                                 });
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
                Toast.makeText(SetNewphoneActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void createProgressBar() {
        FrameLayout layout = (FrameLayout) findViewById(android.R.id.content);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        ProgressBar mProBar = new ProgressBar(this);
        mProBar.setLayoutParams(layoutParams);
        mProBar.setVisibility(View.VISIBLE);
        layout.addView(mProBar);
    }
}
