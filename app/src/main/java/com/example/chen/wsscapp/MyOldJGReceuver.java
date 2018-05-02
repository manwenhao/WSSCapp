package com.example.chen.wsscapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.chen.wsscapp.Bean.PushMsg;
import com.example.chen.wsscapp.Util.ACache;
import com.example.chen.wsscapp.Util.MyApplication;
import com.example.chen.wsscapp.activity.MainActivity;
import com.example.chen.wsscapp.activity.PushInfo;
import com.example.chen.wsscapp.fragment.PushFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    public static String title;
    public static String cont;
    public static String extra;
    public static String datastring;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {

            if (intent.getAction().equals(JPushInterface.ACTION_MESSAGE_RECEIVED)){
                Bundle bundle=intent.getExtras();
                title=bundle.getString(JPushInterface.EXTRA_TITLE);
                cont=bundle.getString(JPushInterface.EXTRA_MESSAGE);
                extra=bundle.getString(JPushInterface.EXTRA_EXTRA);
                SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                datastring=sf.format(new Date());
                JPushLocalNotification ln = new JPushLocalNotification();
                ln.setBuilderId(0);
                ln.setTitle(title);
                ln.setContent(cont);
                ln.setNotificationId(11111111) ;
                JPushInterface.addLocalNotification(context, ln);
                ACache aCache=ACache.get(MyApplication.getContext(),MyApplication.getUser_id());
                String pushdate=aCache.getAsString("pushinfo");
                Type type=new TypeToken<List<PushMsg>>(){}.getType();
                List<PushMsg> list;
                if (pushdate==null){
                    list=new ArrayList<PushMsg>();
                }else {
                    list=new Gson().fromJson(pushdate,type);
                }
                PushMsg e=new PushMsg();
                e.setTitle(title);
                e.setContent(cont);
                e.setTime(datastring);
                e.setStatus("0");
                Log.d(" notification","title:"+title);
                Log.d(" notification","content:"+cont);
                Log.d(" notification",e.toString());
                list.add(0,e);
                aCache.put("pushinfo",new Gson().toJson(list));
                if (PushFragment.flag==1){
                    Intent intent1=new Intent("newpush");
                    MyApplication.getContext().sendBroadcast(intent1);
                }
            }
            if (intent.getAction().equals(JPushInterface.ACTION_NOTIFICATION_OPENED)){
                PushMsg e=new PushMsg();
                e.setTitle(title);
                e.setContent(cont);
                e.setTime(datastring);
                e.setStatus("0");
                Log.d("open notification","title:"+title);
                Log.d("open notification","content:"+cont);
                Log.d("open notification",e.toString());
                Intent intent1=new Intent(MyApplication.getContext(), PushInfo.class);
                intent1.putExtra("bean",e);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
