package com.example.chen.wsscapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chen.wsscapp.Bean.ShopCarProduct;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.GetTel;
import com.example.chen.wsscapp.Util.MyApplication;

import java.util.List;

/**
 * Created by xuxuxiao on 2018/4/19.
 */

public class ShopCartAdapter extends BaseAdapter {
    public LayoutInflater inflater;
    public List<ShopCarProduct> list;
    private CheckInterface checkInterface;
    private ModifyCountInterface modifyCountInterface;
    int count=0;
    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }

    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }
    public ShopCartAdapter() {
    }

    public ShopCartAdapter(List<ShopCarProduct> list, Context context) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder vh;
        if (view==null){
            view=inflater.inflate(R.layout.item_shopcart_product,viewGroup,false);
            vh=new ViewHolder();
            vh.btAdd=(Button)view.findViewById(R.id.bt_add);
            vh.btReduce=(Button)view.findViewById(R.id.bt_reduce);
            vh.checkBox=(CheckBox)view.findViewById(R.id.check_box);
            vh.etNum=(TextView) view.findViewById(R.id.et_num);
            vh.ivAdapterListPic=(ImageView)view.findViewById(R.id.iv_adapter_list_pic);
            vh.tvBuyNum=(TextView)view.findViewById(R.id.tv_buy_num);
            vh.tvColorSize=(TextView)view.findViewById(R.id.tv_color_size);
            vh.tvDiscountPrice=(TextView)view.findViewById(R.id.tv_discount_price);
           // vh.tvGoodsDelete=(TextView)view.findViewById(R.id.tv_goods_delete);
            vh.tvIntro=(TextView)view.findViewById(R.id.tv_intro);
            vh.tvPrice=(TextView)view.findViewById(R.id.tv_price);
            view.setTag(vh);
        }else {
            vh=(ViewHolder)view.getTag();
        }
        final ShopCarProduct product=list.get(i);
        if (product!=null){
            vh.tvIntro.setText(product.getPro_name());
            vh.tvPrice.setText("¥"+ GetTel.getFloat(Float.valueOf(product.getPro_price())* Float.valueOf(product.getPro_discount())));
            vh.etNum.setText(product.getPro_num());
            vh.tvColorSize.setText(product.getPro_color()+";"+product.getPro_size());
            Glide.with(MyApplication.getContext()).load("http://106.14.145.208:80"+product.getPro_photo()).into(vh.ivAdapterListPic);
           // vh.tvColorsize.setText(product.getPro_color()+";"+product.getPro_size());
            SpannableString spanString = new SpannableString("￥" + product.getPro_price());
            StrikethroughSpan span = new StrikethroughSpan();
            spanString.setSpan(span, 0, product.getPro_discount().length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //避免无限次的appand
            if (vh.tvDiscountPrice.getText().toString().length() > 0) {
                vh.tvDiscountPrice.setText("");
            }
            vh.tvDiscountPrice.append(spanString);
            vh.tvBuyNum.setText("x"+product.getPro_num());
            vh.checkBox.setChecked(product.isIschoose());
            vh.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    product.setIschoose(((CheckBox)view).isChecked());
                    vh.checkBox.setChecked(((CheckBox)view).isChecked());
                    checkInterface.checkchild(i,((CheckBox)view).isChecked());
                }
            });
            vh.btAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    modifyCountInterface.DoIncrease(i,vh.etNum,vh.checkBox.isChecked());
                }
            });
            vh.btReduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    modifyCountInterface.DoDecrease(i,vh.etNum,vh.checkBox.isChecked());
                }
            });

            notifyDataSetChanged();
           /* vh.tvGoodsDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog alert = new AlertDialog.Builder(MyApplication.getContext()).create();
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
                                    modifyCountInterface.ChildDelete(i);
                                }
                            });
                    alert.show();
                }
            });
            */

        }
        return view;
    }
    class ViewHolder{
        CheckBox checkBox;
        ImageView ivAdapterListPic;
        TextView tvIntro;
        TextView tvColorSize;
        TextView tvPrice;
        TextView tvDiscountPrice;
        TextView tvBuyNum;
        RelativeLayout rlNoEdtor;
        Button btReduce;
        TextView etNum;
        Button btAdd;
        RelativeLayout llChangeNum;
       // TextView tvColorsize;
        //TextView tvGoodsDelete;
        LinearLayout llEdtor;

    }

    public interface CheckInterface{
        void checkchild(int position, boolean ischecked);
    }
    public interface ModifyCountInterface{
        void DoIncrease(int position, View view, boolean ischecked);
        void DoDecrease(int position, View view, boolean ischecked);

    }

    private void showDialog(final ShopCarProduct goodinfo,final EditText edittext){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MyApplication.getContext());
        View alertDialogView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.dialog_change_num, null,false);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setView(alertDialogView);
        count = Integer.valueOf(goodinfo.getPro_num());
        final EditText editText = (EditText) alertDialogView.findViewById(R.id.et_num);
        editText.setText(""+goodinfo.getPro_num());//设置dialog的数量初始值
        //自动弹出软键盘
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) MyApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });
        final   Button btadd= (Button) alertDialogView.findViewById(R.id.bt_add);
        final   Button btreduce= (Button) alertDialogView.findViewById(R.id.bt_reduce);
        final   TextView cancle= (TextView) alertDialogView.findViewById(R.id.tv_cancle);
        final   TextView sure= (TextView) alertDialogView.findViewById(R.id.tv_sure);
        cancle.setOnClickListener(new View.OnClickListener() { //取消按钮
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {//确定按钮
            @Override
            public void onClick(View v) {
                goodinfo.setPro_num(String.valueOf(count));//重新设置数量
                edittext.setText(count+"");//购物车界面的文本框显示同步
                alertDialog.dismiss();
            }
        });
        btadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count ++;   //点一下量加1
                editText.setText(""+count);//动态显示dialog的文本框的数据

            }
        });
        btreduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count>1) {//数量大雨1时操作
                    count--; //点一下减1
                    editText.setText("" + count);
                }
            }
        });
        alertDialog.show();
    }
    public void setdate(List<ShopCarProduct> list){
        this.list=list;
    }
}
