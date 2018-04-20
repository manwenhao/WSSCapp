package com.example.chen.wsscapp.Util;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by chen on 2017/9/13.
 */

public class BaseActivity extends Activity {
    public String text;
    public void showToast(String mtext){
        this.text = mtext;
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }


}
