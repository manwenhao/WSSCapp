package com.example.chen.wsscapp.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.ACache;
import com.example.chen.wsscapp.Util.MyApplication;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Calendar;

/**
 * Created by chen on 2018/3/26.
 */

public class SetBirthActivity extends Activity implements View.OnClickListener {
    private int mYear;
    private int mMonth;
    private int mDay;
    private int nYear;
    private int nMonth;
    private int nDay;
    private Calendar calendar;
    private TextView tv_setbrith;
    private Button bt_setbirth;
    private static final int START_DATE = 1;
    private static final int END_DATE = 2;
    final ACache mAcahe = ACache.get(MyApplication.getContext(),"userdata");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setbirth);
        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        nYear = calendar.get(Calendar.YEAR);
        nMonth = calendar.get(Calendar.MONTH);
        nDay = calendar.get(Calendar.DAY_OF_MONTH);
        tv_setbrith = (TextView) findViewById(R.id.tv_setbrith);
        bt_setbirth = (Button) findViewById(R.id.toolbar_right_btn);
        tv_setbrith.setText(mAcahe.getAsString("user_birth"));
        tv_setbrith.setOnClickListener(this);
        bt_setbirth.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_setbrith:
                showDialog(START_DATE);
                break;
            case R.id.toolbar_right_btn:
                String birth = tv_setbrith.getText().toString();
                SharedPreferences pref = getSharedPreferences("user_data",MODE_PRIVATE);
                String idphone = pref.getString("phone","");
                setBirth(idphone,birth);
                break;
        }

    }


    /**
     * 修改设置生日
     *idphone 为手机号码
     *birth 为生日日期
     */
    private void setBirth(final String idphone, final String birth) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .addParams("id",idphone)
                        .addParams("key","user_birth")
                        .addParams("value",birth)
                        .url("http://106.14.145.208/ShopMall/UpdateForUserInfo")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                toast("生日修改失败");
                            }

                            @Override
                            public void onResponse(String response) {
                                if(response.toString().equals("ok")){
                                    toast("生日修改成功");
                                    mAcahe.put("user_birth",birth);
                                    Intent intent = new Intent(SetBirthActivity.this,SetUserInfoActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else if(response.toString().equals("error")){
                                    toast("生日修改失败");
                                }

                            }
                        });

            }
        }).start();


    }


    //日期选择函数
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case START_DATE:
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
                        mDay);
            case END_DATE:
                return new DatePickerDialog(this, nDateSetListener, nYear, nMonth,
                        nDay);
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case START_DATE:
                ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                break;
            case END_DATE:
                ((DatePickerDialog) dialog).updateDate(nYear, nMonth, nDay);
                break;
            default:
                break;
        }
    }

    private void setDateTime() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay =c.get(Calendar.DAY_OF_MONTH);

        nYear = c.get(Calendar.YEAR);
        nMonth = c.get(Calendar.MONTH);
        nDay =c.get(Calendar.DAY_OF_MONTH);
        //更新Button上显示的日期
        updateDateDisplay();
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            //更新Button上显示的日期
            updateDateDisplay();
        }
    };


    private DatePickerDialog.OnDateSetListener nDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            nYear = year;
            nMonth = monthOfYear;
            nDay = dayOfMonth;
            //更新Button上显示的日期
            updateDateDisplay();
        }
    };

    private void updateDateDisplay() {
        tv_setbrith.setText(new StringBuilder().append(mYear).append("-")
                .append(mMonth + 1).append("-").append(mDay));
    }

    //toast的小方法
    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SetBirthActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
