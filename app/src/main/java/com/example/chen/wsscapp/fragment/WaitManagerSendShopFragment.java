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

public class WaitManagerSendShopFragment extends Fragment {
    private static final String TAG = "WaitSendShopFragment";
    private RecyclerView rv_waitsend;
    private List<ShopOrder> list;
    private ManagerOrderAdapter adapter;
    private SuperSwipeRefreshLayout swipeRefreshLayout;

    public WaitManagerSendShopFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_waitsendshop,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        rv_waitsend = (RecyclerView) view.findViewById(R.id.rv_waitsend);
        list = new ArrayList<>();
        GetData();
        swipeRefreshLayout = (SuperSwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setHeaderViewBackgroundColor(0xffcccccc);
        swipeRefreshLayout
                .setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
                    @Override
                    public void onRefresh() {

                        //TODO 开始刷新
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                list.clear();
                                GetData();
                                adapter.notifyDataSetChanged();
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


    }

    private void GetData() {
        OkHttpUtils.get()
                .addParams("que","0")
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
                            Log.e(TAG,"??"+orders.size());
                            Log.e(TAG,response);
                            for(ManagerOrder od :orders){
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
                            rv_waitsend.setLayoutManager(new LinearLayoutManager(getContext()));
                            rv_waitsend.setAdapter(adapter);
                        }

                    }
                });
    }


}


