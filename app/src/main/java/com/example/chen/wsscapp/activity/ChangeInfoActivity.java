package com.example.chen.wsscapp.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.chen.wsscapp.Bean.Product;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.BaseActivity;
import com.example.chen.wsscapp.Util.CreateUserDialog;
import com.example.chen.wsscapp.Util.TopUi;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by chen on 2018/4/29.
 * 修改商品的参数信息
 */

public class ChangeInfoActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "ChangeInfoActivity";
    private EditText et_shopchangename,et_shopchangelogo;
    private EditText et_shopchangeuser,et_shopchangemake,et_shopchangeprice;
    private EditText et_shopchangejifen,et_shopchangeinfoshow;
    private Spinner sp_shopchangetype,sp_shopchagnediscount;
    private TextView tv_changetype,tv_changediscount;
    private TextView tv_shopchangecreate;
    private Button bt_next;
    private Button bt_surename,bt_surelogo,bt_suretype;
    private Button bt_surepeople,bt_suremake,bt_sureprice,bt_surejifen,bt_surediscount,bt_sureinfo;
    private List<String> typedataList,discountdataList;
    private CreateUserDialog createUserDialog;
    private String typedata;   //商品种类数据
    //定义一个ArrayAdapter适配器作为spinner的数据适配器
    private ArrayAdapter<String> adapter,adapter2;
    private Product product = new Product();
    private List<Product> list = new ArrayList<Product>();
    private String id;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //判断是否为小米或魅族手机，如果是则将状态栏文字改为黑色
            if (TopUi.MIUISetStatusBarLightMode(this, true) || TopUi.FlymeSetStatusBarLightMode(this, true)) {
                //设置状态栏为指定颜色
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0
                    this.getWindow().setStatusBarColor(getResources().getColor(R.color.topbackgroud));
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4
                    //调用修改状态栏颜色的方法
                    this.getWindow().setStatusBarColor(getResources().getColor(R.color.topbackgroud));
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //如果是6.0以上将状态栏文字改为黑色，并设置状态栏颜色
                this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                this.getWindow().setStatusBarColor(getResources().getColor(R.color.topbackgroud));

            }
        }
        setContentView(R.layout.activity_changeinfo);
        initView();
        initTypedata();
        initDiscountdata();
    }




    private void initView() {

        et_shopchangename = (EditText) findViewById(R.id.et_shopchangename);
        et_shopchangelogo = (EditText) findViewById(R.id.et_shopchangelogo);
        et_shopchangeuser = (EditText) findViewById(R.id.et_shopchangeuser);
        et_shopchangemake = (EditText) findViewById(R.id.et_shopchangemake);
        et_shopchangeprice = (EditText) findViewById(R.id.et_shopchangeprice);
        et_shopchangejifen = (EditText) findViewById(R.id.et_shopchangejifen);
        et_shopchangeinfoshow = (EditText) findViewById(R.id.et_shopchangeinfoshow);
        tv_changetype = (TextView) findViewById(R.id.tv_changetype);
        tv_changediscount = (TextView) findViewById(R.id.tv_changediscount);
        tv_shopchangecreate = (TextView) findViewById(R.id.tv_shopchangecreate);

        sp_shopchangetype = (Spinner) findViewById(R.id.sp_shopchangetype);
        sp_shopchagnediscount = (Spinner) findViewById(R.id.sp_shopchagnediscount);

        bt_surename = (Button) findViewById(R.id.bt_surename);
        bt_surelogo = (Button) findViewById(R.id.bt_surelogo);
        bt_suretype = (Button) findViewById(R.id.bt_suretype);
        bt_surepeople = (Button) findViewById(R.id.bt_surepeople);
        bt_suremake = (Button) findViewById(R.id.bt_suremake);
        bt_sureprice = (Button) findViewById(R.id.bt_sureprice);
        bt_surejifen = (Button) findViewById(R.id.bt_surejifen);
        bt_surediscount = (Button) findViewById(R.id.bt_surediscount);
        bt_sureinfo = (Button) findViewById(R.id.bt_sureinfo);

        bt_next = (Button) findViewById(R.id.bt_next);
        Intent intent = getIntent();
        id = intent.getStringExtra("shopid");
        initShopInfo(id);

        tv_shopchangecreate.setOnClickListener(this);
        bt_next.setOnClickListener(this);
        bt_surename.setOnClickListener(this);
        bt_surelogo.setOnClickListener(this);
        bt_suretype.setOnClickListener(this);
        bt_surepeople.setOnClickListener(this);
        bt_suremake.setOnClickListener(this);
        bt_sureprice.setOnClickListener(this);
        bt_surejifen.setOnClickListener(this);
        bt_surediscount.setOnClickListener(this);
        bt_sureinfo.setOnClickListener(this);


    }

    //产品名称选择
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.bt_save:
                    String newtype_name = createUserDialog.newtype_name.getText().toString().trim();
                    createType(newtype_name);
                    initTypedata();
                    adapter = (ArrayAdapter) sp_shopchangetype.getAdapter();
                    adapter.notifyDataSetChanged();   //刷新
                    break;
            }
        }
    };

    private void initShopInfo(final String id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .addParams("pro_id",id)
                        .url("http://106.14.145.208/ShopMall/BackAppGoodDetails")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                uitoast("获取失败");
                            }

                            @Override
                            public void onResponse(String response) {
                                if("error".equals(response.toString())){
                                    uitoast("获取失败");
                                }else{
                                    list = JSON.parseArray(response.toString(), Product.class);
                                    for(final Product p:list){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                et_shopchangename.setText( p.getPro_name());
                                                et_shopchangelogo.setText(p.getPro_brand());
                                                et_shopchangeuser.setText(p.getPro_suitperson());
                                                et_shopchangeinfoshow.setText(p.getPro_describe());
                                                et_shopchangejifen.setText("0");   //没修改积分
                                                et_shopchangemake.setText(p.getPro_material());
                                                et_shopchangeprice.setText(p.getPro_price()+"");
                                                tv_changetype.setText(p.getPro_classify());
                                                tv_changediscount.setText(p.getPro_discount());
                                                product.setPro_size(p.getPro_size());
                                                product.setPro_color(p.getPro_color());
                                             }
                                        });

                                    }
                                }

                            }
                        });

            }
        }).start();
    }

    private void initTypedata() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .url("http://106.14.145.208/ShopMall/BackAppClassify")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                uitoast("获取商品分类失败,检查网络");
                            }

                            @Override
                            public void onResponse(String response) {
                                if(response.toString().equals("error")){
                                    uitoast("获取商品分类失败");
                                }else{
                                    typedata = response.toString();
                                    getSpdata(typedata);

                                }

                            }
                        });

            }
        }).start();


    }


    public void getSpdata(String typedata) {

        typedataList = new ArrayList<String>();
        typedataList = Arrays.asList(typedata.split(","));
        adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item_1,typedataList);
        //为适配器设置下拉列表下拉时的菜单样式。
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //为spinner绑定我们定义好的数据适配器
        sp_shopchangetype.setAdapter(adapter);

        //为spinner绑定监听器，这里我们使用匿名内部类的方式实现监听器
        sp_shopchangetype.setSelection(0,false);
        sp_shopchangetype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG,"typeselect");
                tv_changetype.setText(sp_shopchangetype.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    private void initDiscountdata() {
        discountdataList = new ArrayList<String>();
        discountdataList.add("不打折");
        discountdataList.add("九折");
        discountdataList.add("八折");
        discountdataList.add("七折");
        discountdataList.add("六折");
        discountdataList.add("五折");
        discountdataList.add("四折");
        discountdataList.add("三折");
        discountdataList.add("二折");
        discountdataList.add("一折");

        adapter2 = new ArrayAdapter<String>(this, R.layout.simple_spinner_item_1,discountdataList);
        //为适配器设置下拉列表下拉时的菜单样式。
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //为spinner绑定我们定义好的数据适配器
        sp_shopchagnediscount.setAdapter(adapter2);

        //为spinner绑定监听器，这里我们使用匿名内部类的方式实现监听器
        sp_shopchagnediscount.setSelection(0,false);
        sp_shopchagnediscount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG,"discountselect");
                tv_changediscount.setText(sp_shopchagnediscount.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_shopchangecreate:
                showEditDialog(v);
                break;
            case R.id.bt_surename:
                String namestr = et_shopchangename.getText().toString();
                ChangeShopName(namestr);
                break;
            case R.id.bt_surelogo:
                String logostr = et_shopchangelogo.getText().toString();
                ChangeShopLogo(logostr);
                break;
            case R.id.bt_suretype:
                String typestr = tv_changetype.getText().toString();
                ChangeShopType(typestr);
                break;
            case R.id.bt_surepeople:
                String peoplestr = et_shopchangeuser.getText().toString();
                ChangeShopPeople(peoplestr);
                break;
            case R.id.bt_suremake:
                String makestr = et_shopchangemake.getText().toString();
                ChangeShopMake(makestr);
                break;
            case R.id.bt_sureprice:
                String pricestr = et_shopchangeprice.getText().toString();
                ChangeShopPrice(pricestr);
                break;
            case R.id.bt_surejifen:
                String jifenstr = et_shopchangejifen.getText().toString();
                ChangeShopJifen(jifenstr);
                break;
            case R.id.bt_surediscount:
                String discountstr = tv_changediscount.getText().toString();
                ChangeShopDiscount(discountstr);
                break;
            case R.id.bt_sureinfo:
                String infostr = et_shopchangeinfoshow.getText().toString();
                ChangeShopInfo(infostr);
                break;
            case R.id.bt_next:
                Intent intent = new Intent(this,ChangeColSizeActivity.class);
                intent.putExtra("iddata",id);
                intent.putExtra("colordata",product.getPro_color());
                intent.putExtra("sizedata",product.getPro_size());
                startActivity(intent);
                break;
        }

    }

    private void ChangeShopInfo(final String infostr) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .addParams("pro_id",id)
                        .addParams("key","pro_describe")
                        .addParams("value",infostr)
                        .url("http://106.14.145.208/ShopMall/ModifyGood")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                uitoast("更改商品简介失败");
                            }

                            @Override
                            public void onResponse(String response) {
                                if("ok".equals(response.toString())){
                                    uitoast("更改商品简介成功");
                                }else{
                                    uitoast("更改商品简介失败");
                                }

                            }
                        });

            }
        }).start();
    }

    private void ChangeShopDiscount(final String discountstr) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .addParams("pro_id",id)
                        .addParams("key","pro_discount")
                        .addParams("value",discountstr)
                        .url("http://106.14.145.208/ShopMall/ModifyGood")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                uitoast("更改商品折扣失败");
                            }

                            @Override
                            public void onResponse(String response) {
                                if("ok".equals(response.toString())){
                                    uitoast("更改商品折扣成功");
                                }else{
                                    uitoast("更改商品折扣失败");
                                }

                            }
                        });

            }
        }).start();
    }

    private void ChangeShopJifen(final String jifenstr) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .addParams("pro_id",id)
                        .addParams("key","pro_jfvalue")
                        .addParams("value",jifenstr)
                        .url("http://106.14.145.208/ShopMall/ModifyGood")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                uitoast("更改商品积分失败");
                            }

                            @Override
                            public void onResponse(String response) {
                                if("ok".equals(response.toString())){
                                    uitoast("更改商品积分成功");
                                }else{
                                    uitoast("更改商品积分失败");
                                }

                            }
                        });

            }
        }).start();
    }

    private void ChangeShopPrice(final String pricestr) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .addParams("pro_id",id)
                        .addParams("key","pro_price")
                        .addParams("value",pricestr)
                        .url("http://106.14.145.208/ShopMall/ModifyGood")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                uitoast("更改商品价格失败");
                            }

                            @Override
                            public void onResponse(String response) {
                                if("ok".equals(response.toString())){
                                    uitoast("更改商品价格成功");
                                }else{
                                    uitoast("更改商品价格失败");
                                }

                            }
                        });

            }
        }).start();
    }

    private void ChangeShopMake(final String makestr) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .addParams("pro_id",id)
                        .addParams("key","pro_material")
                        .addParams("value",makestr)
                        .url("http://106.14.145.208/ShopMall/ModifyGood")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                uitoast("更改商品材料失败");
                            }

                            @Override
                            public void onResponse(String response) {
                                if("ok".equals(response.toString())){
                                    uitoast("更改商品材料成功");
                                }else{
                                    uitoast("更改商品材料失败");
                                }

                            }
                        });

            }
        }).start();
    }

    private void ChangeShopPeople(final String peoplestr) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .addParams("pro_id",id)
                        .addParams("key","pro_suitperson")
                        .addParams("value",peoplestr)
                        .url("http://106.14.145.208/ShopMall/ModifyGood")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                uitoast("更改商品人群失败");
                            }

                            @Override
                            public void onResponse(String response) {
                                if("ok".equals(response.toString())){
                                    uitoast("更改商品人群成功");
                                }else{
                                    uitoast("更改商品人群失败");
                                }
                            }
                        });
            }
        }).start();
    }

    private void ChangeShopType(final String typestr) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .addParams("pro_id",id)
                        .addParams("key","pro_classify")
                        .addParams("value",typestr)
                        .url("http://106.14.145.208/ShopMall/ModifyGood")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                uitoast("更改商品分类失败");
                            }

                            @Override
                            public void onResponse(String response) {
                                if("ok".equals(response.toString())){
                                    uitoast("更改商品分类成功");
                                }else{
                                    uitoast("更改商品分类失败");
                                }
                            }
                        });
            }
        }).start();
    }

    private void ChangeShopLogo(final String logostr) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .addParams("pro_id",id)
                        .addParams("key","pro_brand")
                        .addParams("value",logostr)
                        .url("http://106.14.145.208/ShopMall/ModifyGood")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                uitoast("更改商品品牌失败");
                            }

                            @Override
                            public void onResponse(String response) {
                                if("ok".equals(response.toString())){
                                    uitoast("更改商品品牌成功");
                                }else{
                                    uitoast("更改商品品牌失败");
                                }

                            }
                        });

            }
        }).start();
    }

    private void ChangeShopName(final String namestr) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .addParams("pro_id",id)
                        .addParams("key","pro_name")
                        .addParams("value",namestr)
                        .url("http://106.14.145.208/ShopMall/ModifyGood")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                uitoast("更改商品名失败");
                            }

                            @Override
                            public void onResponse(String response) {
                                if("ok".equals(response.toString())){
                                    uitoast("更改商品名成功");
                                }else{
                                    uitoast("更改商品名失败");
                                }

                            }
                        });

            }
        }).start();
    }

    private void showEditDialog(View v) {
        createUserDialog = new CreateUserDialog(this, R.style.Dialog_Common,onClickListener);
        createUserDialog.show();
    }

    private void createType(final String newtype_name) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .addParams("classifyname",newtype_name)
                        .url("http://106.14.145.208/ShopMall/CreateGoodClassify")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                uitoast("添加失败,检查网络");

                            }

                            @Override
                            public void onResponse(String response) {
                                if(response.toString().equals("ok")){
                                    uitoast("添加成功");
                                }else{
                                    uitoast("添加失败");

                                }

                            }

                        });
            }
        }).start();
    }
}
