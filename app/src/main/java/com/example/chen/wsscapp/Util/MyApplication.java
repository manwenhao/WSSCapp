package com.example.chen.wsscapp.Util;

import android.app.Application;
import android.content.Context;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by chen on 2018/3/17.
 */


//获取context工具类
public class MyApplication extends Application {
    private static Context context;
    public static String user_id;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

    }
    public static void setUser_id(String id){
        user_id=id;
    }
    public static String getUser_id(){
        return user_id;
    }
    public static Context getContext(){
        return context;
    }
}
