package com.example.chen.wsscapp.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.chen.wsscapp.Bean.JiFen;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.BaseActivity;
import com.example.chen.wsscapp.Util.GetTel;
import com.example.chen.wsscapp.Util.TopUi;
import com.example.chen.wsscapp.Util.takevideo.utils.LogUtils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

/**
 * Created by chen on 2018/5/6.
 */
public class ShowJiFenActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "ShowJiFenActivity";
    private Button  bt_right;
    private TextView rightTvText;
    private TextView tv_whiteshowjifen,tv_redshowjifen,tv_tixian,tv_seetxist;
    private EditText et_zhifbaccount;
    private Button bt_tixian;
    private String txjifen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //判断是否为小米或魅族手机，如果是则将状态栏文字改为黑色
            if (TopUi.MIUISetStatusBarLightMode(this, true) || TopUi.FlymeSetStatusBarLightMode(this, true)) {
                //设置状态栏为指定颜色
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0
                    this.getWindow().setStatusBarColor(getResources().getColor(R.color.topbackgroud));
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4
                    //调用修改状态栏颜色的方法
                    this.getWindow().setStatusBarColor(getResources().getColor(R.color.topbackgroud));
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //如果是6.0以上将状态栏文字改为黑色，并设置状态栏颜色
                this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                this.getWindow().setStatusBarColor(getResources().getColor(R.color.topbackgroud));

            }
        }
        setContentView(R.layout.activity_showjifen);
        initView();
    }

    private void initView() {
        bt_right = (Button) findViewById(R.id.toolbar_right_btn);
        rightTvText = (TextView) findViewById(R.id.toolbar_right_tv);
        rightTvText.setTextColor(getResources().getColor(R.color.text_blue));
        tv_whiteshowjifen = (TextView) findViewById(R.id.tv_whiteshowjifen);
        tv_redshowjifen = (TextView) findViewById(R.id.tv_redshowjifen);
        tv_tixian = (TextView) findViewById(R.id.tv_tixian);
        tv_seetxist = (TextView) findViewById(R.id.tv_seetxist);
        et_zhifbaccount = (EditText) findViewById(R.id.et_zhifbaccount);
        bt_tixian = (Button) findViewById(R.id.bt_tixian);
        initJifenData();
        bt_tixian.setOnClickListener(this);
        bt_right.setOnClickListener(this);
        tv_seetxist.setOnClickListener(this);

    }



    private void initJifenData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .addParams("user_phone", GetTel.gettel())
                        .url("http://106.14.145.208/ShopMall/BackAppUserJF")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                Log.e(TAG,e.getMessage());
                                uitoast("获取积分数据失败");
                            }

                            @Override
                            public void onResponse(String response) {
                                if("error".equals(response.toString())){
                                    Log.e(TAG,response.toString());
                                    uitoast("获取积分数据失败");
                                }else{
                                    List<JiFen> jiFens = JSON.parseArray(response.toString(), JiFen.class);
                                    for(final JiFen e: jiFens){
                                       runOnUiThread(new Runnable() {
                                           @Override
                                           public void run() {
                                               tv_whiteshowjifen.setText(e.getWhitejf());
                                               tv_redshowjifen.setText(e.getRedjf());
                                               Float tx = Float.valueOf(e.getRedjf());
                                               if(tx/20000>=1){
                                                   tv_tixian.setText(""+(int)(tx/20000)+"00");
                                               }else{
                                                   tv_tixian.setText("0");
                                               }
                                           }
                                       });
                                    }
                                }

                            }
                        });

            }
        }).start();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_right_btn:
                Intent intent = new Intent(this,ShowJifenInfo.class);
                startActivity(intent);
                break;
            case R.id.bt_tixian:
                String zfbstr = et_zhifbaccount.getText().toString();
                getMoney(zfbstr);
                break;
            case R.id.tv_seetxist:
                Intent intent1 = new Intent(this,ShowTiXianListActicity.class);
                startActivity(intent1);
                break;
        }

    }

    private void getMoney(final String zfbstr) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .addParams("user_phone",GetTel.gettel())
                        .url("http://106.14.145.208/ShopMall/BackUserRealRedJF")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                uitoast("失败");
                            }

                            @Override
                            public void onResponse(String response) {
                                txjifen = response.toString();
                                realgetMoney(txjifen,zfbstr);
                            }
                        });


            }
        }).start();
    }

    private void realgetMoney(String txjifen, final String zfbstr) {
        String allMoney = null;
        String monrystr = txjifen;
        Float monetValue = Float.valueOf(monrystr);
        Log.e(TAG,monetValue+"");
        if(monetValue/20000>=1){
            allMoney = (int)(monetValue/20000)+"00";
        }else{
            uitoast("积分不足无法提现");
            return;

        }
        final String finalAllMoney = allMoney;
        Log.e(TAG,allMoney);
        Log.e(TAG,zfbstr);
        Log.e(TAG,monrystr);
        new Thread(new Runnable() {

            @Override
            public void run() {
                OkHttpUtils.get()
                        .addParams("user_phone",GetTel.gettel())
                        .addParams("zfbid",zfbstr)
                        .addParams("money", finalAllMoney)
                        .url("http://106.14.145.208/ShopMall/SubmitReflectReq")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                uitoast("提现失败，未知错误");
                            }

                            @Override
                            public void onResponse(String response) {
                                if("ok".equals(response)){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            tv_tixian.setText("0");
                                        }
                                    });
                                    uitoast("提现申请成功,等待商家处理");
                                }else if("error".equals(response)){
                                    uitoast("提现失败");
                                }else{
                                    uitoast(response);
                                }

                            }
                        });


            }
        }).start();
    }


}
