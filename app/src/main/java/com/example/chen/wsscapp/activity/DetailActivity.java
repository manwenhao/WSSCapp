package com.example.chen.wsscapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.BaseActivity;
import com.example.chen.wsscapp.Util.GetTel;
import com.example.chen.wsscapp.Util.PayResult;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by chen on 2018/5/7.
 */
public class DetailActivity extends BaseActivity {
    private static final String TAG = "DetailActivity";
    private TextView tv_sendtype;
    private TextView tv_allmoney;
    private CheckBox cb_usejifen;
    private TextView tv_jifencost;
    private TextView tv_jisuan;
    private String products,allprice,sendtype;
    private Float getJifen;  //从后台获取的积分
    private  int cost; //抵扣的金额
    private String endmoney;
    private String rev_name,rev_phone,rev_address;

    private static final int SDK_PAY_FLAG = 1;

    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                       showToast("支付成功");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Map<String,String> map = new HashMap<String, String>();
                                try {
                                    map.put("ord_user", GetTel.gettel());
                                    map.put("products", URLEncoder.encode(products,"UTF-8"));
                                    map.put("money",endmoney);
                                    map.put("taketype",sendtype);
                                    if(!TextUtils.isEmpty(rev_address)&&!TextUtils.isEmpty(rev_name)&&!TextUtils.isEmpty(rev_phone)){
                                        map.put("rev_name",rev_name);
                                        map.put("rev_phone",rev_phone);
                                        map.put("rev_addr",rev_address);
                                    }
                                    if(cost!=0) {
                                        map.put("usejf", String.valueOf(cost * 100));
                                    }
                                    Log.e(TAG,products+"\n"+endmoney+"\n"+sendtype+"\n"+rev_name+"\n"+rev_address+"\n"+rev_phone+"\n"+cost);
                                    OkHttpUtils.get()
                                            .params(map)
                                            .url("http://106.14.145.208/ShopMall/SubmitUsersOrder")
                                            .build()
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onError(Request request, Exception e) {
                                                    uitoast("购买失败");
                                                }

                                                @Override
                                                public void onResponse(String response) {
                                                    if("ok".equals(response.toString())){
                                                        uitoast("购买成功");
                                                    }else{
                                                        uitoast("购买失败");
                                                    }


                                                }
                                            });
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }


                            }
                        }).start();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                           showToast("支付结果确认中");

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                           showToast("支付失败");

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        products = intent.getStringExtra("Products");
        allprice = intent.getStringExtra("allmoney");
        sendtype = intent.getStringExtra("sendType");
        if(!TextUtils.isEmpty(intent.getStringExtra("rev_name"))){
            rev_name = intent.getStringExtra("rev_name");
        }
        if(!TextUtils.isEmpty(intent.getStringExtra("rev_address"))){
            rev_address = intent.getStringExtra("rev_address");
        }
        if(!TextUtils.isEmpty(intent.getStringExtra("rev_phone"))){
            rev_phone = intent.getStringExtra("rev_phone");
        }
        initView();
    }

    private void initView() {
        tv_sendtype = (TextView) findViewById(R.id.tv_sendtype);
        tv_allmoney = (TextView) findViewById(R.id.tv_allmoney);
        cb_usejifen = (CheckBox) findViewById(R.id.cb_usejifen);
        tv_jifencost = (TextView) findViewById(R.id.tv_jifencost);
        tv_jisuan = (TextView) findViewById(R.id.tv_jisuan);
        if("0".equals(sendtype)){
            tv_sendtype.setText("商家配送");
        }else if("8".equals(sendtype)){
            tv_sendtype.setText("店铺自提");
        }else{
            tv_sendtype.setText("未知");
        }
        tv_allmoney.setText(allprice);
        cb_usejifen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    getJifen();
                    tv_jifencost.setVisibility(View.VISIBLE);
                }else{
                    tv_allmoney.setText(allprice);
                    tv_jifencost.setVisibility(View.INVISIBLE);
                }
            }
        });

        tv_jisuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_usejifen.isChecked()) {
                    Log.e(TAG, cost + "\n" + endmoney + "\n" + products + "\n" + sendtype);
                }else{
                    cost = 0 ;
                    endmoney = allprice;
                    Log.e(TAG, cost + "\n" + endmoney + "\n" + products + "\n" + sendtype);
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpUtils.get()
                                .addParams("ord_user",GetTel.gettel())
                                .addParams("money",endmoney)
                                .url("http://106.14.145.208/ShopMall/BackAppAlipayOrderString")
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Request request, Exception e) {
                                        uitoast("调起支付宝失败");
                                    }

                                    @Override
                                    public void onResponse(final String response) {
                                        if("error".equals(response)){
                                            uitoast("调起支付宝失败");
                                        }else{
                                            Runnable payRunnable = new Runnable() {
                                                @Override
                                                public void run() {
                                                    PayTask alipay = new PayTask(DetailActivity.this);
                                                    String result = alipay.pay(response,true);
                                                    Message msg = new Message();
                                                    msg.what = SDK_PAY_FLAG;
                                                    msg.obj = result;
                                                    mHandler.sendMessage(msg);
                                                }
                                            };
                                            Thread payThread = new Thread(payRunnable);
                                            payThread.start();
                                        }

                                    }
                                });

                    }
                }).start();

            }
        });
    }


    public void getJifen() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpUtils.get()
                            .addParams("user_phone", GetTel.gettel())
                            .addParams("products",URLEncoder.encode(products,"UTF-8"))
                            .url("http://106.14.145.208/ShopMall/BackUserRedJF")
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Request request, Exception e) {
                                    Log.e(TAG,e.getMessage());
                                    uitoast("查询积分失败，请稍后再试");
                                }

                                @Override
                                public void onResponse(String response) {
                                    if("error".equals(response.toString())){
                                        uitoast("查询积分失败，请稍后再试");
                                    }else{
                                        getJifen = Float.valueOf(response.toString());
                                        cost = (int) (getJifen/100);
                                        endmoney = GetTel.getFloat(Float.valueOf(allprice) - Float.valueOf(cost) );
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(Float.valueOf(allprice)>Float.valueOf(cost)){
                                                    tv_jifencost.setText("-"+cost);
                                                    if(Float.valueOf(endmoney)>1)
                                                    {
                                                        tv_allmoney.setText(endmoney);
                                                    }else{
                                                        endmoney="0"+endmoney;
                                                        tv_allmoney.setText(endmoney);
                                                    }
                                                }else if(Float.valueOf(allprice)<Float.valueOf(cost)){
                                                    cost = (int)Float.parseFloat(allprice);
                                                    endmoney =  GetTel.getFloat(Float.valueOf(allprice) - Float.valueOf(cost)) ;
                                                    tv_jifencost.setText("-"+cost);
                                                    if(".00".equals(endmoney)){
                                                        endmoney = "0.01";
                                                        tv_allmoney.setText(endmoney);
                                                    }else {
                                                        endmoney="0"+endmoney;
                                                        tv_allmoney.setText(endmoney);
                                                    }
                                                }else{
                                                    endmoney = "0.01";
                                                    tv_allmoney.setText(endmoney);
                                                }
                                            }
                                        });

                                    }

                                }
                            });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }
}
