package com.example.chen.wsscapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chen.wsscapp.Bean.PushMsg;
import com.example.chen.wsscapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by xuxuxiao on 2018/4/26.
 */

public class MyPushLIseViewAdapter extends BaseAdapter{
    private List<PushMsg> list;
    private LayoutInflater inflater;

    public MyPushLIseViewAdapter(List<PushMsg> list, Context context) {
        this.list = list;
        this.inflater =LayoutInflater.from(context);
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if (view==null){
            view=inflater.inflate(R.layout.push_listview_item,viewGroup,false);
            vh=new ViewHolder();
            vh.title=(TextView)view.findViewById(R.id.system_news_item_title);
            vh.content=(TextView)view.findViewById(R.id.system_news_item_cont);
            vh.time=(TextView)view.findViewById(R.id.system_news_item_time);
            vh.imageView=(ImageView)view.findViewById(R.id.system_news_item_icon);
            view.setTag(vh);
        }else {
            vh=(ViewHolder)view.getTag();
        }
        vh.title.setText(list.get(i).getTitle());
        vh.content.setText(list.get(i).getContent());
        if (list.get(i).getStatus()==null){
            vh.imageView.setVisibility(View.VISIBLE);
        }else if (list.get(i).getStatus().equals("0")){   //未读
            vh.imageView.setVisibility(View.VISIBLE);
        }else {
            vh.imageView.setVisibility(View.INVISIBLE);
        }
        String time=list.get(i).getTime();
        SimpleDateFormat sfd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sf2=new SimpleDateFormat("HH:mm:ss");
        try{
            Date datetime=sfd.parse(time);
            if (isSameDay(new Date(),datetime)){
                //是同一天
                vh.time.setText(sf2.format(datetime));
            }else{
                vh.time.setText(sf.format(datetime));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }
    public void setDate(List<PushMsg> list){
        this.list=list;
    }
    class ViewHolder{
        TextView title,content,time;
        ImageView imageView;
    }
    public static boolean isSameDay(Date date1, Date date2) {
        Calendar calDateA = Calendar.getInstance();
        calDateA.setTime(date1);

        Calendar calDateB = Calendar.getInstance();
        calDateB.setTime(date2);

        return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
                && calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
                && calDateA.get(Calendar.DAY_OF_MONTH) == calDateB
                .get(Calendar.DAY_OF_MONTH);
    }
}
