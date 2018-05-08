package com.example.chen.wsscapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.BaseActivity;
import com.example.chen.wsscapp.Util.GetTel;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

/**
 * Created by chen on 2018/5/8.
 */
public class AddneAddrActivity extends BaseActivity {
    private static final String TAG = "AddneAddrActivity";
    private Button toolbar_right_btn;
    private TextView toolbar_right_tv;
    private EditText et_newuser,et_newphone,et_newaddr;
    private String newuser,newphone,newaddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnewaddr);
        initView();
        initListener();

    }

    private void initListener() {
        toolbar_right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newuser = et_newuser.getText().toString();
                newphone = et_newphone.getText().toString();
                newaddr = et_newaddr.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG,newaddr+newphone+newuser);
                        OkHttpUtils.get()
                                .addParams("rev_name",newuser)
                                .addParams("rev_phone",newphone)
                                .addParams("rev_address",newaddr)
                                .addParams("ord_phone", GetTel.gettel())
                                .url("http://106.14.145.208/ShopMall/AddUserCommonAddr")
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Request request, Exception e) {
                                        uitoast("添加新地址失败");

                                    }

                                    @Override
                                    public void onResponse(String response) {
                                        Log.e(TAG,response);
                                        if("ok".equals(response.toString())){
                                            uitoast("添加新地址成功");
                                        }else{
                                            uitoast("添加新地址失败");
                                        }

                                    }
                                });

                    }
                }).start();


            }
        });
    }

    private void initView() {
        toolbar_right_btn = (Button) findViewById(R.id.toolbar_right_btn);
        toolbar_right_tv = (TextView) findViewById(R.id.toolbar_right_tv);
        toolbar_right_tv.setTextColor(getResources().getColor(R.color.text_blue));
        et_newuser = (EditText) findViewById(R.id.et_newuser);
        et_newphone = (EditText) findViewById(R.id.et_newphone);
        et_newaddr = (EditText) findViewById(R.id.et_newaddr);

    }
}
