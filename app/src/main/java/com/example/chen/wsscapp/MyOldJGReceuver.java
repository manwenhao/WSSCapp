package com.example.chen.wsscapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.logging.Logger;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.data.JPushLocalNotification;

import static android.util.Log.d;
import static android.util.Log.w;
import static java.util.logging.Logger.*;

/**
 * Created by xuxuxiao on 2018/4/23.
 */

public class MyOldJGReceuver extends BroadcastReceiver {
    private static final String TAG = "MyJPushReceiver";
    public static String title;
    public static String cont;
    public static String extra;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle=intent.getExtras();
            title=bundle.getString(JPushInterface.EXTRA_TITLE);
            cont=bundle.getString(JPushInterface.EXTRA_MESSAGE);
            extra=bundle.getString(JPushInterface.EXTRA_EXTRA);
            if (intent.getAction().equals(JPushInterface.ACTION_MESSAGE_RECEIVED)){
                JPushLocalNotification ln = new JPushLocalNotification();
                ln.setBuilderId(0);
                ln.setTitle(title);
                ln.setContent(cont);
                ln.setNotificationId(11111111) ;
                JPushInterface.addLocalNotification(context, ln);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
