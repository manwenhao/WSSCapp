package com.example.chen.wsscapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.ACache;
import com.example.chen.wsscapp.Util.BaseActivity;
import com.example.chen.wsscapp.Util.MyApplication;

import com.example.chen.wsscapp.Util.TopUi;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;
import com.yuyh.library.imgsel.config.ISListConfig;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by chen on 2018/3/28.
 */

public class SetTouxiangActivity extends BaseActivity implements View.OnClickListener {
    private Button bt_settouxiang;
    private ImageView iv_icon;
    private static final int REQUEST_LIST_CODE = 0;
    private static final String TAG = "SetTouxiangActivity";
    private static String time; //修改头像的时间
    final ACache mAcache = ACache.get(MyApplication.getContext(),"userdata");
    final ACache aCache = ACache.get(MyApplication.getContext(),"icondata");
    public static final String action = "jason.broadcast.action";
    String url = "http://106.14.145.208";


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
        setContentView(R.layout.activity_settouxiang);
        initView();
    }

    private void initView() {
        bt_settouxiang = (Button) findViewById(R.id.toolbar_right_btn);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);


        //设置图片宽高
        int width = iv_icon.getContext().getResources().getDisplayMetrics().widthPixels;
        int height = width;
        iv_icon.setLayoutParams(new LinearLayout.LayoutParams(width , height));

        refreshIcon();

        // 自定义图片加载器
        ISNav.getInstance().init(new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });

        bt_settouxiang.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.toolbar_right_btn:
                Iconselect(view);
        }

    }

    private void Iconselect(View view) {
        ISListConfig config = new ISListConfig.Builder()
                .multiSelect(false)
                .titleBgColor(Color.parseColor("#0F0F0F"))
                .cropSize(1, 1, 500, 500)
                .needCrop(true)
                .build();
        ISNav.getInstance().toListActivity(this, config, REQUEST_LIST_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences pref = getSharedPreferences("user_data",MODE_PRIVATE);
        if (requestCode == REQUEST_LIST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra("result");
            String path = pathList.get(0);
            Log.i("ImagePathList", path);
            String idphone = pref.getString("phone","");
            sendIconRequest(idphone,path);
        }

    }


    private void sendIconRequest(final String idphone, final String path){
        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient mOkHttpClent = new OkHttpClient();
                final File file = new File(path);
                MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("user_id",idphone)
                        .addFormDataPart("user_touxiang", file.getName(),
                                RequestBody.create(MediaType.parse("image/jpg"), file));

                RequestBody requestBody = builder.build();

                Request request = new Request.Builder()
                        .url("http://106.14.145.208/ShopMall/UpdateForUserPhoto")
                        .post(requestBody)
                        .build();
                Log.e(TAG,file.getName());
                Log.e(TAG,"path "+path);
                aCache.put("icon",path);
                Call call = mOkHttpClent.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "onFailure: "+e );
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SetTouxiangActivity.this,"修改照片失败，请检查网络",Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        final String respdata = response.body().string();
                        Log.e(TAG,respdata);
                        if (!respdata.equals("error")){
                            JMessageClient.updateUserAvatar(file, new BasicCallback() {
                                @Override
                                public void gotResult(int i, String s) {
                                    Log.d("修改头像",i+";"+s);
                                }
                            });
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if("ok".equals(respdata)){
                                    //保存头像路径到本地
                                    mAcache.put("user_touxiang",path);
                                    Toast.makeText(SetTouxiangActivity.this,"修改照片成功",Toast.LENGTH_SHORT).show();
                                    //保存更改时间作为签名
                                    Calendar calendar = Calendar.getInstance();
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                    time = format.format(calendar.getTime());
                                    SharedPreferences.Editor editor = getSharedPreferences("time",
                                            MODE_PRIVATE).edit();
                                    editor.putString("icontime",time);
                                    editor.apply();
                                    Log.e(TAG,time);

                                    //刷新头像
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Glide.with(MyApplication.getContext())
                                                    .load(url)
                                                    .error(R.drawable.ic_default_image)
                                                    .signature(new StringSignature(time))
                                                    .into(iv_icon);
                                        }
                                    });

                                    //通知主页面刷新头像
                                    Intent intent = new Intent(action);
                                    sendBroadcast(intent);
                                    finish();
                                }else if("error".equals(respdata)){
                                    Toast.makeText(SetTouxiangActivity.this,"修改照片失败",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        });
                    }
                });

            }

        }).start();

    }

    public void refreshIcon() {

        //读取存放的头像路径
        String iconaddr = mAcache.getAsString("user_touxiang");
        Log.d(TAG,"iconaddr "+iconaddr);
        SharedPreferences pref = getSharedPreferences("time",MODE_PRIVATE);
        String qm = pref.getString("icontime","");
        if (TextUtils.isEmpty(iconaddr)){
            iv_icon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_default_image));
        }else {
            Glide.with(this)
                    .load(aCache.getAsString("icon"))
                    .error(R.drawable.ic_default_image)
                    .signature(new StringSignature(qm))
                    .into(iv_icon);
        }

    }


    //toast的小方法
    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SetTouxiangActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
