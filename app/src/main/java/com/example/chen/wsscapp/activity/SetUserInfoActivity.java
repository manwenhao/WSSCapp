package com.example.chen.wsscapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.ACache;
import com.example.chen.wsscapp.Util.BaseActivity;
import com.example.chen.wsscapp.Util.MyApplication;

import qiu.niorgai.StatusBarCompat;


/**
 * Created by chen on 2018/3/15.
 */

public class SetUserInfoActivity extends BaseActivity implements View.OnClickListener{

   /* private RadioGroup rg_sex;
    private RadioButton rb_man,rb_woman;*/
    private TextView tv_newnickname,tv_newsex,tv_newaddr,tv_newphone,tv_newbirth;
    private LinearLayout ll_nickname,ll_sex,ll_addr,ll_phone,ll_birth;

    String user_sex,user_name,user_addr,user_phone,user_birth;   //传递的值
    String TAG = "SetUserInfoActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setuserinfo);
        initView();
    }

    private void initView() {
        ACache aCache = ACache.get(MyApplication.getContext(),"userdata");
        SharedPreferences pref = getSharedPreferences("user_data",MODE_PRIVATE);
        tv_newnickname = (TextView) findViewById(R.id.tv_newnickname);
        tv_newnickname.setText(aCache.getAsString("user_name"));
        ll_nickname = (LinearLayout) findViewById(R.id.ll_nickname);
        tv_newsex = (TextView) findViewById(R.id.tv_newsex);
        tv_newsex.setText(aCache.getAsString("user_sex"));
        ll_sex = (LinearLayout) findViewById(R.id.ll_sex);
        tv_newaddr = (TextView) findViewById(R.id.tv_newaddr);
        tv_newaddr.setText(aCache.getAsString("user_addr"));
        ll_addr = (LinearLayout) findViewById(R.id.ll_addr);
        tv_newphone = (TextView) findViewById(R.id.tv_newphone);
        String phone = pref.getString("phone","").substring(0,3)+"****"+pref.getString("phone","").substring(7,11);
        tv_newphone.setText(phone);
        ll_phone = (LinearLayout) findViewById(R.id.ll_phone);
        tv_newbirth = (TextView) findViewById(R.id.tv_newbirth);
        tv_newbirth.setText(aCache.getAsString("user_birth"));
        ll_birth = (LinearLayout) findViewById(R.id.ll_birth);
        ll_nickname.setOnClickListener(this);
        ll_sex.setOnClickListener(this);
        ll_addr.setOnClickListener(this);
        ll_phone.setOnClickListener(this);
        ll_birth.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_nickname:
                Intent intent = new Intent(this,SetNicknameActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.ll_sex:
                Intent intent1 = new Intent(this,SetSexActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.ll_addr:
                Intent intent2 = new Intent(this,SetAddrActivity.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.ll_phone:
                showToast("电话设置");
                break;
            case R.id.ll_birth:
                Intent intent4 = new Intent(this,SetBirthActivity.class);
                startActivity(intent4);
                finish();
                break;
            default: break;

        }

    }





    //toast的小方法
    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SetUserInfoActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
