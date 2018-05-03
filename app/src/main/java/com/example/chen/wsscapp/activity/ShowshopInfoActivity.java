package com.example.chen.wsscapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.chen.wsscapp.Bean.GetProduct;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.BaseActivity;
import com.example.chen.wsscapp.Util.GetTel;
import com.example.chen.wsscapp.Util.GlideImageLoader;
import com.example.chen.wsscapp.Util.MyApplication;
import com.example.chen.wsscapp.Util.SelectColorSizeDialog;
import com.example.chen.wsscapp.Util.ShowShopinfoDialog;
import com.github.chrisbanes.photoview.PhotoView;
import com.hankkin.library.GradationScrollView;
import com.hankkin.library.MyImageLoader;
import com.hankkin.library.NoScrollListView;
import com.hankkin.library.ScrollViewContainer;
import com.hankkin.library.StatusBarUtil;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.lybeat.multiselector.MultiSelector;
import com.squareup.okhttp.Request;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by chen on 2018/4/18.
 * 点击商品展示详细页
 */

public class ShowshopInfoActivity extends BaseActivity implements View.OnClickListener,GradationScrollView.ScrollViewListener {

    private GradationScrollView scrollView;  //滑动布局
    private  RelativeLayout llTitle;   //顶端布局
    private  ImageView iv_good_detai_back;  //返回图片
    private  LinearLayout llOffset;    //商品信息布局
    private ScrollViewContainer container;
    private  TextView tvGoodTitle;   //顶端商品详情字
    private NoScrollListView nlvImgs;//图片详情

    private Banner banner;  //轮播图
    private  TextView tv_addcar;  //加入购物车
    private  TextView tv_shopbuy;  //立即购买
    private  TextView tv_itemshopname;  //商品品牌名称
    private  TextView tv_itemshopprice;  //商品现价
    private  TextView tv_itemshopoldprice; //商品原价
    private  TextView tv_shoptypecolor;  //商品分类
    private  TextView tv_shopcanshu;  //商品介绍参数
    private ImageView iv_chat;
    private SelectColorSizeDialog selectColorSizeDialog;
    private ShowShopinfoDialog showShopinfoDialog;


    private QuickAdapter<String> imgAdapter;
    private List<String> imgsUrl;
    private List<String> imgs;
    private String sbcolor,sbsize;


    private int height;
    private int width;

    private String TAG = "ShowshopInfoActivity";
    Boolean color = false;   //判断标志  是否选择颜色尺寸
    Boolean size = false;
    private String value ; //购物数量

    private LinearLayout ll_good_detail_bottom;

    private PhotoView photoView,photoView2;
    private FrameLayout parent,parent2;
    private ImageView mbg,mbg2;
    AlphaAnimation in = new AlphaAnimation(0, 1);
    AlphaAnimation out = new AlphaAnimation(1, 0);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showshop);
        initView();
        getShopInfo();
       // initListeners();
    }


    //初始化控件
    private void initView() {
        container = new ScrollViewContainer(getApplicationContext());
        scrollView = (GradationScrollView)findViewById(R.id.scrollview);
        llTitle = (RelativeLayout)findViewById(R.id.ll_good_detail);
        llOffset = (LinearLayout)findViewById(R.id.ll_offset);
        container = (ScrollViewContainer)findViewById(R.id.sv_container);
        tvGoodTitle = (TextView)findViewById(R.id.tv_good_detail_title_good);
        nlvImgs = (NoScrollListView)findViewById(R.id.nlv_good_detial_imgs);
        iv_good_detai_back = (ImageView) findViewById(R.id.iv_good_detai_back);
        tv_addcar = (TextView) findViewById(R.id.tv_addshopcar);
        tv_shopbuy = (TextView) findViewById(R.id.tv_shopbuy);
        tv_itemshopname = (TextView) findViewById(R.id.tv_itemshopname);
        tv_itemshopprice = (TextView) findViewById(R.id.tv_itemshopprice);
        tv_itemshopoldprice = (TextView) findViewById(R.id.tv_itemshopoldprice);
        tv_shoptypecolor = (TextView) findViewById(R.id.tv_shoptypecolor);
        tv_shopcanshu = (TextView) findViewById(R.id.tv_shopcanshu);
        iv_chat = (ImageView) findViewById(R.id.iv_chat);
        ll_good_detail_bottom = (LinearLayout) findViewById(R.id.ll_good_detail_bottom);
       // selectColorSizeDialog = new SelectColorSizeDialog(this,R.style.Dialog_Common,colorSelect,sizeSelect,addcar,shopbuy);
        banner = (Banner) findViewById(R.id.banner);
        //透明状态栏
        StatusBarUtil.setTranslucentForImageView(this,llOffset);
        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) llOffset.getLayoutParams();
        params1.setMargins(0,-StatusBarUtil.getStatusBarHeight(this)/4,0,0);
        llOffset.setLayoutParams(params1);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) banner.getLayoutParams();
        params.height = getScreenHeight(this)*2/3;
        banner.setLayoutParams(params);
        banner.setImageLoader(new GlideImageLoader());


        photoView = (PhotoView) findViewById(R.id.img);
        photoView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        parent = (FrameLayout) findViewById(R.id.parent);
        mbg = (ImageView) findViewById(R.id.bg);

        photoView2 = (PhotoView) findViewById(R.id.img2);
        photoView2.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        parent2 = (FrameLayout) findViewById(R.id.parent2);
        mbg2 = (ImageView) findViewById(R.id.bg2);

        in.setDuration(300);
        out.setDuration(300);
        out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mbg2.setVisibility(View.INVISIBLE);
                mbg.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });









        iv_chat.setOnClickListener(this);
        iv_good_detai_back.setOnClickListener(this);
        tv_addcar.setOnClickListener(this);
        tv_shopbuy.setOnClickListener(this);
        tv_shoptypecolor.setOnClickListener(this);
        tv_shopcanshu.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_good_detai_back:
                finish();
                break;
            case R.id.tv_addshopcar:
                showSelectShop();
                break;
            case R.id.tv_shopbuy:
                showSelectShop();
                break;
            case R.id.tv_shoptypecolor:
                showSelectShop();
                //showToast("选择颜色尺寸数量");
                break;
            case R.id.tv_shopcanshu:
                showShopInfo();
                //showToast("商品参数");
                break;
            case R.id.iv_chat:
                showToast("跳转商家聊天");

                break;
        }

    }

    private void showShopInfo() {
        showShopinfoDialog.show();
    }


    //颜色选择
    private MultiSelector.OnSelectListener  colorSelect = new MultiSelector.OnSelectListener() {
        @Override
        public void onSelect(List<String> choices) {
            color = true;
            for (String choice : choices) {
                    sbcolor = choice;
            }
            Log.e(TAG,sbcolor);

        }
    } ;


    //尺寸选择
    private MultiSelector.OnSelectListener  sizeSelect = new MultiSelector.OnSelectListener() {
        @Override
        public void onSelect(List<String> choices) {
            size = true;
            for (String choice : choices) {
                    sbsize = choice;
            }
            Log.e(TAG,sbsize);

        }
    } ;



    //Dialog的商品数量监听
    private TextWatcher nums = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
           // Log.e(TAG,"on "+s.toString());

        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.e(TAG,"after "+s.toString());
            value = s.toString();

        }
    };



    //Dialog的加入购物车监听
    private View.OnClickListener addcar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(color==false||"".equals(sbcolor.toString())||sbcolor.toString()==null){
                showToast("请选择商品颜色");
                return;
            }
            if(size==false||"".equals(sbsize.toString())||sbsize.toString()==null){
                showToast("请选择商品尺寸");
                return;
            }
            if("0".equals(value)||value==null){
                showToast("请选择商品数量");
                return;
            }
            addCarHttp(sbcolor,sbsize,value);


           // showToast(sbcolor.toString()+sbsize.toString()+value);
        }
    };

    private void addCarHttp(final String sbcolor, final String sbsize, final String value) {
        Intent intent = getIntent();
        final String pro_id =  intent.getStringExtra("shopid");
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .addParams("pro_id", GetTel.gettel())
                        .addParams("good_id",pro_id)
                        .addParams("good_num",value)
                        .addParams("good_size",sbsize)
                        .addParams("good_color",sbcolor)
                        .url("http://106.14.145.208/ShopMall/UploadUserCart")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                toast("网络连接失败");
                            }

                            @Override
                            public void onResponse(String response) {
                                if("ok".equals(response.toString())){
                                    toast("加入购物车成功");
                                }else{
                                    toast("加入购物车失败");
                                }

                            }
                        });

            }
        }).start();
    }


    //Dialog的立即购买监听
    private View.OnClickListener shopbuy = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(color==false||"".equals(sbcolor.toString())||sbcolor.toString()==null){
                showToast("请选择商品颜色");
                return;
            }
            if(size==false||"".equals(sbsize.toString())||sbsize.toString()==null){
                showToast("请选择商品尺寸");
                return;
            }
            if("0".equals(value)||value==null){
                showToast("请选择商品数量");
                return;
            }

            showToast(sbcolor.toString()+sbsize.toString()+value+"付款去了拉拉拉拉");
        }
    };






    private void showSelectShop() {
        selectColorSizeDialog.show();
    }

    public  int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }


    private void initListeners() {
        ViewTreeObserver vto = banner.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                llTitle.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                height = banner.getHeight();
                scrollView.setScrollViewListener(ShowshopInfoActivity.this);
            }
        });


    }

    /**
     * 滑动监听
     * @param scrollView
     * @param x
     * @param y
     * @param oldx
     * @param oldy
     */
    @Override
    public void onScrollChanged(GradationScrollView scrollView, int x, int y,
                                int oldx, int oldy) {
        // TODO Auto-generated method stub
        if (y <= 0) {   //设置标题的背景颜色
            llTitle.setBackgroundColor(Color.argb((int) 0, 255,255,255));
        } else if (y > 0 && y <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) y / height;
            float alpha = (255 * scale);
            tvGoodTitle.setTextColor(Color.argb((int) alpha, 1,24,28));
            llTitle.setBackgroundColor(Color.argb((int) alpha, 255,255,255));
        } else {    //滑动到banner下面设置普通颜色
            llTitle.setBackgroundColor(Color.argb((int) 255, 255,255,255));
        }
    }


    //获取item点击得到的json数据
    public void getShopInfo() {
        Intent intent = getIntent();
        final String pro_id =  intent.getStringExtra("shopid");
        Log.e(TAG,pro_id);
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .url("http://106.14.145.208/ShopMall/BackAppGoodDetails")
                        .addParams("pro_id",pro_id)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                toast("网络加载失败");

                            }

                            @Override
                            public void onResponse(String response) {
                                if("error".equals(response.toString())){
                                    toast("获取数据失败");
                                }else{
                                    Log.e(TAG,response.toString());
                                    JsontoUI(response.toString());
                                }

                            }
                        });
            }
        }).start();


    }

    private void JsontoUI(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<GetProduct> data  = JSON.parseArray(s, GetProduct.class);
                for(GetProduct getProduct : data){
                    float price = Float.parseFloat(getProduct.getPro_price())*Float.parseFloat(getProduct.getPro_discount());
                    tv_itemshopname.setText("["+getProduct.getPro_brand()+"]"+"       "+getProduct.getPro_name());
                    tv_itemshopprice.setText("￥"+price);
                    tv_itemshopoldprice.setText("         原价：￥"+getProduct.getPro_price());
                    getPhotos(getProduct.getPro_photo());
                    getDesPhotos(getProduct.getPro_describephoto());
                    Log.e(TAG,getProduct.getPro_color());
                    Log.e(TAG,getProduct.getPro_size());
                    getshopColorSize(getProduct.getPro_color(),getProduct.getPro_size());
                    getshopCanshu(getProduct);
                }

            }
        });

    }

    private void getshopCanshu(GetProduct getProduct) {
        showShopinfoDialog = new ShowShopinfoDialog(this, R.style.Dialog_Common,getProduct);
    }


    //显示尺寸与颜色
    private void getshopColorSize(String pro_color,String pro_size) {
        String [] colors = pro_color.split(",");
        String [] sizes = pro_size.split(",");
        selectColorSizeDialog = new SelectColorSizeDialog(this, R.style.Dialog_Common,colorSelect,sizeSelect,addcar,shopbuy,nums,colors,sizes);
    }


    //介绍图片设置数据以及图片的点击放大
    private void getDesPhotos(final String pro_describephoto) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String[] split = pro_describephoto.split(";");
                imgsUrl = new ArrayList<>();
                String url ="http://106.14.145.208";
                for(int i=0;i<split.length;i++){
                    imgsUrl.add(url+split[i]);
                }
                width = getScreenWidth(getApplicationContext());
                imgAdapter = new QuickAdapter<String>(ShowshopInfoActivity.this, R.layout.adapter_good_detail_imgs) {
                    @Override
                    protected void convert(final BaseAdapterHelper helper, String item) {
                        ImageView iv1 = helper.getView(R.id.iv_adapter_good_detail_img);
                        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv1.getLayoutParams();
                        params.width = width;
                        params.height = width/2;
                        iv1.setLayoutParams(params);
                        helper.setOnClickListener(iv1.getId(), new View.OnClickListener() {
                            int pos = helper.getPosition();
                            @Override
                            public void onClick(View v) {
                                mbg.startAnimation(in);
                                mbg.setVisibility(View.VISIBLE);
                                parent.setVisibility(View.VISIBLE);
                                photoView.setVisibility(View.VISIBLE);
                               // ll_good_detail_bottom.setVisibility(View.GONE);
                                Glide.with(MyApplication.getContext()).load(imgsUrl.get(pos)).into(photoView);
                                //Toast.makeText(ShowshopInfoActivity.this,"点击"+imgsUrl.get(pos),Toast.LENGTH_SHORT).show();
                            }
                        });
                        MyImageLoader.getInstance().displayImageCen(getApplicationContext(),item,iv1,width,width/2);
                    }
                };

                photoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (photoView.getVisibility()==View.VISIBLE){
                            photoView.setVisibility(View.GONE);
                            mbg.setVisibility(View.GONE);
                            parent.setVisibility(View.GONE);
                          //  ll_good_detail_bottom.setVisibility(View.VISIBLE);
                        }
                    }
                });

                imgAdapter.addAll(imgsUrl);
                nlvImgs.setAdapter(imgAdapter);
            }
        });

    }


    //轮播图设置数据以及图片的点击放大
    private void getPhotos(final String photo) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String[] split = photo.split(";");
                imgs = new ArrayList<>();
                String url ="http://106.14.145.208";
                for(int i=0;i<split.length;i++){
                    imgs.add(url+split[i]);
                }
                banner.setImages(imgs);
                banner.setBannerAnimation(Transformer.Default);
                //设置自动轮播，默认为true
                banner.isAutoPlay(true);
                //设置轮播时间
                banner.setDelayTime(2000);
                //设置指示器位置（当banner模式中有指示器时）
                banner.setIndicatorGravity(BannerConfig.CENTER);
                //banner设置方法全部调用完毕时最后调用
                banner.start();

                banner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        mbg2.startAnimation(in);
                        mbg2.setVisibility(View.VISIBLE);
                        parent2.setVisibility(View.VISIBLE);
                        photoView2.setVisibility(View.VISIBLE);
                        // ll_good_detail_bottom.setVisibility(View.GONE);
                        Glide.with(MyApplication.getContext()).load(imgs.get(position).toString()).into(photoView2);
                        Log.e(TAG,imgs.get(position).toString());
                    }
                });

               photoView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (photoView2.getVisibility()==View.VISIBLE){
                            photoView2.setVisibility(View.GONE);
                            mbg2.setVisibility(View.GONE);
                            parent2.setVisibility(View.GONE);
                            //  ll_good_detail_bottom.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }
        });



    }

    //toast的小方法
    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ShowshopInfoActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
