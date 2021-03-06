package com.example.chen.wsscapp.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chen.wsscapp.Bean.Product;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.BaseActivity;
import com.example.chen.wsscapp.Util.TopUi;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

/**
 * Created by chen on 2018/5/15.
 */

public class BecomeColorSizeActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "BecomeColorSizeActivity";
    private TextView tv_shopchangecolor,tv_shopchangesize;
    private EditText et_changecolor,et_changesize;
    private Button bt_addcolor,bt_canlcecolor,bt_clearcolor;
    private Button bt_addsize,bt_canlcesize,bt_clearsize;
    private Button bt_colorsizenext;
    private String pro_color,pro_size;
    private Product product;

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
        setContentView(R.layout.activity_becomecolorsize);
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
        bt_addsize = (Button) findViewById(R.id.bt_addsize);
        bt_canlcesize = (Button) findViewById(R.id.bt_canlcesize);
        bt_clearsize = (Button) findViewById(R.id.bt_clearsize);
        bt_colorsizenext = (Button) findViewById(R.id.bt_colorsizenext);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        product = (Product) extras.getSerializable("data");
        Log.e(TAG,product.toString());


        bt_addcolor.setOnClickListener(this);
        bt_canlcecolor.setOnClickListener(this);
        bt_clearcolor.setOnClickListener(this);
        bt_addsize.setOnClickListener(this);
        bt_canlcesize.setOnClickListener(this);
        bt_clearsize.setOnClickListener(this);
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
            case R.id.bt_addsize:
                AddSize();
                break;
            case R.id.bt_canlcesize:
                CanlceSize();
                break;
            case R.id.bt_clearsize:
                tv_shopchangesize.setText("");
                break;
            case R.id.bt_colorsizenext:
                if(TextUtils.isEmpty(tv_shopchangecolor.getText().toString())){
                    showToast("颜色不能为空");
                    return;
                }
                if(TextUtils.isEmpty(tv_shopchangesize.getText().toString())){
                    showToast("尺寸不能为空");
                    return;
                }
                Intent intent1 = new Intent(this, BecomephotoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data",product);
                bundle.putString("color",tv_shopchangecolor.getText().toString());
                bundle.putString("size",tv_shopchangesize.getText().toString());
                Log.e(TAG,"1"+product.toString());
                intent1.putExtras(bundle);
                startActivity(intent1);
                break;
        }
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
