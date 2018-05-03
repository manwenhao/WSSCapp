package com.example.chen.wsscapp.Util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.wsscapp.R;

/**
 * Created by chen on 2018/5/3.
 */

public class ChooseSendDialog extends Dialog implements View.OnClickListener{
    private Context context;
    private TextView kdsend;
    private TextView bt_zqsned;
    private TextView bt_canclean;

    public ChooseSendDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_sendshop);
        kdsend = (TextView) findViewById(R.id.kdsend);
        kdsend.setOnClickListener(this);
        bt_zqsned = (TextView) findViewById(R.id.bt_zqsned);
        bt_zqsned.setOnClickListener(this);
        bt_canclean = (TextView) findViewById(R.id.bt_canclean);
        bt_canclean.setOnClickListener(this);
        /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        dialogWindow.setAttributes(p);
        this.setCancelable(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.kdsend:
                Toast.makeText(context,"快递",Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_canclean:
                dismiss();
                break;
            case R.id.bt_zqsned:
                Toast.makeText(context,"自取",Toast.LENGTH_SHORT).show();
                break;

        }

    }
}
