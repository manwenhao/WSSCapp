package com.example.chen.wsscapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.BaseActivity;
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
        setContentView(R.layout.activity_sendmsg);
        initView();

        bt_suresend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
