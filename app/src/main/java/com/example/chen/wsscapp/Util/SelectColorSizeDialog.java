package com.example.chen.wsscapp.Util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.chen.wsscapp.R;
import com.lybeat.multiselector.BaseOption;
import com.lybeat.multiselector.MultiSelector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 2018/4/19.
 * 选择 产品分类弹框
 */

public class SelectColorSizeDialog extends Dialog {

    Activity context;

    private Button bt_addcar;

    private Button bt_shopbuy;

    private Button btReduce;

    private TextView tvNum;

    private Button btAdd;

    private MultiSelector mcolorselect,msizeselect;

    private MultiSelector.OnSelectListener mcolorListener,msizeListener;

    private View.OnClickListener addcarListener,shopbuyListener;

    private TextWatcher numListener;



    private String TAG = "SelectColorSizeDialog";




    private String[] colors;
    private String[] sizes;
    Boolean isadd = false;
    Boolean isreduce = false;

    private int value = 0;


    public SelectColorSizeDialog( Activity context) {
        super(context);
        this.context = context;
    }


    public SelectColorSizeDialog(Activity context, int themeResId,MultiSelector.OnSelectListener ccolor,MultiSelector.OnSelectListener csize,
                                 View.OnClickListener addcarListener,View.OnClickListener shopbuyListener,TextWatcher numListener,String[] colors,String[] sizes) {
        super(context);
        this.context = context;
        this.mcolorListener = ccolor;
        this.msizeListener = csize;
        this.addcarListener = addcarListener;
        this.shopbuyListener = shopbuyListener;
        this.numListener = numListener;
        this.colors = colors;
        this.sizes = sizes;

    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_selectshop);
        bt_addcar = (Button) findViewById(R.id.bt_addcar);
        bt_shopbuy = (Button) findViewById(R.id.bt_shopbuy);
        btReduce = (Button) findViewById(R.id.bt_reduce);
        tvNum = (TextView) findViewById(R.id.tv_num);
        btAdd = (Button) findViewById(R.id.bt_add);

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

        List<String> options = new ArrayList<>();
        options.add("");
        for(int i=0;i<colors.length;i++){
            options.add(colors[i]);
        }
        BaseOption baseOption = new BaseOption();
        baseOption.options(options).select(0);


        List<String> option = new ArrayList<>();
        option.add("");
        for(int i  =0;i<sizes.length;i++){
            option.add(sizes[i]);
        }
        BaseOption base = new BaseOption();
        base.options(option).select(0);






        List<BaseOption> baseOptions = new ArrayList<>();
        baseOptions.add(baseOption);
        mcolorselect = (MultiSelector) findViewById(R.id.color_selector);
        mcolorselect.addBaseOptions(baseOptions);
        mcolorselect.setOnSelectListener(mcolorListener);


        List<BaseOption> baseOptionList = new ArrayList<>();
        baseOptionList.add(base);
        msizeselect = (MultiSelector) findViewById(R.id.size_selector);
        msizeselect.addBaseOptions(baseOptionList);
        msizeselect.setOnSelectListener(msizeListener);


        bt_addcar.setOnClickListener(addcarListener);
        bt_shopbuy.setOnClickListener(shopbuyListener);

        if(isadd==false&&isreduce==false){
            tvNum.setText(String.valueOf(0));
        }
        btReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isreduce = true;
                if (value > 0) {
                    value--;
                } else {
                    value = 0;
                }
                Log.e(TAG,"reduce "+value);
                tvNum.setText(String.valueOf(value));

            }
        });
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isadd = true;
                value++;
                Log.e(TAG,"add "+value);
                tvNum.setText(String.valueOf(value));
            }

        });
        tvNum.addTextChangedListener(numListener);





        this.setCancelable(true);
    }






}
