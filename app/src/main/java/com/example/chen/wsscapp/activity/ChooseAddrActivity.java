package com.example.chen.wsscapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.chen.wsscapp.Bean.Addr;
import com.example.chen.wsscapp.Util.BaseActivity;

import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.GetTel;
import com.example.chen.wsscapp.adapter.AddrAdapter;
import com.github.nuptboyzhb.lib.SuperSwipeRefreshLayout;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 2018/5/8.
 */
public class ChooseAddrActivity extends BaseActivity{
    private TextView tv_addnewaddr;
    private SuperSwipeRefreshLayout swipeRefreshLayout;
    private SwipeMenuListView exListView;
    private List<Addr> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseaddr);
        tv_addnewaddr = (TextView) findViewById(R.id.tv_addnewaddr);
        exListView = (SwipeMenuListView) findViewById(R.id.exListView);
        getListData();
        exListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Addr addr = list.get(position);
                //showToast(addr.getRev_name()+addr.getRev_address()+addr.getRev_phone());
                Intent getintent = getIntent();
                Intent intent = new Intent(getApplication(),DetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("rev_name",addr.getRev_name());
                intent.putExtra("rev_address",addr.getRev_address());
                intent.putExtra("rev_phone",addr.getRev_phone());
                intent.putExtra("Products", getintent.getStringExtra("Products"));
                intent.putExtra("sendType",  getintent.getStringExtra("sendType"));
                intent.putExtra("allmoney",getintent.getStringExtra("allmoney"));
                getApplication().startActivity(intent);
            }
        });
        swipeRefreshLayout = (SuperSwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setHeaderViewBackgroundColor(0xffcccccc);
        swipeRefreshLayout.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                getListData();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }

            @Override
            public void onPullDistance(int i) {

            }

            @Override
            public void onPullEnable(boolean b) {

            }
        });
        tv_addnewaddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseAddrActivity.this,AddneAddrActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getListData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .addParams("ord_phone", GetTel.gettel())
                        .url("http://106.14.145.208/ShopMall/BackAppUserCommonAddr")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                uitoast("获取常用地址失败");
                            }

                            @Override
                            public void onResponse(String response) {
                                if("error".equals(response.toString())){
                                    uitoast("获取常用地址失败");
                                }else{
                                    List<Addr> addrs = JSON.parseArray(response.toString(), Addr.class);
                                    for(Addr e :addrs){
                                        list.add(e);
                                    }
                                    AddrAdapter adapter = new AddrAdapter(getApplicationContext(),list);
                                    exListView.setAdapter(adapter);
                                }

                            }
                        });

            }
        }).start();

    }
}
