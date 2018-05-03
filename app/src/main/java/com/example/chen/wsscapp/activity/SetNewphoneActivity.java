package com.example.chen.wsscapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chen.wsscapp.R;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

public class SetNewphoneActivity extends AppCompatActivity {

    private static final String TAG = "activity_setnewphone";

     private Button bt_setnewphone;
    private EditText et_setnewphone;
    private   SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                                            toast("手机号码修改成功");
                                            JMessageClient.register(new_number, new_number, new BasicCallback() {
                                                @Override
                                                public void gotResult(int i, String s) {
                                                    Log.d("重新注册jmsg",s);
                                                }
                                            });
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
}
