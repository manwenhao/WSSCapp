package com.example.chen.wsscapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.ACache;
import com.example.chen.wsscapp.Util.BaseActivity;
import com.example.chen.wsscapp.Util.MyApplication;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

/**
 * Created by chen on 2018/3/26.
 */

public class SetAddrActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "setAddrActivity";
    private Button bt_setaddr;
    private EditText et_setaddr;
    final ACache mAcahe = ACache.get(MyApplication.getContext(),"userdata");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
