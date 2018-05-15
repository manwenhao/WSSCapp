package com.example.chen.wsscapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.tu.loadingdialog.LoadingDailog;
import com.bumptech.glide.Glide;
import com.example.chen.wsscapp.Bean.GridItem;
import com.example.chen.wsscapp.Bean.Product;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.BaseActivity;
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
 * Created by chen on 2018/4/7.
 * 商品上架的图片压缩上传
 */

public class BecomephotoActivity extends BaseActivity implements View.OnClickListener {
    private String TAG = "BecomephotoActivity";
    private Button bt_photo,bt_xcphoto,bt_uploadinfo;
    private ImageView immmgg;
    private GridView gridView,xcgridView;
    private ArrayList<GridItem> mGridData = null;
    private Myadapter mGridViewAdapter = null;
    private static final int REQUEST_LIST_CODE = 0;
    private static final int REQUEST_XCLIST_CODE = 1;
    private  Product product ;
    private List<File> files = null,xcfiles = null;
    private LoadingDailog dialog = null;
    private LinkedList<Runnable> taskList = new LinkedList<>();
    private LinkedList<Runnable> taskList2 = new LinkedList<>();
    private String color;
    private String size;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_becomephoto);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        product = (Product) extras.getSerializable("data");
        color = extras.getString("color");
        size = extras.getString("size");

        Log.e(TAG,product.toString());
        initView();
    }

    private void initView() {
        bt_photo = (Button) findViewById(R.id.bt_photo);
        bt_xcphoto = (Button) findViewById(R.id.bt_xcphoto);
        gridView = (GridView) findViewById(R.id.gv_photo);
        xcgridView = (GridView) findViewById(R.id.gv_xcphoto);
        bt_uploadinfo = (Button) findViewById(R.id.bt_uploadinfo);
        bt_xcphoto.setOnClickListener(this);
        bt_photo.setOnClickListener(this);
        bt_uploadinfo.setOnClickListener(this);
        bt_uploadinfo.setVisibility(View.INVISIBLE);

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
            case R.id.bt_photo:
                goselectphoto();
                break;
            case R.id.bt_xcphoto:
                goselectxcphoto();
                break;
            case R.id.bt_uploadinfo:
                Log.e(TAG,product.toString());
                uploadShopInfo(product,files,xcfiles);
                break;

        }

    }

    private void uploadShopInfo(final Product product, final List<File> files,final List<File> xcfiles) {
        Log.e(TAG,color+size+product.toString());
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
                        .addFormDataPart("pro_name",product.getPro_name())
                        .addFormDataPart("pro_classify",product.getPro_classify())
                        .addFormDataPart("pro_suitperson",product.getPro_suitperson())
                        .addFormDataPart("pro_material",product.getPro_material())
                        .addFormDataPart("pro_brand",product.getPro_brand())
                        .addFormDataPart("pro_size",size)
                        .addFormDataPart("pro_color",color)
                        .addFormDataPart("pro_price", String.valueOf(product.getPro_price()))
                        .addFormDataPart("pro_discount",product.getPro_discount())
                        .addFormDataPart("pro_describephoto",product.getPro_describe())
                        .addFormDataPart("pro_jfvalue",product.getPro_jfvalue())
                        .setType(MultipartBody.FORM);
                if (files != null){
                    for (File file : files) {
                        builder.addFormDataPart("upload", file.getName(), RequestBody.create(MediaType.parse("image/jpg"), file));
                    }
                }
                if (xcfiles != null){
                    for (File file : xcfiles) {
                        builder.addFormDataPart("imgxc", file.getName(), RequestBody.create(MediaType.parse("image/jpg"), file));
                    }
                }

                RequestBody requestBody = builder.build();
                final okhttp3.Request request = new okhttp3.Request.Builder()
                        .url("http://106.14.145.208/ShopMall/UploadForGoodCreate")
                        .post(requestBody)
                        .build();

                Call call = mOkHttpClent.newCall(request);
                call.enqueue(new Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG,e.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BecomephotoActivity.this,"上传超时失败",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String resp = response.body().string();
                        if(resp.equals("ok")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(BecomephotoActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });

                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(BecomephotoActivity.this,"上传失败",Toast.LENGTH_SHORT).show();
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


        if (requestCode == REQUEST_XCLIST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra("result");
            ArrayList<GridItem> xcGridData = new ArrayList<GridItem>();
            xcfiles = new ArrayList<>();
            compressxcMore(pathList);
            for (String path : pathList) {
                Log.e(TAG,pathList.size()+"");
                Log.i("media",path);
                GridItem item = new GridItem();
                item.setImageView(path);
               // xcfiles.add(new File(path));
                xcGridData.add(item);
            }

            Log.e(TAG,"grid "+xcGridData.size());
            mGridViewAdapter = new Myadapter(this, R.layout.itemtu, xcGridData);
            xcgridView.setAdapter(mGridViewAdapter);
        }

    }

    //代码写得太冗余，不想修改了
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
                Luban.get(BecomephotoActivity.this)
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
                                        bt_uploadinfo.setVisibility(View.VISIBLE);
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
                Luban.get(BecomephotoActivity.this)
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
                                    if(taskList2.size()==0){
                                        bt_uploadinfo.setVisibility(View.INVISIBLE);
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
