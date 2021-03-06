package com.example.chen.wsscapp.Util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.wsscapp.R;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

/**
 * Created by chen on 2018/5/3.
 */

public class ChooseSendDialog extends Dialog implements View.OnClickListener{
    private Context context;
    private TextView kdsend;
    private TextView bt_zqsned;
    private TextView bt_canclean;
    private String user_phone;
    private String ord_id;

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

    public ChooseSendDialog(Context context, String user_phone, String ord_id) {
        super(context);
        this.context = context;
        this.user_phone = user_phone;
        this.ord_id = ord_id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_sendshop);
        kdsend = (TextView) findViewById(R.id.kdsend);
        kdsend.setOnClickListener(this);
        bt_zqsned = (TextView) findViewById(R.id.bt_zqsned);
        bt_zqsned.setOnClickListener(this);
        bt_canclean = (TextView) findViewById(R.id.bt_canclean);
        bt_canclean.setOnClickListener(this);
        /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        dialogWindow.setAttributes(p);
        this.setCancelable(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.kdsend:
                this.dismiss();
                SendByKuaidiDialog e = new SendByKuaidiDialog(context,user_phone,ord_id);
                e.show();
                break;
            case R.id.bt_canclean:
                dismiss();
                break;
            case R.id.bt_zqsned:
                sendByShop(user_phone,ord_id);
                break;

        }

    }

    private void sendByShop(final String user_phone, final String ord_id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .addParams("ord_user",user_phone)
                        .addParams("ord_id",ord_id)
                        .addParams("ord_status","1")
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
