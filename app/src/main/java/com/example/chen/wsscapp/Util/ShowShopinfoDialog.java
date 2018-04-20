package com.example.chen.wsscapp.Util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.chen.wsscapp.Bean.GetProduct;
import com.example.chen.wsscapp.R;

/**
 * Created by chen on 2018/4/19.
 * 展示商品参数信息
 */

public class ShowShopinfoDialog extends Dialog {
    Activity context;
    GetProduct getProduct;
    private TextView tv_pro_brand;
    private TextView tv_pro_type;
    private TextView tv_pro_people;
    private TextView tv_pro_make;
    private TextView tv_pro_size;
    private TextView tv_pro_color;
    private TextView tv_pro_desinfo;

    public ShowShopinfoDialog(Activity context) {
        super(context);
        this.context = context;

    }

    public ShowShopinfoDialog(Activity context, int themeResId, GetProduct getProduct){
        super(context);
        this.context = context;
        this.getProduct = getProduct;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_shopcanshu);
        /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = this.getWindow();
        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        // p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);
        tv_pro_brand = (TextView) findViewById(R.id.tv_pro_brand);
        tv_pro_brand.setText(getProduct.getPro_brand());
        tv_pro_type = (TextView) findViewById(R.id.tv_pro_type);
        tv_pro_type.setText(getProduct.getPro_classify());
        tv_pro_people = (TextView) findViewById(R.id.tv_pro_people);
        tv_pro_people.setText(getProduct.getPro_suitperson());
        tv_pro_make = (TextView) findViewById(R.id.tv_pro_make);
        tv_pro_make.setText(getProduct.getPro_material());
        tv_pro_size = (TextView) findViewById(R.id.tv_pro_size);
        tv_pro_size.setText(getProduct.getPro_size());
        tv_pro_color = (TextView) findViewById(R.id.tv_pro_color);
        tv_pro_color.setText(getProduct.getPro_color());
        tv_pro_desinfo = (TextView) findViewById(R.id.tv_pro_desinfo);
        tv_pro_desinfo.setText(getProduct.getPro_describe());
        this.setCancelable(true);
    }
}
