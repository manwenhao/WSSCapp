package com.example.chen.wsscapp.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.example.chen.wsscapp.Bean.User;
import com.example.chen.wsscapp.Util.ACache;
import com.example.chen.wsscapp.Util.MyApplication;
import com.example.chen.wsscapp.activity.BecomeShopActivity;
import com.example.chen.wsscapp.activity.SetBirthActivity;
import com.example.chen.wsscapp.activity.SetNicknameActivity;
import com.example.chen.wsscapp.activity.SetTouxiangActivity;
import com.example.chen.wsscapp.activity.SetUserInfoActivity;
import com.example.chen.wsscapp.adapter.GridViewAdapter;
import com.example.chen.wsscapp.Bean.Items;
import com.example.chen.wsscapp.R;
import com.github.nuptboyzhb.lib.SuperSwipeRefreshLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;


public class MeFragment extends Fragment {

    private View rootView;
    private String TAG = "MEfragment";
    private TextView tv_nickname, tv_phone, tv_jifen;
    private CircleImageView iv_touxiang;
    private GridView gridView,gridView2; //布局1为订单 布局2为工具
    private List<Items> itemList= new ArrayList<>();
    private List<Items> item= new ArrayList<>();
    private BaseAdapter itemAdapter;
    private Button bt_setting;
    private SuperSwipeRefreshLayout swipeRefreshLayout;
    //从缓存中取值
    ACache aCache = ACache.get(MyApplication.getContext(),"userdata");
    User userdata = new User();
    final String url = "http://106.14.145.208";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (null == rootView) {
            rootView = inflater.inflate(R.layout.fragment_me, container,
                    false);
            initView(rootView);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (null != parent) {
            parent.removeView(rootView);
        }
        return rootView;

    }


    //初始化控件以及json数据的解析
    private void initView(View view) {

        tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
        tv_jifen = (TextView) view.findViewById(R.id.tv_jifen);
        iv_touxiang = (CircleImageView) view.findViewById(R.id.iv_metouxiang);
        swipeRefreshLayout = (SuperSwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        bt_setting = (Button) view.findViewById(R.id.toolbar_right_btn);
        swipeRefreshLayout.setHeaderViewBackgroundColor(0xffcccccc);
        swipeRefreshLayout
                .setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
                    @Override
                    public void onRefresh() {
                        tv_nickname.setText(aCache.getAsString("user_name"));
                        if(TextUtils.isEmpty(userdata.getUser_touxiang())){
                            iv_touxiang.setImageDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(),R.mipmap.defalutuser));
                        }else{
                            SharedPreferences p = getActivity().getSharedPreferences("time",MODE_PRIVATE);
                            Glide.with(getContext())
                                    .load(url+userdata.getUser_touxiang())
                                    .signature(new StringSignature(p.getString("icontime","").toString()))
                                    .error(R.mipmap.defalutuser)
                                    .into(iv_touxiang);
                        }

                        //TODO 开始刷新
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }, 400);
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

        Gson gson = new Gson();
        String data = getArguments().getString("data");
        List<User> list = new ArrayList<User>();
        Type listType = new TypeToken<List<User>>() {}.getType();
        list = gson.fromJson(data, listType);

        for(User user :list){
            userdata.setUser_name(user.getUser_name());
            userdata.setUser_addr(user.getUser_addr());
            userdata.setUser_jifen(user.getUser_jifen());
            userdata.setUser_birth(user.getUser_birth());
            userdata.setUser_phone(user.getUser_phone());
            userdata.setUser_sex(user.getUser_sex());
            userdata.setUser_touxiang(user.getUser_touxiang());
        }

        //初始化控件数据

        if(TextUtils.isEmpty(userdata.getUser_touxiang())){
            iv_touxiang.setImageDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(),R.mipmap.defalutuser));
        }else{
            SharedPreferences p = getActivity().getSharedPreferences("time",MODE_PRIVATE);
            Glide.with(this)
                    .load(url+userdata.getUser_touxiang())
                    .signature(new StringSignature(p.getString("icontime","").toString()))
                    .error(R.mipmap.defalutuser)
                    .into(iv_touxiang);
        }
        Log.e(TAG,userdata.getUser_touxiang()+"");
        Log.e(TAG+" acache name",aCache.getAsString("user_name")+"");
        Log.e(TAG,userdata.getUser_name()+"");
        Log.e(TAG,userdata.getUser_jifen()+"");
        tv_nickname.setText(aCache.getAsString("user_name")+"");
        tv_jifen.setText(userdata.getUser_jifen()+"");

        IntentFilter filter = new IntentFilter(SetTouxiangActivity.action);


        getActivity().registerReceiver(broadcastReceiver, filter);

        IntentFilter f = new IntentFilter(SetNicknameActivity.action);


        getActivity().registerReceiver(broadcastReceiver1,f);


        //网格布局初始化
        init(view);

    }




    //自定义界面数据的初始化
    private void init(View view) {

        gridView = (GridView) rootView.findViewById(R.id.gridview);
        // 数据源
        initData();
        // 适配器
        itemAdapter = new GridViewAdapter(getActivity(),R.layout.item,itemList);
        // 添加控件适配器
        gridView.setAdapter(itemAdapter);
        // 添加GridView的监听事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                int index=position+1;//id是从0开始的，所以需要+1
                Toast.makeText(getActivity(), "你按下了选项："+index, Toast.LENGTH_SHORT).show();
            }
        });


        gridView2 = (GridView) rootView.findViewById(R.id.gridview2);
        // 数据源
        initDataUtil();
        // 适配器
        itemAdapter = new GridViewAdapter(getActivity(),R.layout.item,item);
        // 添加控件适配器
        gridView2.setAdapter(itemAdapter);
        // 添加GridView的监听事件
        gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                switch (position) {
                    case 0:
                        IsShop();
                        break;
                    default:
                        break;
                }

            }
        });

    }

    private void IsShop() {
        SharedPreferences pref = getActivity().getSharedPreferences("user_data",MODE_PRIVATE);
        final String idphone = pref.getString("phone","");
        Log.e(TAG,idphone);
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .addParams("mast_id",idphone)
                        .url("http://106.14.145.208/ShopMall/JudgeAppShopManager")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Looper.prepare();
                                        Toast.makeText(getActivity(), "无权限上架", Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                    }
                                }).start();
                            }

                            @Override
                            public void onResponse(String response) {
                                Log.e(TAG,response.toString());
                                if(response.toString().equals("1")){
                                    Intent intent = new Intent(getActivity(), BecomeShopActivity.class);
                                    startActivity(intent);
                                }else{
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Looper.prepare();
                                            Toast.makeText(getActivity(), "无权限上架", Toast.LENGTH_SHORT).show();
                                            Looper.loop();
                                        }
                                    }).start();

                                }

                            }
                        });

            }
        }).start();
    }




    //我的工具自定义界面
    private void initDataUtil() {
        Items item1 = new Items("商品上架",R.mipmap.becomeshop);
        item.add(item1);

    }


    //我的订单自定义界面
    private void initData() {
        Items item1 = new Items("待付款",R.mipmap.waitpay);
        itemList.add(item1);
        Items item2 = new Items("待发货",R.mipmap.waitsendshop);
        itemList.add(item2);
        Items item3 = new Items("待收货",R.mipmap.waitgetshop);
        itemList.add(item3);
        Items item4 = new Items("待评价",R.mipmap.waitwrite);
        itemList.add(item4);
        Items item5 = new Items("退款/售后",R.mipmap.payback);
        itemList.add(item5);
    }



    //界面的跳转
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bt_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.toolbar_right_btn:
                        Intent intent = new Intent(getActivity(), SetUserInfoActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });

        iv_touxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.iv_metouxiang:
                        Intent intent1 = new Intent(getActivity(), SetTouxiangActivity.class);
                        startActivity(intent1);
                        break;
                }
            }
        });



    }






    //广播刷新头像和昵称
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshIcon();
        }
        





    };
    BroadcastReceiver broadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshName();
        }



    };

    private void refreshName() {
        tv_nickname.setText(aCache.getAsString("user_name"));
    }


    private void refreshIcon() {
        if(TextUtils.isEmpty(userdata.getUser_touxiang())){
            iv_touxiang.setImageDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(),R.mipmap.defalutuser));
        }else{
            SharedPreferences p = getActivity().getSharedPreferences("time",MODE_PRIVATE);
            Glide.with(this)
                    .load(url+userdata.getUser_touxiang())
                    .signature(new StringSignature(p.getString("icontime","").toString()))
                    .error(R.mipmap.defalutuser)
                    .into(iv_touxiang);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
        getActivity().unregisterReceiver(broadcastReceiver1);
    }
}
