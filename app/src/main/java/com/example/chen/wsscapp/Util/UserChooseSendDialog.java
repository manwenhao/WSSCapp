package com.example.chen.wsscapp.Util;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.activity.ChooseAddrActivity;
import com.example.chen.wsscapp.activity.DetailActivity;

/**
 * Created by chen on 2018/5/6.
 */

public class UserChooseSendDialog extends Dialog implements View.OnClickListener{
    private static final String TAG = "UserChooseSendDialog";
    private Context context;
    private String Products;
    private TextView tv_kdsend;
    private TextView tv_zqsned;
    private String allPrice;

    public UserChooseSendDialog(Context context,String Products,String allPrice) {
        super(context);
        this.context = context;
        this.Products = Products;
        this.allPrice = allPrice;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_userchoosesend);
        tv_kdsend = (TextView) findViewById(R.id.tv_kdsend);
        tv_zqsned = (TextView) findViewById(R.id.tv_zqsned);
        tv_kdsend.setOnClickListener(this);
        tv_zqsned.setOnClickListener(this);

        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        dialogWindow.setAttributes(p);
        this.setCancelable(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_kdsend:
                Intent intent0 = new Intent(getContext(),ChooseAddrActivity.class);
                intent0.putExtra("Products",Products);
                intent0.putExtra("sendType","0");
                intent0.putExtra("allmoney",allPrice);
                getContext().startActivity(intent0);
                break;
            case R.id.tv_zqsned:
                Intent intent = new Intent(getContext(),DetailActivity.class);
                intent.putExtra("Products",Products);
                intent.putExtra("sendType","8");
                intent.putExtra("allmoney",allPrice);
                getContext().startActivity(intent);
                //Log.e(TAG,Products+allPrice);
                break;
        }

    }
}
