package com.example.chen.wsscapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chen.wsscapp.Bean.WeiTaoBean;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.MyApplication;
import com.example.chen.wsscapp.activity.WeiTaoDetail;
import com.example.chen.wsscapp.adapter.WeiTaoRecycAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 2018/3/10.
 */

public class WeiTaoFragment extends Fragment {
    private XRecyclerView weitaolist;
    private WeiTaoRecycAdapter adapter;
    private List<WeiTaoBean> list;


    public WeiTaoFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_weitao, container, false);
        weitaolist=(XRecyclerView) view.findViewById(R.id.weitao_recyc);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyApplication.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        weitaolist.setLayoutManager(layoutManager);
        weitaolist.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        weitaolist.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        weitaolist.setArrowImageView(R.drawable.iconfont_downgrey);
        weitaolist
                .getDefaultRefreshHeaderView()
                .setRefreshTimeVisible(true);
        weitaolist.getDefaultFootView().setLoadingHint("加载中...");
        weitaolist.getDefaultFootView().setNoMoreHint("加载完毕...");
        adapter=new WeiTaoRecycAdapter();
        list=new ArrayList<WeiTaoBean>();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initview();
        weitaolist.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                Log.d("下拉刷新","开始刷新");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpUtils.get()
                                .url("http://106.14.145.208/ShopMall/BackAppShopDynamic")
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Request request, Exception e) {
                                        e.printStackTrace();
                                    }

                                    @Override
                                    public void onResponse(String response) {
                                        Log.d("微淘信息",response);
                                        Type type=new TypeToken<List<WeiTaoBean>>(){}.getType();
                                        list=new Gson().fromJson(response,type);
                                        Log.d("微淘信息",list.toString());
                                        adapter.setdate(list);
                                        adapter.notifyDataSetChanged();
                                        // adapter=new WeiTaoRecycAdapter(list,MyApplication.getContext());
                                        //weitaolist.setAdapter(adapter);


                                    }
                                });
                        if (weitaolist!=null)
                            weitaolist.refreshComplete();
                    }
                },2000);
            }

            @Override
            public void onLoadMore() {
                Log.d("上拉加载","开始加载");


            }
        });


    }

    public void initview(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .url("http://106.14.145.208/ShopMall/BackAppShopDynamic")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(String response) {
                                Log.d("微淘信息",response);
                                Type type=new TypeToken<List<WeiTaoBean>>(){}.getType();
                                list=new Gson().fromJson(response,type);
                                Log.d("微淘信息",list.toString());
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter=new WeiTaoRecycAdapter(list, MyApplication.getContext());
                                        adapter.setOnItemClickListener(new WeiTaoRecycAdapter.OnItemClickListener() {
                                            @Override
                                            public void onClick(int position) {
                                                Intent intent=new Intent(MyApplication.getContext(), WeiTaoDetail.class);
                                                intent.putExtra("bean",list.get(position));
                                                getActivity().startActivity(intent);
                                            }

                                            @Override
                                            public void onLongClick(int position) {

                                            }
                                        });
                                        weitaolist.setAdapter(adapter);
                                    }
                                });

                            }
                        });
            }
        }).start();
    }
}

