package com.example.chen.wsscapp.Util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chen.wsscapp.R;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

/**
 * Created by chen on 2018/5/15.
 */

public class SendByKuaidiDialog extends Dialog implements View.OnClickListener {
    private static final String TAG = "SendByKuaidiDialog";
    private EditText et_expressname;
    private EditText et_expressid;
    private Button bt_sendsure;
    private String user_phone;
    private String ord_id;
    private Context context;

    private Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Toast.makeText(context,"发货失败",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(context,"发货成功",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

        }
    };


    public SendByKuaidiDialog(Context context, String user_phone, String ord_id) {
        super(context);
        this.context = context;
        this.user_phone = user_phone;
        this.ord_id = ord_id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendbykuaidi);
        et_expressname = (EditText) findViewById(R.id.et_expressname);
        et_expressid = (EditText) findViewById(R.id.et_expressid);
        bt_sendsure = (Button) findViewById(R.id.bt_sendsure);
        bt_sendsure.setOnClickListener(this);
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        dialogWindow.setAttributes(p);
        this.setCancelable(true);
    }

    @Override
    public void onClick(View v) {
        final String expressname = et_expressname.getText().toString();
        final String expressid = et_expressid.getText().toString();

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .addParams("ord_user",user_phone)
                        .addParams("ord_id",ord_id)
                        .addParams("ord_status","1")
                        .addParams("expressname",expressname)
                        .addParams("expressid",expressid)
                        .url("http://106.14.145.208/ShopMall/UploadOrderStatus")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                Message msg = new Message();
                                msg.what = 1;
                                mhandler.sendMessage(msg);
                            }

                            @Override
                            public void onResponse(String response) {
                                if("ok".equals(response.toString())){
                                    Message msg = new Message();
                                    msg.what = 2;
                                    mhandler.sendMessage(msg);

                                }else{
                                    Message msg = new Message();
                                    msg.what = 1;
                                    mhandler.sendMessage(msg);
                                }

                            }
                        });
            }
        }).start();


    }
}
