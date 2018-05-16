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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.example.chen.wsscapp.Bean.JiFen;
import com.example.chen.wsscapp.Bean.User;
import com.example.chen.wsscapp.Util.ACache;
import com.example.chen.wsscapp.Util.GetTel;
import com.example.chen.wsscapp.Util.MyApplication;
import com.example.chen.wsscapp.activity.BecomeShopActivity;
import com.example.chen.wsscapp.activity.OrderActivity;
import com.example.chen.wsscapp.activity.SetBirthActivity;
import com.example.chen.wsscapp.activity.SetNicknameActivity;
import com.example.chen.wsscapp.activity.SetTouxiangActivity;
import com.example.chen.wsscapp.activity.SetUserInfoActivity;
import com.example.chen.wsscapp.activity.ShopManagerActivity;
import com.example.chen.wsscapp.activity.ShowJiFenActivity;
import com.example.chen.wsscapp.activity.TuiGuangActivity;
import com.example.chen.wsscapp.adapter.GridViewAdapter;
import com.example.chen.wsscapp.Bean.Items;
import com.example.chen.wsscapp.R;
import com.github.nuptboyzhb.lib.SuperSwipeRefreshLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.utils.L;
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
    private TextView tv_nickname, tv_phone, tv_whitejifen,tv_redjifen;
    private CircleImageView iv_touxiang;
    private GridView gridView,gridView2; //布局1为订单 布局2为工具
    private List<Items> itemList= new ArrayList<>();
    private List<Items> item= new ArrayList<>();
    private LinearLayout ll_meorder;
    private BaseAdapter itemAdapter;
    private Button bt_setting,bt_tuiguang;
    private TextView tv_tuiguang;
    private SuperSwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout ll_jifen,ll_jifen1;
    //从缓存中取值
    ACache aCache = ACache.get(MyApplication.getContext(), GetTel.gettel());
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
    private void initView(final View view) {
        ll_jifen = (LinearLayout) view.findViewById(R.id.ll_jifen);
        ll_jifen1 = (LinearLayout) view.findViewById(R.id.ll_jifen1);
        tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
        iv_touxiang = (CircleImageView) view.findViewById(R.id.iv_metouxiang);
        swipeRefreshLayout = (SuperSwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        bt_setting = (Button) view.findViewById(R.id.toolbar_right_btn);
        bt_tuiguang = (Button) view.findViewById(R.id.toolbar_left_btn);
        tv_tuiguang = (TextView) view.findViewById(R.id.toolbar_left_tv);
        tv_tuiguang.setTextColor(getResources().getColor(R.color.text_blue));

        swipeRefreshLayout.setHeaderViewBackgroundColor(0xffcccccc);
        swipeRefreshLayout
                .setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
                    @Override
                    public void onRefresh() {
                        initJifen(view);
                        tv_nickname.setText(aCache.getAsString("user_name"));
                        Log.e(TAG,"me tou "+aCache.getAsString("user_touxiang"));
                        if(TextUtils.isEmpty(aCache.getAsString("user_touxiang"))){
                            iv_touxiang.setImageResource(R.drawable.defalutuser);
                        }else{
                            Glide.with(getContext())
                                    .load(url+aCache.getAsString("user_touxiang"))
                                    .placeholder(R.drawable.defalutuser)
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
        Log.e(TAG,"icon "+userdata.getUser_touxiang());
        if(TextUtils.isEmpty(userdata.getUser_touxiang())){
            iv_touxiang.setImageResource(R.drawable.defalutuser);
        }else{
            Glide.with(getContext())
                    .load(url+userdata.getUser_touxiang())
                    .into(iv_touxiang);
        }
        Log.e(TAG,"touxiang "+userdata.getUser_touxiang()+"");
        Log.e(TAG+" acache name",aCache.getAsString("user_name")+"");
        Log.e(TAG,userdata.getUser_name()+"");
        Log.e(TAG,userdata.getUser_jifen()+"");
        tv_nickname.setText(aCache.getAsString("user_name")+"");
        initJifen(view);

        IntentFilter filter = new IntentFilter(SetTouxiangActivity.action);


        getActivity().registerReceiver(broadcastReceiver, filter);

        IntentFilter f = new IntentFilter(SetNicknameActivity.action);


        getActivity().registerReceiver(broadcastReceiver1,f);


        //网格布局初始化
        init(view);

    }

    private void initJifen(View view) {
        tv_whitejifen = (TextView) view.findViewById(R.id.tv_whitejifen);
        tv_redjifen = (TextView) view.findViewById(R.id.tv_redjifen);
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .addParams("user_phone",GetTel.gettel())
                        .url("http://106.14.145.208/ShopMall/BackAppUserJF")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(),"积分获取失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(String response) {
                                if("error".equals(response.toString())){
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getContext(),"积分获取失败", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else{
                                    List<JiFen> jiFens = JSON.parseArray(response.toString(), JiFen.class);
                                    for(final JiFen e:jiFens){
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Float whitestr = Float.valueOf(e.getWhitejf());
                                                Float redstr = Float.valueOf(e.getRedjf());
                                                String wstr = null;
                                                String rstr = null;
                                                if (whitestr / 100000 >= 1) {
                                                    wstr = GetTel.getFloat(whitestr / 10000)+"W+";
                                                } else {
                                                    wstr = e.getWhitejf();
                                                }


                                                if (redstr / 100000 >= 1) {
                                                    rstr = GetTel.getFloat(redstr / 10000)+"W+";
                                                } else {
                                                    rstr = e.getRedjf();
                                                    }

                                                tv_redjifen.setText(rstr);
                                                tv_whitejifen.setText(wstr);
                                            }
                                        });
                                    }
                                }
                            }
                        });


            }
        }).start();
    }


    //自定义界面数据的初始化
    private void init(View view) {

        ll_meorder = (LinearLayout) rootView.findViewById(R.id.ll_myorder);
        ll_meorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OrderActivity.class);
                getActivity().startActivity(intent);
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
                    case 1:
                        Toast.makeText(getContext(),"我要开店请联系******",Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(getActivity(), "无商家权限", Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                    }
                                }).start();
                            }

                            @Override
                            public void onResponse(String response) {
                                Log.e(TAG,response.toString());
                                if(response.toString().equals("1")){
                                    Intent intent = new Intent(getActivity(), ShopManagerActivity.class);
                                    startActivity(intent);
                                }else{
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Looper.prepare();
                                            Toast.makeText(getActivity(), "无商家权限", Toast.LENGTH_SHORT).show();
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
        Items item1 = new Items("商家管理",R.mipmap.becomeshop);
        item.add(item1);
        Items item2 = new Items("我要开店",R.mipmap.becomeshop);
        item.add(item2);

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

        bt_tuiguang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),TuiGuangActivity.class);
                startActivity(intent);
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

        ll_jifen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ShowJiFenActivity.class);
                startActivity(intent);
            }
        });

        ll_jifen1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ShowJiFenActivity.class);
                startActivity(intent);
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
        if(TextUtils.isEmpty(aCache.getAsString("user_touxiang"))){
            SharedPreferences tou = getActivity().getSharedPreferences("share_touxiang",MODE_PRIVATE);
           Glide.with(this)
                   .load(url+tou.getString("tou","").toString())
                   .error(R.drawable.defalutuser)
                   .into(iv_touxiang);
        }else{
            SharedPreferences p = getActivity().getSharedPreferences("time",MODE_PRIVATE);
            Glide.with(this)
                    .load(url+aCache.getAsString("user_touxiang"))
                    .signature(new StringSignature(p.getString("icontime","").toString()))
                    .error(R.drawable.defalutuser)
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
