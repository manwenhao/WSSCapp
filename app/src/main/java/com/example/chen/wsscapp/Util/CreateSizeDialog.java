package com.example.chen.wsscapp.Util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.chen.wsscapp.R;
import com.lybeat.multiselector.BaseOption;
import com.lybeat.multiselector.MultiSelector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 2018/4/8.
 */

public class CreateSizeDialog extends Dialog {

    Activity context;

    private Button bt_save;

    private MultiSelector multiSelector;

    private MultiSelector.OnSelectListener mClickListener;


    public CreateSizeDialog(Activity context) {
        super(context);
        this.context = context;
    }

    public CreateSizeDialog(Activity context, int themeResId,MultiSelector.OnSelectListener clickListener) {
        super(context, themeResId);
        this.context = context;
        this.mClickListener = clickListener;

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.simple_size_select);




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
        options.add("均码");
        BaseOption baseOption = new BaseOption();
        baseOption.options(options);

        List<String> options1 = new ArrayList<>();
        options1.add("");
        options1.add("小码");
        BaseOption baseOption1 = new BaseOption();
        baseOption1.options(options1);

        List<String> options2 = new ArrayList<>();
        options2.add("");
        options2.add("中码");
        BaseOption baseOption2 = new BaseOption();
        baseOption2.options(options2);

        List<String> options3 = new ArrayList<>();
        options3.add("");
        options3.add("大码");
        BaseOption baseOption3 = new BaseOption();
        baseOption3.options(options3);


        List<BaseOption> baseOptions = new ArrayList<>();
        baseOptions.add(baseOption);
        baseOptions.add(baseOption1);
        baseOptions.add(baseOption2);
        baseOptions.add(baseOption3);
        multiSelector = (MultiSelector) findViewById(R.id.size_selector);
        multiSelector.addBaseOptions(baseOptions);
        multiSelector.setOnSelectListener(mClickListener);



        this.setCancelable(true);
    }

}
