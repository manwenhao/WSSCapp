package com.example.chen.wsscapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chen.wsscapp.Bean.WeiTaoBean;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.MyApplication;
import com.example.chen.wsscapp.adapter.WTGridViewAdapter;
import com.example.chen.wsscapp.adapter.WTGridviewPhotoviewAdapter;
import com.github.chrisbanes.photoview.PhotoView;

import de.hdodenhof.circleimageview.CircleImageView;

public class WeiTaoDetail extends AppCompatActivity {
    private CircleImageView imageView;
    private TextView name,time,content;
    private GridView gridView;
    private PhotoView photoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wei_tao_detail);
        imageView=(CircleImageView)findViewById(R.id.item_touxiang);
        name=(TextView)findViewById(R.id.item_name);
        time=(TextView)findViewById(R.id.item_time);
        content=(TextView)findViewById(R.id.words_cont);
        gridView=(GridView)findViewById(R.id.wt_detail_gridView);
        photoView=(PhotoView) findViewById(R.id.wt_item_detail_image);
        WeiTaoBean e=(WeiTaoBean) getIntent().getSerializableExtra("bean");
        if (TextUtils.isEmpty(e.getMster_touxiang())){
            Glide.with(MyApplication.getContext())
                    .load(R.drawable.imgerror)
                    .into(imageView);
        }else {
            Glide.with(MyApplication.getContext())
                    .load("http://106.14.145.208:80"+e.getMster_touxiang())
                    .into(imageView);
        }
        if (TextUtils.isEmpty(e.getMster_name())){
            name.setText("暂无姓名");
        }else {
            name.setText(e.getMster_name());
        }
        if (TextUtils.isEmpty(e.getUptime())){
            time.setText("暂无时间");
        }else {
           time.setText(e.getUptime());
        }
        if (TextUtils.isEmpty(e.getDyn_content())){
            content.setText("暂无内容");
        }else {
            content.setText(e.getDyn_content());
        }
        if (!TextUtils.isEmpty(e.getDyn_photots())){
           final String a[]=e.getDyn_photots().split(";");
            for (int ii = 0; ii < a.length; ii++) {
                String http = "http://106.14.145.208:80";
                a[ii] = http + a[ii];
            }

            WTGridViewAdapter adapter=new WTGridViewAdapter(a,MyApplication.getContext());
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    photoView.setVisibility(View.VISIBLE);
                    Glide.with(MyApplication.getContext()).load(a[i]).into(photoView);

                }
            });
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (photoView.getVisibility()==View.VISIBLE){
                        photoView.setVisibility(View.GONE);
                    }
                }
            });
        }

    }
}
