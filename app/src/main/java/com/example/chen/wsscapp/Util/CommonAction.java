package com.example.chen.wsscapp.Util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 2018/5/15.
 */

public class CommonAction {

    private List<Activity> AllActivitites = new ArrayList<Activity>();
    private static CommonAction instance;

    public CommonAction() {

    }

    public synchronized static CommonAction getInstance() {
        if (null == instance) {
            instance = new CommonAction();
        }
        return instance;
    }

    //在Activity基类的onCreate()方法中执行
    public void addActivity(Activity activity) {
        AllActivitites.add(activity);
    }

    //注销是销毁所有的Activity
    public void OutSign() {
        for (Activity activity : AllActivitites) {
            if (activity != null) {
                activity.finish();
            }
        }
    }
}
