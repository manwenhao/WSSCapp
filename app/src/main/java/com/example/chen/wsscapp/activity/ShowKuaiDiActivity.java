package com.example.chen.wsscapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.chen.wsscapp.R;

/**
 * Created by chen on 2018/5/15.
 */
public class ShowKuaiDiActivity extends Activity{
    private WebView wb_showkuaidi;
    private String com;
    private String nu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showkuaidi);
        wb_showkuaidi = (WebView) findViewById(R.id.wb_showkuaidi);
        Intent intent = getIntent();
        com = intent.getStringExtra("com");
        nu = intent.getStringExtra("nu");
        wb_showkuaidi.loadUrl("https://www.kuaidi100.com/chaxun?com="+com+"&nu="+nu);
    }
}
