package com.example.chen.wsscapp.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.chen.wsscapp.Bean.ManagerOrder;
import com.example.chen.wsscapp.Bean.OrderItem;
import com.example.chen.wsscapp.Bean.ShopOrder;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.adapter.ManagerOrderAdapter;
import com.github.nuptboyzhb.lib.SuperSwipeRefreshLayout;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 2018/5/1.
 */


public class WaitManagerGetShopFragment extends Fragment {
    private static final String TAG = "WaitManagerGetShopFragment";
    private RecyclerView rv_waitget;
    private List<ShopOrder> list;
    private ManagerOrderAdapter adapter;
    private SuperSwipeRefreshLayout swipeRefreshLayout;
    private List<String> mlist;
    private int pos;

    public WaitManagerGetShopFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_waitgetshop,container,false);
        initView(view);


        return view;
    }

    private void initView(View view) {
        rv_waitget = (RecyclerView) view.findViewById(R.id.rv_waitget);
        list = new ArrayList<>();
        mlist = new ArrayList<>();
        GetData();
        swipeRefreshLayout = (SuperSwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setHeaderViewBackgroundColor(0xffcccccc);
        swipeRefreshLayout.setFooterView(createView());
        swipeRefreshLayout
                .setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
                    @Override
                    public void onRefresh() {

                        //TODO 开始刷新
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                list.clear();
                                mlist.clear();
                                OkHttpUtils.get()
                                        .addParams("que","1")
                                        .url("http://106.14.145.208/ShopMall/BackShopOrders")
                                        .build()
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onError(Request request, Exception e) {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getContext(),"获取订单失败",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onResponse(String response) {
                                                if(TextUtils.isEmpty(response.toString())){
                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(getContext(),"无订单",Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }else if("error".equals(response.toString())){
                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(getContext(),"获取订单失败",Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }else
                                                {
                                                    List<ManagerOrder> orders = JSON.parseArray(response.toString(), ManagerOrder.class);
                                                    for(ManagerOrder od :orders){
                                                        mlist.add(od.getOrd_time());
                                                        // type,  ord_id,  user_name,  user_phone,  user_touxiang,  ord_time,
                                                        //  ord_money,  ord_products,  ord_status,  ord_expressname,  ord_expressid
                                                        //   rev_name,  rev_phone, rev_address,  ord_gooid,  ord_name
                                                        //  pro_price,  pro_discount,  ord_photo, ord_size,  ord_color, String ord_num
                                                        list.add(new ShopOrder("1",od.getOrd_id(),od.getUser_name(),od.getUser_phone(),od.getUser_touxiang(),"","","","","","","","","","","","","","","","",""));
                                                        String product = od.getOrd_products();
                                                        List<OrderItem> orderItems = JSON.parseArray(product.toString(),OrderItem.class);
                                                        for(OrderItem oditem :orderItems){
                                                            list.add(new ShopOrder("2","","","","","","","","","","","","","","",oditem.getOrd_name(),oditem.getPro_price(),oditem.getPro_discount(),oditem.getOrd_photo(),oditem.getOrd_size(),oditem.getOrd_color(),oditem.getOrd_num()));
                                                        }
                                                        list.add(new ShopOrder("4","","","","","",od.getOrd_money(),"",od.getOrd_status(),"","",od.getRev_name(),od.getRev_phone(),od.getRev_address(),"","","","","","","",""));

                                                    }
                                                    pos = list.size();
                                                    adapter = new ManagerOrderAdapter(list,getContext());
                                                    rv_waitget.setLayoutManager(new LinearLayoutManager(getContext()));
                                                    rv_waitget.setAdapter(adapter);
                                                    adapter.notifyDataSetChanged();
                                                }

                                            }
                                        });
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }, 1000);
                    }

                    @Override
                    public void onPullDistance(int distance) {
                        //TODO 下拉距离
                    }

                    @Override
                    public void onPullEnable(boolean enable) {
                        //TODO 下拉过程中，下拉的距离是否足够出发刷新
                    }
                });

        swipeRefreshLayout.setOnPushLoadMoreListener(new SuperSwipeRefreshLayout.OnPushLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String lasttime = list.get(list.size()-1).getOrd_time();
                        getMoreData(lasttime);
                        swipeRefreshLayout.setLoadMore(false);
                    }
                },1000);
            }

            @Override
            public void onPushDistance(int i) {

            }

            @Override
            public void onPushEnable(boolean b) {

            }
        });

    }

    private void getMoreData(String lasttime) {
        OkHttpUtils.get()
                .addParams("que","1")
                .addParams("lastime",lasttime)
                .url("http://106.14.145.208/ShopMall/BackShopOrders")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),"获取订单失败",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(String response) {
                        if(TextUtils.isEmpty(response.toString())){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(),"无订单",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else if("error".equals(response.toString())){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(),"获取订单失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else
                        {
                            List<ManagerOrder> orders = JSON.parseArray(response.toString(), ManagerOrder.class);
                            for(ManagerOrder od :orders){
                                mlist.add(od.getOrd_time());
                                // type,  ord_id,  user_name,  user_phone,  user_touxiang,  ord_time,
                                //  ord_money,  ord_products,  ord_status,  ord_expressname,  ord_expressid
                                //   rev_name,  rev_phone, rev_address,  ord_gooid,  ord_name
                                //  pro_price,  pro_discount,  ord_photo, ord_size,  ord_color, String ord_num
                                list.add(new ShopOrder("1",od.getOrd_id(),od.getUser_name(),od.getUser_phone(),od.getUser_touxiang(),"","","","","","","","","","","","","","","","",""));
                                String product = od.getOrd_products();
                                List<OrderItem> orderItems = JSON.parseArray(product.toString(),OrderItem.class);
                                for(OrderItem oditem :orderItems){
                                    list.add(new ShopOrder("2","","","","","","","","","","","","","","",oditem.getOrd_name(),oditem.getPro_price(),oditem.getPro_discount(),oditem.getOrd_photo(),oditem.getOrd_size(),oditem.getOrd_color(),oditem.getOrd_num()));
                                }
                                list.add(new ShopOrder("3","","","","","",od.getOrd_money(),"",od.getOrd_status(),"","",od.getRev_name(),od.getRev_phone(),od.getRev_address(),"","","","","","","",""));

                            }
                            adapter = new ManagerOrderAdapter(list,getContext());
                            LinearLayoutManager manager = new LinearLayoutManager(getContext());
                            manager.scrollToPositionWithOffset(pos-1,0);
                            rv_waitget.setLayoutManager(manager);
                            rv_waitget.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            pos = list.size();
                        }

                    }
                });
    }

    private View createView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_moredata,null);
        return view;
    }

    private void GetData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .addParams("que","1")
                        .url("http://106.14.145.208/ShopMall/BackShopOrders")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(),"获取订单失败",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(String response) {
                                if(TextUtils.isEmpty(response.toString())){
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                        }
                                    });
                                }else if("error".equals(response.toString())){
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getContext(),"获取订单失败",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else
                                {
                                    List<ManagerOrder> orders = JSON.parseArray(response.toString(), ManagerOrder.class);

                                    for(ManagerOrder od :orders){
                                        mlist.add(od.getOrd_time());
                                        // type,  ord_id,  user_name,  user_phone,  user_touxiang,  ord_time,
                                        //  ord_money,  ord_products,  ord_status,  ord_expressname,  ord_expressid
                                        //   rev_name,  rev_phone, rev_address,  ord_gooid,  ord_name
                                        //  pro_price,  pro_discount,  ord_photo, ord_size,  ord_color, String ord_num
                                        list.add(new ShopOrder("1",od.getOrd_id(),od.getUser_name(),od.getUser_phone(),od.getUser_touxiang(),"","","","","","","","","","","","","","","","",""));
                                        String product = od.getOrd_products();
                                        List<OrderItem> orderItems = JSON.parseArray(product.toString(),OrderItem.class);
                                        for(OrderItem oditem :orderItems){

                                            list.add(new ShopOrder("2","","","","","","","","","","","","","","",oditem.getOrd_name(),oditem.getPro_price(),oditem.getPro_discount(),oditem.getOrd_photo(),oditem.getOrd_size(),oditem.getOrd_color(),oditem.getOrd_num()));
                                        }
                                        list.add(new ShopOrder("4","","","","","",od.getOrd_money(),"",od.getOrd_status(),"","",od.getRev_name(),od.getRev_phone(),od.getRev_address(),"","","","","","","",""));
                                    }
                                    adapter = new ManagerOrderAdapter(list,getContext());
                                    pos = list.size();
                                    rv_waitget.setLayoutManager(new LinearLayoutManager(getContext()));
                                    rv_waitget.setAdapter(adapter);
                                }

                            }
                        });

            }
        }).start();

    }
}



