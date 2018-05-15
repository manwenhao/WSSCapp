package com.example.chen.wsscapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.tu.loadingdialog.LoadingDailog;
import com.bumptech.glide.Glide;
import com.example.chen.wsscapp.Bean.GridItem;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.BaseActivity;
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
 * Created by chen on 2018/4/30.
 */

public class ChangePhotoActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "ChangePhotoActivity";
    private Button bt_changephoto,bt_changexcphoto;
    private GridView gv_photo,gv_xcphoto;
    private Button bt_uploadchangephoto,bt_uploadchangexcphoto;
    private String pro_id;
    private ArrayList<GridItem> mGridData = null;
    private Myadapter mGridViewAdapter = null;
    private static final int REQUEST_LIST_CODE = 0;
    private static final int REQUEST_XCLIST_CODE = 1;
    private List<File> files = new ArrayList<>();
    private List<File> xcfiles = new ArrayList<>();
    private LoadingDailog dialog = null;
    private LinkedList<Runnable> taskList = new LinkedList<>();
    private LinkedList<Runnable> taskList2 = new LinkedList<>();

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
        setContentView(R.layout.activity_changephoto);
        initView();
    }

    private void initView() {
        bt_changephoto = (Button) findViewById(R.id.bt_changephoto);
        bt_changexcphoto = (Button) findViewById(R.id.bt_changexcphoto);
        bt_uploadchangephoto = (Button) findViewById(R.id.bt_uploadchangephoto);
        bt_uploadchangexcphoto = (Button) findViewById(R.id.bt_uploadchangexcphoto);
        gv_photo = (GridView) findViewById(R.id.gv_photo);
        gv_xcphoto = (GridView) findViewById(R.id.gv_xcphoto);

        Intent intent = getIntent();
        pro_id = intent.getStringExtra("proid");
        bt_uploadchangephoto.setVisibility(View.INVISIBLE);
        bt_uploadchangexcphoto.setVisibility(View.INVISIBLE);
        bt_uploadchangephoto.setOnClickListener(this);
        bt_uploadchangexcphoto.setOnClickListener(this);
        bt_changexcphoto.setOnClickListener(this);
        bt_changephoto.setOnClickListener(this);

        ISNav.getInstance().init(new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_changephoto:
                gv_photo.setVisibility(View.VISIBLE);
                goselectphoto();
                gv_xcphoto.setVisibility(View.GONE);
                bt_uploadchangexcphoto.setVisibility(View.INVISIBLE);
                break;
            case R.id.bt_uploadchangephoto:
                upLoadChangephoto(files);
                bt_uploadchangephoto.setVisibility(View.INVISIBLE);
                break;
            case R.id.bt_changexcphoto:
                gv_xcphoto.setVisibility(View.VISIBLE);
                goselectxcphoto();
                gv_photo.setVisibility(View.GONE);
                bt_uploadchangephoto.setVisibility(View.INVISIBLE);
                break;
            case R.id.bt_uploadchangexcphoto:
                upLoadChangeXcphoto(xcfiles);
                bt_uploadchangexcphoto.setVisibility(View.INVISIBLE);
                break;
        }

    }

    private void upLoadChangeXcphoto(final List<File> xcfiles) {
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

                MultipartBody.Builder builder=new MultipartBody.Builder()
                        .addFormDataPart("pro_id",pro_id)
                        .addFormDataPart("type","1")
                        .setType(MultipartBody.FORM);
                if (xcfiles != null){
                    for (File file : xcfiles) {
                        Log.e(TAG,"xc "+file.getName());
                        builder.addFormDataPart("photos", file.getName(), RequestBody.create(MediaType.parse("image/jpg"), file));
                    }
                }else{
                    uitoast("请添加商品宣传图片");
                    dialog.dismiss();
                    return ;
                }
                RequestBody requestBody = builder.build();
                final okhttp3.Request request = new okhttp3.Request.Builder()
                        .url("http://106.14.145.208/ShopMall/ModifyGoodXCPhoto")
                        .post(requestBody)
                        .build();

                Call call = mOkHttpClent.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("更改商品宣传图片失败");
                                dialog.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String resp = response.body().string();
                        if("ok".equals(resp)){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showToast("更改商品宣传图片成功");
                                    dialog.dismiss();
                                }
                            });
                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showToast("更改商品宣传图片失败");
                                    dialog.dismiss();
                                }
                            });
                        }

                    }
                });

            }
        }).start();

    }

    private void upLoadChangephoto(final List<File> files) {
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

                MultipartBody.Builder builder=new MultipartBody.Builder()
                        .addFormDataPart("pro_id",pro_id)
                        .addFormDataPart("type","0")
                        .setType(MultipartBody.FORM);
                if (files != null){

                    for (File file : files) {
                        Log.e(TAG,"p "+file.getName());
                        Log.e(TAG,files.size()+"");
                        builder.addFormDataPart("photos", file.getName(), RequestBody.create(MediaType.parse("image/jpg"), file));
                    }
                }else{
                    uitoast("请添加商品图片");
                    dialog.dismiss();
                    return;
                }
                RequestBody requestBody = builder.build();
                final okhttp3.Request request = new okhttp3.Request.Builder()
                        .url("http://106.14.145.208/ShopMall/ModifyGoodXCPhoto")
                        .post(requestBody)
                        .build();

                Call call = mOkHttpClent.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("更改商品图片失败");
                                dialog.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String resp = response.body().string();
                        if("ok".equals(resp)){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showToast("更改商品图片成功");
                                    dialog.dismiss();
                                }
                            });
                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showToast("更改商品图片失败");
                                    dialog.dismiss();
                                }
                            });
                        }

                    }
                });

            }
        }).start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LIST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra("result");
            mGridData = new ArrayList<GridItem>();
            files = new ArrayList<>();
            compressMore(pathList);
            for (String path : pathList) {
                Log.i("media",path);

                GridItem item = new GridItem();
                item.setImageView(path);
                // files.add(new File(path));
                mGridData.add(item);
            }

            mGridViewAdapter = new Myadapter(this, R.layout.itemtu, mGridData);
            gv_photo.setAdapter(mGridViewAdapter);
        }


        if (requestCode == REQUEST_XCLIST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra("result");
            ArrayList<GridItem> xcGridData = new ArrayList<GridItem>();
            xcfiles = new ArrayList<>();
            compressxcMore(pathList);
            for (String path : pathList) {
                Log.i("media",path);
                GridItem item = new GridItem();
                item.setImageView(path);
                // xcfiles.add(new File(path));
                xcGridData.add(item);
            }

            mGridViewAdapter = new Myadapter(this, R.layout.itemtu, xcGridData);
            gv_xcphoto.setAdapter(mGridViewAdapter);
        }

    }

    private void goselectxcphoto() {
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
        ISNav.getInstance().toListActivity(this, config, REQUEST_XCLIST_CODE);

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


    private void compressxcMore(List<String> pathList) {
        final Handler handler = new Handler();
        class  Task implements Runnable{
            String path;
            Task(String path){
                this.path = path;
            }
            @Override
            public void run() {
                Luban.get(ChangePhotoActivity.this)
                        .load(new File(path))                     //传人要压缩的图片
                        .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
                        .setCompressListener(new OnCompressListener() { //设置回调
                            @Override
                            public void onStart() {

                            }
                            @Override
                            public void onSuccess(File file) {
                                xcfiles.add(file);
                                if(!taskList.isEmpty()){
                                    Runnable runnable = taskList.pop();
                                    handler.post(runnable);
                                    if(taskList.size()==0){
                                        bt_uploadchangexcphoto.setVisibility(View.VISIBLE);
                                    }
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

    private void compressMore(List<String> pathList) {
        final Handler handler = new Handler();
        class  Task implements Runnable{
            String path;
            Task(String path){
                this.path = path;
            }
            @Override
            public void run() {
                Luban.get(ChangePhotoActivity.this)
                        .load(new File(path))                     //传人要压缩的图片
                        .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
                        .setCompressListener(new OnCompressListener() { //设置回调
                            @Override
                            public void onStart() {

                            }
                            @Override
                            public void onSuccess(File file) {
                                files.add(file);
                                if(!taskList2.isEmpty()){
                                    Runnable runnable = taskList2.pop();
                                    handler.post(runnable);
                                    Log.e(TAG,taskList2.size()+"");
                                    if(taskList2.size()==0){
                                        bt_uploadchangephoto.setVisibility(View.VISIBLE);
                                    }
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
            taskList2.add(new Task(path));
        }
        handler.post(taskList2.pop());


    }

}
