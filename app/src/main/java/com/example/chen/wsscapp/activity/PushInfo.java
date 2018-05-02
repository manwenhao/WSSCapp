package com.example.chen.wsscapp.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.chen.wsscapp.Bean.PushMsg;
import com.example.chen.wsscapp.R;

public class PushInfo extends AppCompatActivity {
    private TextView time ,title,cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_info);
        time=(TextView)findViewById(R.id.push_time);
        title=(TextView)findViewById(R.id.push_title);
        cont=(TextView)findViewById(R.id.push_content);
        PushMsg e=(PushMsg)getIntent().getSerializableExtra("bean");
        time.setText("时间："+e.getTime());
        title.setText("主题："+e.getTitle());
        cont.setText("内容："+e.getContent());

    }

}
