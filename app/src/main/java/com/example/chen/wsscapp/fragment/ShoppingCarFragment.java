package com.example.chen.wsscapp.fragment;


import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.chen.wsscapp.Bean.ShopCarProduct;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.GetTel;
import com.example.chen.wsscapp.Util.MyApplication;
import com.example.chen.wsscapp.adapter.ShopCartAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingCarFragment extends Fragment implements ShopCartAdapter.CheckInterface,ShopCartAdapter.ModifyCountInterface{
    private TextView title,subtitle,totalprice,gotopay,tvdelete;
    private SwipeMenuListView swipeMenuListView;
    private CheckBox allcheckBox;
    private LinearLayout allinfo,allshar,cartempty;

    private float totalPrice = 0;// 购买的商品总价
    private int totalCount = 0;// 购买的商品总数量
    private ShopCartAdapter adapter;
    private List<ShopCarProduct> list=new ArrayList<>();
    private int flag=0;
    final String tel= GetTel.gettel();

    public ShoppingCarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_shoppingcar, container, false);
        title=(TextView)view.findViewById(R.id.shop_title);
        totalprice=(TextView)view.findViewById(R.id.tv_total_price);
        gotopay=(TextView)view.findViewById(R.id.tv_go_to_pay);
        tvdelete=(TextView)view.findViewById(R.id.tv_delete);
        swipeMenuListView=(SwipeMenuListView)view.findViewById(R.id.exListView);
        allcheckBox=(CheckBox)view.findViewById(R.id.all_chekbox);
        allinfo=(LinearLayout)view.findViewById(R.id.ll_info);
        allshar=(LinearLayout)view.findViewById(R.id.ll_shar);
        cartempty=(LinearLayout)view.findViewById(R.id.layout_cart_empty);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
             /*   // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        MyApplication.getContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);
*/
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        MyApplication.getContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        swipeMenuListView.setMenuCreator(creator);

        return view ;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter=new ShopCartAdapter(list,MyApplication.getContext());
        adapter.setCheckInterface(this);
        adapter.setModifyCountInterface(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .url("http://106.14.145.208/ShopMall/BackAppUserCart")
                        .addParams("pro_id",tel)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(String response) {
                                Log.d("获取购物车商品信息",response);
                                if (!response.equals("error")){
                                    Type type=new TypeToken<List<ShopCarProduct>>(){}.getType();
                                    list=new Gson().fromJson(response,type);
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            for (int i=0;i<list.size();i++){
                                                list.get(i).setIschoose(false);
                                            }
                                            adapter.setdate(list);
                                            swipeMenuListView.setAdapter(adapter);
                                            swipeMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                                }
                                            });
                                            swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                                                @Override
                                                public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                                                    switch (index){
                                                        case 0:
                                                            AlertDialog alert = new AlertDialog.Builder(getActivity()).create();
                                                            alert.setTitle("操作提示");
                                                            alert.setMessage("您确定要将这些商品从购物车中移除吗？");
                                                            alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                                                                    new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {

                                                                            return;
                                                                        }
                                                                    });
                                                            alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                                                                    new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            ChildDelete(position);
                                                                        }
                                                                    });
                                                            alert.show();
                                                            break;
                                                    }
                                                    return false;
                                                }
                                            });
                                            allcheckBox.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    deCheckAll();
                                                }
                                            });
                                            gotopay.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    int totalCount1 = 0;
                                                    float totalPrice1 = 0;
                                                    for (ShopCarProduct e:list){
                                                        if (e.isIschoose()){
                                                            totalCount1++;
                                                            totalPrice1+=Integer.valueOf(e.getPro_num()) *Float.valueOf(e.getPro_price())*Float.valueOf(e.getPro_discount());
                                                        }
                                                    }
                                                    String allPrice = GetTel.getFloat(totalPrice1);
                                                    Toast.makeText(MyApplication.getContext(), "商品数："+totalCount1+" 商品总价："+allPrice, Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        });
            }
        }).start();
    }


    @Override
    public void checkchild(int position, boolean ischecked) {
        if (isallchecked()){
            allcheckBox.setChecked(true);
        }else {
            allcheckBox.setChecked(false);
        }
        adapter.notifyDataSetChanged();
        calculate();
    }
    public boolean isallchecked(){
        for (ShopCarProduct e:list){
            if (!e.isIschoose()){
                return false;
            }
        }
        return true;
    }
    public void deCheckAll(){
        for(int i=0;i<list.size();i++){
            list.get(i).setIschoose(allcheckBox.isChecked());
        }
        adapter.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void DoIncrease(final int position, final View view, boolean ischecked) {

        final ShopCarProduct e=list.get(position);
        int currentcount=Integer.valueOf(e.getPro_num());
        currentcount++;
        final String countstring=String.valueOf(currentcount);
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .url("http://106.14.145.208/ShopMall/UpdateUserCartInfo")
                        .addParams("pro_id",tel)
                        .addParams("good_id",e.getPro_id())
                        .addParams("good_num",countstring)
                        .addParams("good_size",e.getPro_size())
                        .addParams("good_color",e.getPro_color())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                e.printStackTrace();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MyApplication.getContext(), "修改失败，请重试！", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(String response) {
                                Log.d("增加商品数量",response);
                                if (response.equals("ok")){
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            list.get(position).setPro_num(countstring);
                                            ((TextView)view).setText(countstring);
                                            adapter.notifyDataSetChanged();
                                            calculate();
                                        }
                                    });
                                }else {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MyApplication.getContext(), "修改失败，请重试！", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
            }
        }).start();


    }

    @Override
    public void DoDecrease(final int position,final View view, boolean ischecked) {
        final ShopCarProduct e=list.get(position);
        int currentcount=Integer.valueOf(e.getPro_num());
        if (currentcount==1){
            return;
        }
        currentcount--;
        final String countstring=String.valueOf(currentcount);
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .url("http://106.14.145.208/ShopMall/UpdateUserCartInfo")
                        .addParams("pro_id",tel)
                        .addParams("good_id",e.getPro_id())
                        .addParams("good_num",countstring)
                        .addParams("good_size",e.getPro_size())
                        .addParams("good_color",e.getPro_color())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                e.printStackTrace();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MyApplication.getContext(), "修改失败，请重试！", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(String response) {
                                Log.d("减少商品数量",response);
                                if (response.equals("ok")){
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            list.get(position).setPro_num(countstring);
                                            ((TextView)view).setText(countstring);
                                            adapter.notifyDataSetChanged();
                                            calculate();
                                        }
                                    });
                                }else {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MyApplication.getContext(), "修改失败，请重试！", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
            }
        }).start();

    }


    public void ChildDelete(final int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .url("http://106.14.145.208/ShopMall/DeleteCartAGood")
                        .addParams("pro_id",tel)
                        .addParams("good_id",list.get(position).getPro_id())
                        .addParams("good_size",list.get(position).getPro_size())
                        .addParams("good_color",list.get(position).getPro_color())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                e.printStackTrace();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MyApplication.getContext(), "删除失败，请重试！", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(String response) {
                                Log.d("删除购物车商品",response);
                                if (response.equals("ok")){
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MyApplication.getContext(), "删除成功！", Toast.LENGTH_SHORT).show();
                                            list.remove(position);
                                            adapter.notifyDataSetChanged();
                                            calculate();
                                        }
                                    });
                                }else {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MyApplication.getContext(), "删除失败，请重试！", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
            }
        }).start();
    }


    private void calculate(){
        totalCount = 0;
        totalPrice = 0;
        for (ShopCarProduct e:list){
            if (e.isIschoose()){
                totalCount++;
                totalPrice+=Integer.valueOf(e.getPro_num()) *Float.valueOf(e.getPro_price())* Float.valueOf(e.getPro_discount()) ;
            }
        }
        totalprice.setText("¥"+ GetTel.getFloat(totalPrice));
        gotopay.setText("去支付("+totalCount+")");


    }
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

}

