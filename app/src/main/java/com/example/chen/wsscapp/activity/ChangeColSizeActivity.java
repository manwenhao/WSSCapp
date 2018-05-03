package com.example.chen.wsscapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.BaseActivity;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

/**
 * Created by chen on 2018/4/30.
 * 修改商品颜色尺寸
 */

public class ChangeColSizeActivity extends BaseActivity implements View.OnClickListener{
    private TextView tv_shopchangecolor,tv_shopchangesize;
    private EditText et_changecolor,et_changesize;
    private Button bt_addcolor,bt_canlcecolor,bt_clearcolor,bt_surecolor;
    private Button bt_addsize,bt_canlcesize,bt_clearsize,bt_suresize;
    private Button bt_colorsizenext;
    private String pro_id,pro_color,pro_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changecolsize);
        initView();
    }

    private void initView() {
        tv_shopchangecolor = (TextView) findViewById(R.id.tv_shopchangecolor);
        tv_shopchangesize = (TextView) findViewById(R.id.tv_shopchangesize);
        et_changecolor = (EditText) findViewById(R.id.et_changecolor);
        et_changesize = (EditText) findViewById(R.id.et_changesize);
        bt_addcolor = (Button) findViewById(R.id.bt_addcolor);
        bt_canlcecolor = (Button) findViewById(R.id.bt_canlcecolor);
        bt_clearcolor = (Button) findViewById(R.id.bt_clearcolor);
        bt_surecolor = (Button) findViewById(R.id.bt_surecolor);
        bt_addsize = (Button) findViewById(R.id.bt_addsize);
        bt_canlcesize = (Button) findViewById(R.id.bt_canlcesize);
        bt_clearsize = (Button) findViewById(R.id.bt_clearsize);
        bt_colorsizenext = (Button) findViewById(R.id.bt_colorsizenext);
        bt_suresize = (Button) findViewById(R.id.bt_suresize);

        Intent intent = getIntent();
        pro_id = intent.getStringExtra("iddata");
        pro_color = intent.getStringExtra("colordata");
        pro_size = intent.getStringExtra("sizedata");

        tv_shopchangecolor.setText(pro_color);
        tv_shopchangesize.setText(pro_size);

        bt_addcolor.setOnClickListener(this);
        bt_canlcecolor.setOnClickListener(this);
        bt_clearcolor.setOnClickListener(this);
        bt_surecolor.setOnClickListener(this);
        bt_addsize.setOnClickListener(this);
        bt_canlcesize.setOnClickListener(this);
        bt_clearsize.setOnClickListener(this);
        bt_suresize.setOnClickListener(this);
        bt_colorsizenext.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_addcolor:
                AddColor();
                break;
            case R.id.bt_canlcecolor:
                CanlceColor();
                break;
            case R.id.bt_clearcolor:
                tv_shopchangecolor.setText("");
                break;
            case R.id.bt_surecolor:
                if(TextUtils.isEmpty(tv_shopchangecolor.getText().toString())){
                    showToast("请输入你需要的商品颜色");
                    return;
                }else{
                    SureColor(tv_shopchangecolor.getText().toString());
                }
                break;
            case R.id.bt_addsize:
                AddSize();
                break;
            case R.id.bt_canlcesize:
                CanlceSize();
                break;
            case R.id.bt_clearsize:
                tv_shopchangesize.setText("");
                break;
            case R.id.bt_suresize:
                if(TextUtils.isEmpty(tv_shopchangesize.getText().toString())){
                    showToast("请输入你需要的商品尺寸");
                    return;
                }else{
                    SureSize(tv_shopchangesize.getText().toString());
                }
                break;
            case R.id.bt_colorsizenext:
                Intent intent = new Intent(this, ChangePhotoActivity.class);
                intent.putExtra("proid",pro_id);
                startActivity(intent);
                break;
        }

    }

    private void SureSize(final String s) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .addParams("pro_id",pro_id)
                        .addParams("key","pro_size")
                        .addParams("value",s)
                        .url("http://106.14.145.208/ShopMall/ModifyGood")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                uitoast("更改商品尺寸失败");
                            }

                            @Override
                            public void onResponse(String response) {
                                if("ok".equals(response.toString())){
                                    uitoast("更改商品尺寸成功");
                                }else{
                                    uitoast("更改商品尺寸失败");
                                }

                            }
                        });


            }
        }).start();
    }

    private void CanlceSize() {
        String sizestr = tv_shopchangesize.getText().toString();
        String size[] = sizestr.split(",");
        String getsize = "";
        String getsize2 ;
        if(!TextUtils.isEmpty(sizestr)){
            for(int i =0 ;i<size.length-1;i++){
                getsize += size[i]+",";
            }
            if(getsize.length()==0){
                getsize2 = "";
            }else {
                getsize2 = getsize.substring(0, getsize.length() - 1);
            }
            tv_shopchangesize.setText(getsize2);
        }

    }

    private void AddSize() {
        String addsizestr = et_changesize.getText().toString();
        String sizestr = tv_shopchangesize.getText().toString();
        if(!TextUtils.isEmpty(sizestr)){
            if(TextUtils.isEmpty(addsizestr)){
                showToast("尺寸不能为空");
                return;
            }else {
                tv_shopchangesize.setText(sizestr + "," + addsizestr);
            }
        }else{
            if(TextUtils.isEmpty(addsizestr)){
                showToast("尺寸不能为空");
                return;
            }else {
            tv_shopchangesize.setText(addsizestr);
        }
        }
    }



    private void SureColor(final String s) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .addParams("pro_id",pro_id)
                        .addParams("key","pro_color")
                        .addParams("value",s)
                        .url("http://106.14.145.208/ShopMall/ModifyGood")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                uitoast("更改商品颜色失败");
                            }

                            @Override
                            public void onResponse(String response) {
                                if("ok".equals(response.toString())){
                                    uitoast("更改商品颜色成功");
                                }else{
                                    uitoast("更改商品颜色失败");
                                }

                            }
                        });

            }
        }).start();
    }

    private void CanlceColor() {
        String colorstr = tv_shopchangecolor.getText().toString();
        String color[] = colorstr.split(",");
        String getcolor = "";
        String getcolor2 ;
        if(!TextUtils.isEmpty(colorstr)){
            for(int i =0 ;i<color.length-1;i++){
                getcolor += color[i]+",";
            }
            if(getcolor.length()==0){
                getcolor2 = "";
            }else {
                getcolor2 = getcolor.substring(0, getcolor.length() - 1);
            }
            tv_shopchangecolor.setText(getcolor2);
        }
    }

    private void AddColor() {
        String addcolorstr = et_changecolor.getText().toString();
        String colorstr = tv_shopchangecolor.getText().toString();
        if(!TextUtils.isEmpty(colorstr)){
            if(TextUtils.isEmpty(addcolorstr)){
                showToast("颜色不能为空");
                return;
            }else {
                tv_shopchangecolor.setText(colorstr + "," + addcolorstr);
            }
        }else{
            if(TextUtils.isEmpty(addcolorstr)){
                showToast("颜色不能为空");
                return;
            }else{
                tv_shopchangecolor.setText(addcolorstr);
        }
        }
    }
}
