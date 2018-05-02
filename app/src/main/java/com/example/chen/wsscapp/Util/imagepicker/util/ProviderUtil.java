package com.example.chen.wsscapp.Util.imagepicker.util;

import android.content.Context;


public class ProviderUtil {

    public static String getFileProviderName(Context context){
        return context.getPackageName()+".provider";
    }
}
