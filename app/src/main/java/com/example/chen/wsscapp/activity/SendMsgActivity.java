package com.example.chen.wsscapp.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.BaseActivity;
import com.example.chen.wsscapp.Util.TopUi;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

/**
 * Created by chen on 2018/4/26.
 * 群发消息
 */

public class SendMsgActivity extends BaseActivity {

    private static final String TAG = "SendMsgActivity";
    private EditText et_sendmsg;
    private Button bt_suresend;

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
        setContentView(R.layout.activity_sendmsg);
        initView();

        bt_suresend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(et_sendmsg.getText().toString())){
                    showToast("请输入你要群发的消息");
                    return;
                }
                sendMsg();
            }
        });

    }

    private void sendMsg() {
        final String msg = et_sendmsg.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .addParams("content",msg)
                        .url("http://106.14.145.208/ShopMall/SendMsgToUsers")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                uitoast("发送失败");
                            }

                            @Override
                            public void onResponse(String response) {
                                Log.e(TAG,response);
                                if("ok".equals(response)){
                                    uitoast("发送成功");
                                }else if("error".equals(response)){
                                    uitoast("位置错误");
                                }else{
                                    uitoast(response);
                                }
                            }
                        });

            }
        }).start();
    }


    private void initView() {
        et_sendmsg = (EditText) findViewById(R.id.et_sendmsg);
        bt_suresend = (Button) findViewById(R.id.bt_suresend);
    }

}
