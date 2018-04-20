package com.example.chen.wsscapp.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.alibaba.fastjson.JSON;
import com.example.chen.wsscapp.Bean.TalentShow;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.GlideImageLoader;
import com.example.chen.wsscapp.activity.KeySearchShopActivity;
import com.example.chen.wsscapp.adapter.TalentShowAdapter;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.squareup.okhttp.Response;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    private static final String TAG = "MenuFragment";
    private View rootView;
    private DrawerLayout dl_menu;
    private Button bt_listshow;
    private EditText et_searchshop;
    private ListView ll_shoplist;
    private List<String> typedata =  null;
    private String[] imgIds1 ;
    private ArrayAdapter typeadapter;
    private List<TalentShow> menudata = new ArrayList<>();
    private XRecyclerView mRecyclerView;
    private int times = 0;
    private int refreshTime = 0;
    private List<TalentShow> listData;
    private TalentShowAdapter mAdapter;
    private List<String> images = new ArrayList<>() ;
    private Banner banner;




    public MenuFragment() {

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == rootView) {
            rootView = inflater.inflate(R.layout.fragment_menu, container,
                    false);
            initView(rootView);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (null != parent) {
            parent.removeView(rootView);
        }

        return rootView;
    }


    private void initView(View view){
        dl_menu = (DrawerLayout) view.findViewById(R.id.dl_menu);
        bt_listshow = (Button) view.findViewById(R.id.bt_listshow);
        et_searchshop = (EditText) view.findViewById(R.id.et_searchshop);
        typeadapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,getTypedata());
        ll_shoplist = (ListView) view.findViewById(R.id.ll_shoplist);
        ll_shoplist.setAdapter(typeadapter);
        et_searchshop.setCursorVisible(false);
        mRecyclerView = (XRecyclerView)view.findViewById(R.id.recyclerview);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager( 2,
                StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);


        //宣传图片的获取
        String url = "http://106.14.145.208";
        String [] img = getImgStr();
        for(int i=0;i<getImgStr().length;i++){
            images.add(url+img[i]);
        }

        View header =   LayoutInflater.from(mRecyclerView.getContext()).inflate(R.layout.recyclerview_header,mRecyclerView,false);
        banner = (Banner)header.findViewById(R.id.banner);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        mRecyclerView.addHeaderView(header);
        initShopdata();



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bt_listshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.bt_listshow:
                        dl_menu.openDrawer(GravityCompat.START);
                        break;
                    default:
                        break;
                }
            }
        });



        //商品搜索建立监听
        et_searchshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.et_searchshop:
                        et_searchshop.setCursorVisible(true);
                        String searchstr = et_searchshop.getText().toString().trim();
                        Log.e(TAG,searchstr);
                        Intent intent = new Intent(getActivity(), KeySearchShopActivity.class);
                        startActivity(intent);
                      //  getSearchShopInfo(searchstr);
                }
            }
        });


        //商品分类监听事件
        ll_shoplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getAdapter().getItem(position);
                Toast.makeText(getContext(),item,Toast.LENGTH_SHORT).show();
                getShopforType(item);

            }
        });




           mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override

            //下拉刷新宣传图
            public void onRefresh() {
                refreshTime ++;
                times = 0;
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                images.clear();
                                String url = "http://106.14.145.208";
                                String [] img = getImgStr();
                                Log.e(TAG,"?"+getImgStr().length);
                                List<String> imgstr = new ArrayList<>() ;
                                for(int i=0;i<getImgStr().length;i++){
                                    imgstr.add(url+img[i]);
                                }
                                Log.e(TAG,"2?"+imgstr.size());
                                banner.update(imgstr);
                            }
                        });
                        menudata.clear();
                        initShopdata();
                        mRecyclerView.getItemAnimator().setChangeDuration(0);
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.refreshComplete();
                    }

                }, 1000);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                mRecyclerView.setNoMore(true);
                /*if(times < 2){
                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            mRecyclerView.loadMoreComplete();
                            menudata.clear();
                            initShopdata();
                            mRecyclerView.loadMoreComplete();
                            mAdapter.notifyDataSetChanged();
                        }
                    }, 1000);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                          *//**//*  for(int i = 0; i < 9 ;i++){
                                listData.add("item" + (1 + listData.size() ) );
                            }*//**//*
                            mRecyclerView.setNoMore(true);
                           // mAdapter.notifyDataSetChanged();
                        }
                    }, 1000);
                }
                times ++;*/
            }
        });





    }


    //根据分类获取商品
    private void getShopforType(String item) {
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }

    //根据输入的关键字查询商品
    private void getSearchShopInfo(final String searchstr) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .addParams("pro_key",searchstr)
                        .url("http://106.14.145.208/ShopMall/BackAppQueryProduct")
                        .build()
                        .execute(new StringCallback() {

                            @Override
                            public void onError(com.squareup.okhttp.Request request, Exception e) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // tv_test.setText("未知错误无数据");
                                    }
                                });
                            }

                            @Override
                            public void onResponse(final String response) {
                                if("error".equals(response.toString())){
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                         //   tv_test.setText("未知错误无数据");
                                        }
                                    });
                                }else if("[]".equals(response.toString())){
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                          //  tv_test.setText("无此相关商品");
                                        }
                                    });
                                }else{
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                          //  tv_test.setText(response.toString());
                                        }
                                    });

                                }

                            }
                        });

            }
        }).start();
    }

    //同步堵塞方法返回获得商品分类
    public List<String> getTypedata() {
        final CountDownLatch latch = new CountDownLatch(1);
        typedata = new ArrayList<String> ();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = OkHttpUtils.get()
                            .url("http://106.14.145.208/ShopMall/BackAppClassify")
                            .build()
                            .execute();
                    String str = response.body().string();
                        String[] data = str.split(",");
                        for(int i =0;i<data.length;i++){
                            typedata.add(data[i]);
                            Log.e(TAG,data[i]);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                latch.countDown();

            }
        }).start();

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.e(TAG,"t2: "+typedata.isEmpty());
        return typedata;

    }

    //同步堵塞方法返回获得商品宣传轮播图
    public String[] getImgStr() {
        final CountDownLatch latch = new CountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response =OkHttpUtils.get()
                            .url("http://106.14.145.208/ShopMall/BackAppShopXCPhotos")
                            .build()
                            .execute();
                    imgIds1 = response.body().string().split(";");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                latch.countDown();
            }
        }).start();

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return imgIds1;
    }


    //回调获取商城的商品信息
    private void initShopdata() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .url("http://106.14.145.208/ShopMall/BackAppGoodsInfo")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(com.squareup.okhttp.Request request, Exception e) {

                            }

                            @Override
                            public void onResponse(String response) {
                                if("error".equals(response.toString())){
                                  getActivity().runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          Toast.makeText(getActivity(),"获取数据失败",Toast.LENGTH_SHORT).show();
                                      }
                                  });
                                }else{
                                    getJsonData(response);
                                }

                            }
                        });

            }
        }).start();

    }

    //解析json并初始化商品展示控件
    private void getJsonData(String response) {
        Log.e(TAG,response.toString());
        List<TalentShow> talentShows = JSON.parseArray(response.toString(), TalentShow.class);
        for(TalentShow t:talentShows){
            TalentShow data = new TalentShow();
            data.setPro_discount(t.getPro_discount());
            data.setPro_name(t.getPro_name());
            data.setPro_describe(t.getPro_describe());
            data.setPro_id(t.getPro_id());
            data.setPro_brand(t.getPro_brand());
            data.setPro_photo(t.getPro_photo());
            data.setPro_price(t.getPro_price());
            Log.e(TAG,t.getPro_id()+"----"+t.getPro_name());
            menudata.add(data);
        }

        mAdapter = new TalentShowAdapter(getContext(),menudata);
        mRecyclerView.setAdapter(mAdapter);



    }



}
