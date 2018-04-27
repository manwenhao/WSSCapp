package com.example.chen.wsscapp.Util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by chen on 2018/3/17.
 */


//获取context工具类
public class MyApplication extends Application {
    private static Context context;
    public static String user_id;
    public static int count=0;
    private ActivityLifecycleCallbacksImpl mActivityLifecycleCallbacksImpl;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        mActivityLifecycleCallbacksImpl=new ActivityLifecycleCallbacksImpl();
        this.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacksImpl);

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
    private class ActivityLifecycleCallbacksImpl implements ActivityLifecycleCallbacks{
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }
        @Override
        public void onActivityStarted(Activity activity) {
            count++;
        }
        @Override
        public void onActivityResumed(Activity activity) {
        }
        @Override
        public void onActivityPaused(Activity activity) {
        }
        @Override
        public void onActivityStopped(Activity activity) {
            count--;
        }
        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }
        @Override
        public void onActivityDestroyed(Activity activity) {
        }
    }
}
