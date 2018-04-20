package com.example.chen.wsscapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.wsscapp.Bean.Product;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.BaseActivity;
import com.example.chen.wsscapp.Util.CreateColorDialog;
import com.example.chen.wsscapp.Util.CreateSizeDialog;
import com.example.chen.wsscapp.Util.CreateUserDialog;
import com.lybeat.multiselector.BaseOption;
import com.lybeat.multiselector.MultiSelector;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by chen on 2018/3/15.
 * 商家管理商品上架
 */

public class BecomeShopActivity extends BaseActivity implements View.OnClickListener{

    private String TAG = "BecomeShopActivity";
    private Spinner sp_shoptype,sp_shopdiscount;
    //定义一个String类型的List数组作为数据源
    private List<String> typedataList,discountdataList;
    private EditText et_shopname,et_shoplogo,et_shopuser,et_shopmake,et_shopprice,et_shopinfoshow;
    private TextView tv_shoptypecreate,tv_shopcolor,tv_shopsize;
    private  MultiSelector multiSelector;
    private Button bt_next;
    private CreateUserDialog createUserDialog;
    private CreateColorDialog createColorDialog;
    private CreateSizeDialog createSizeDialog;
    private String typedata;   //商品种类数据
    private String newType; //新商品种类
    //定义一个ArrayAdapter适配器作为spinner的数据适配器
    private ArrayAdapter<String> adapter,adapter2;
    Product product = new Product();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_becomeshop);
        initView();
        initTypedata();
        initDiscountdata();

    }




    private void initView() {
        et_shopname = (EditText) findViewById(R.id.et_shopname);
        et_shoplogo = (EditText) findViewById(R.id.et_shoplogo);
        sp_shoptype = (Spinner) findViewById(R.id.sp_shoptype);
        tv_shoptypecreate = (TextView) findViewById(R.id.tv_shopcreate);
        et_shopuser = (EditText) findViewById(R.id.et_shopuser);
        et_shopmake = (EditText) findViewById(R.id.et_shopmake);
        et_shopprice = (EditText) findViewById(R.id.et_shopprice);
        tv_shopcolor = (TextView) findViewById(R.id.tv_shopcolor);
        tv_shopsize = (TextView) findViewById(R.id.tv_shopsize);
        sp_shopdiscount = (Spinner) findViewById(R.id.sp_shopdiscount);
        et_shopinfoshow = (EditText) findViewById(R.id.et_shopinfoshow);
        bt_next = (Button) findViewById(R.id.bt_next);
        tv_shopcolor.setOnClickListener(this);
        tv_shoptypecreate.setOnClickListener(this);
        bt_next.setOnClickListener(this);
        tv_shopsize.setOnClickListener(this);
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
                                toast("获取商品分类失败,检查网络");
                            }

                            @Override
                            public void onResponse(String response) {
                                if(response.toString().equals("error")){
                                    toast("获取商品分类失败");
                                }else{
                                     typedata = response.toString();
                                     getSpdata(typedata);

                                }

                            }
                        });

            }
        }).start();





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

        adapter2 = new ArrayAdapter<String>(this,R.layout.simple_spinner_item_1,discountdataList);
        //为适配器设置下拉列表下拉时的菜单样式。
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //为spinner绑定我们定义好的数据适配器
        sp_shopdiscount.setAdapter(adapter2);

        //为spinner绑定监听器，这里我们使用匿名内部类的方式实现监听器
        sp_shopdiscount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               product.setPro_discount(sp_shopdiscount.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }





    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_shopcreate:
                showEditDialog(v);
                break;
            case R.id.bt_next:
                //进行为空的条件判断
                if("".equals(et_shopname.getText().toString())||null==et_shopname.getText().toString()){
                    showToast("商品名不能为空");
                    return;
                }
                if("".equals(et_shoplogo.getText().toString())||null==et_shoplogo.getText().toString()){
                    showToast("商品品牌不能为空");
                    return;
                }
                if("".equals(et_shopuser.getText().toString())||null==et_shopuser.getText().toString()){
                    showToast("使用人群不能为空");
                    return;
                }
                if("".equals(et_shopmake.getText().toString())||null==et_shopmake.getText().toString()){
                    showToast("使用人群不能为空");
                    return;
                }
                if("".equals(tv_shopcolor.getText().toString().trim())||null==tv_shopcolor.getText().toString()){
                    showToast("商品颜色不能为空");
                    return;
                }
                if("".equals(tv_shopsize.getText().toString().trim())||null==tv_shopsize.getText().toString()){
                    showToast("商品尺寸不能为空");
                    return;
                }
                if("".equals(et_shopprice.getText().toString())||null==et_shopprice.getText().toString()){
                    showToast("商品价格不能为空");
                    return;
                }
                if("".equals(et_shopinfoshow.getText().toString())||null==et_shopinfoshow.getText().toString()){
                    showToast("描述信息不能为空");
                    return;
                }
                Intent intent = new Intent(this,BecomephotoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data",getAllData());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.tv_shopcolor:
                showColorDialog(v);
                break;
            case R.id.tv_shopsize:
                showSizeDialog(v);
                break;
        }

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
                                toast("添加失败,检查网络");

                            }

                            @Override
                            public void onResponse(String response) {
                                if(response.toString().equals("ok")){
                                    toast("添加成功");

                                }else{
                                    toast("添加失败");

                                }

                            }

                        });
            }
        }).start();
    }

    //toast的小方法
    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BecomeShopActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getSpdata(String typedata) {

        typedataList = new ArrayList<String>();
        typedataList = Arrays.asList(typedata.split(","));
        adapter = new ArrayAdapter<String>(this,R.layout.simple_spinner_item_1,typedataList);
        //为适配器设置下拉列表下拉时的菜单样式。
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //为spinner绑定我们定义好的数据适配器
        sp_shoptype.setAdapter(adapter);

        //为spinner绑定监听器，这里我们使用匿名内部类的方式实现监听器
        sp_shoptype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                     product.setPro_classify(sp_shoptype.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



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
                    ArrayAdapter adapter = (ArrayAdapter) sp_shoptype.getAdapter();
                    adapter.notifyDataSetChanged();   //刷新
                    break;
            }
        }
    };



    //颜色选择
    private MultiSelector.OnSelectListener vselect = new MultiSelector.OnSelectListener() {
        @Override
        public void onSelect(List<String> choices) {
            StringBuilder sb = new StringBuilder();
            for (String choice : choices) {
                if("".equals(choice)) {
                       continue;
                }else {
                    sb.append(choice);
                    sb.append(",");
                }
            }


               tv_shopcolor.setText(sb.toString().substring(0,sb.toString().length()-1));
                product.setPro_color(sb.toString().substring(0,sb.toString().length()-1));
        }
    };



    //尺寸选择
    private MultiSelector.OnSelectListener cselect = new MultiSelector.OnSelectListener() {
        @Override
        public void onSelect(List<String> choices) {
            StringBuilder sb = new StringBuilder();
            for (String choice : choices) {
                if("".equals(choice)) {
                    continue;
                }else {
                    sb.append(choice);
                    sb.append(",");
                }
            }
                tv_shopsize.setText( sb.toString().substring(0,sb.toString().length()-1));
                product.setPro_size( sb.toString().substring(0,sb.toString().length()-1));
        }
    };


    public void showEditDialog(View view) {
        createUserDialog = new CreateUserDialog(this,R.style.Dialog_Common,onClickListener);
        createUserDialog.show();
    }

    public void showColorDialog(View view) {
        createColorDialog = new CreateColorDialog(this,R.style.Dialog_Common,vselect);
        createColorDialog.show();
    }


    private void showSizeDialog(View v) {
        createSizeDialog = new CreateSizeDialog(this,R.style.Dialog_Common,cselect);
        createSizeDialog.show();
    }

    public Product getAllData() {
        product.setPro_name(et_shopname.getText().toString().trim());
        product.setPro_brand(et_shoplogo.getText().toString().trim());
        product.setPro_suitperson(et_shopuser.getText().toString().trim());
        product.setPro_material(et_shopmake.getText().toString().trim());
        product.setPro_price(Float.parseFloat(et_shopprice.getText().toString().trim()));
        product.setPro_describe(et_shopinfoshow.getText().toString().trim());
        Log.d(TAG,"INFO "+product.toString());
        return product;
    }



}
