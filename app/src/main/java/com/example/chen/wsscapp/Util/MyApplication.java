package com.example.chen.wsscapp.Util;

import android.app.Application;
import android.content.Context;

/**
 * Created by chen on 2018/3/17.
 */


//获取context工具类
public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
