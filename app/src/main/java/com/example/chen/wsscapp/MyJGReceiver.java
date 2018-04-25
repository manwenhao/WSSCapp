package com.example.chen.wsscapp;

import android.content.Context;
import android.widget.Toast;

import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * Created by xuxuxiao on 2018/4/23.
 */

public class MyJGReceiver extends JPushMessageReceiver {

    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onAliasOperatorResult(context, jPushMessage);
        //Toast.makeText(context, "我是徐啸啊", Toast.LENGTH_SHORT).show();
    }

}
