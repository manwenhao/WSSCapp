package com.example.chen.wsscapp.Util;

import android.content.SharedPreferences;

import java.text.DecimalFormat;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by xuxuxiao on 2018/4/19.
 *
 */

public class GetTel {
    //获取手机号
   public static String gettel(){
       SharedPreferences pref = MyApplication.getContext().getSharedPreferences("user_data",MODE_PRIVATE);
       String idphone = pref.getString("phone","");
       return idphone;
   }
    //float保留两位小数
    public  static String getFloat(Float price){
        DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p=decimalFormat.format(price);//format 返回的是字符串
        return p;
    }
}
