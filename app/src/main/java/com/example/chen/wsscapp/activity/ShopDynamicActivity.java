package com.example.chen.wsscapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.tu.loadingdialog.LoadingDailog;
import com.bumptech.glide.Glide;
import com.example.chen.wsscapp.Bean.GridItem;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.BaseActivity;
import com.example.chen.wsscapp.Util.GetTel;
import com.example.chen.wsscapp.Util.TopUi;
import com.example.chen.wsscapp.adapter.Myadapter;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;
import com.yuyh.library.imgsel.config.ISListConfig;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by chen on 2018/4/22.
 * 商家动态发送
 */

public class ShopDynamicActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ShopDynamicActivity";
    private Button bt_photo, bt_uploadinfo;
    private GridView gridView;
    private EditText et_dynamic;
    private ArrayList<GridItem> mGridData = null;
    private Myadapter mGridViewAdapter = null;
    private static final int REQUEST_LIST_CODE = 0;
    private List<File> files = null;
    private LoadingDailog dialog = null;
    private LinkedList<Runnable> taskList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopdynamic);
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

        bt_photo = (Button) findViewById(R.id.bt_photo);
        gridView = (GridView) findViewById(R.id.gv_photo);
        bt_uploadinfo = (Button) findViewById(R.id.bt_uploadinfo);
        et_dynamic = (EditText) findViewById(R.id.et_dynamic);
        bt_photo.setOnClickListener(this);
        bt_uploadinfo.setOnClickListener(this);

        ISNav.getInstance().init(new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_photo:
                goselectphoto();
                break;
            case R.id.bt_uploadinfo:
                String dynamic = et_dynamic.getText().toString();
                if(TextUtils.isEmpty(dynamic)){
                    showToast("输入你要发送的动态");
                    return;
                }
                uploadShopInfo(files,dynamic);
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LIST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra("result");
            Log.e(TAG,"pathlist"+pathList.size());
            Log.e(TAG,pathList.toString());
            mGridData = new ArrayList<GridItem>();
            files = new ArrayList<>();
            compressMore(pathList);
            for (String path : pathList) {
                Log.e(TAG,pathList.size()+"");
                Log.i("media",path);
                GridItem item = new GridItem();
                item.setImageView(path);
                // files.add(new File(path));
                mGridData.add(item);
            }
            Log.e(TAG,"grid "+mGridData.size());
            mGridViewAdapter = new Myadapter(this, R.layout.itemtu, mGridData);
            gridView.setAdapter(mGridViewAdapter);
        }

    }

    private void uploadShopInfo(final List<File> files, final String dynamic) {
        LoadingDailog.Builder loadBuilder=new LoadingDailog.Builder(this)
                .setMessage("上传中...")
                .setCancelable(false)
                .setCancelOutside(false);
        dialog=loadBuilder.create();
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient.Builder mbulider = new OkHttpClient.Builder();
                mbulider.connectTimeout(30, TimeUnit.MINUTES)
                        .writeTimeout(30, TimeUnit.MINUTES)
                        .readTimeout(30, TimeUnit.MINUTES);
                OkHttpClient mOkHttpClent = mbulider.build();
                Log.e(TAG, GetTel.gettel());
                MultipartBody.Builder builder=new MultipartBody.Builder()
                        .addFormDataPart("mat_phone", GetTel.gettel())
                        .addFormDataPart("content",dynamic)
                        .setType(MultipartBody.FORM);
                if (files != null){
                    for (File file : files) {
                        Log.e(TAG,file.getName());
                        builder.addFormDataPart("imge", file.getName(), RequestBody.create(MediaType.parse("image/jpg"), file));
                    }
                }
                RequestBody requestBody = builder.build();
                final okhttp3.Request request = new okhttp3.Request.Builder()
                        .url("http://106.14.145.208/ShopMall/UploadShopDynamic")
                        .post(requestBody)
                        .build();
                Call call = mOkHttpClent.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ShopDynamicActivity.this,"上传超时失败",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String resp = response.body().string();
                        Log.e(TAG,resp);
                        if(resp.equals("ok")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ShopDynamicActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });

                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ShopDynamicActivity.this,"上传失败",Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });

                        }

                    }
                });

            }
        }).start();
    }

    private void goselectphoto() {
        // 自由配置选项
        ISListConfig config = new ISListConfig.Builder()
                // 是否多选, 默认true
                .multiSelect(true)
                // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                .rememberSelected(true)
                // “确定”按钮背景色
                .btnBgColor(Color.GRAY)
                // “确定”按钮文字颜色
                .btnTextColor(Color.BLUE)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#3F51B5"))
                // 返回图标ResId
                .titleColor(Color.WHITE)
                // TitleBar背景色
                .cropSize(1, 1, 200, 200)
                .needCrop(false)
                // 第一个是否显示相机，默认true
                .needCamera(false)
                // 最大选择图片数量，默认9
                .maxNum(12)
                .build();

// 跳转到图片选择器
        ISNav.getInstance().toListActivity(this, config, REQUEST_LIST_CODE);
    }


    //Lunban图片压缩
    private void compressMore(List<String> pathList) {
        final Handler handler = new Handler();
        class  Task implements Runnable{
            String path;
            Task(String path){
                this.path = path;
            }
            @Override
            public void run() {
                Luban.get(ShopDynamicActivity.this)
                        .load(new File(path))                     //传人要压缩的图片
                        .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
                        .setCompressListener(new OnCompressListener() { //设置回调
                            @Override
                            public void onStart() {

                            }
                            @Override
                            public void onSuccess(File file) {
                                files.add(file);
                                if(!taskList.isEmpty()){
                                    Runnable runnable = taskList.pop();
                                    handler.post(runnable);
                                }
                            }
                            @Override
                            public void onError(Throwable e) {
                            }
                        }).launch();    //启动压缩
            }
        }

        //循环遍历原始路径 添加至linklist中
        for (String path :pathList){
            taskList.add(new Task(path));
        }
        handler.post(taskList.pop());

    }
}
