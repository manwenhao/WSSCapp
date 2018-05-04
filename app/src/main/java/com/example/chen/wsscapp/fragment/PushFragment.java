package com.example.chen.wsscapp.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.chen.wsscapp.Bean.PushMsg;
import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.ACache;
import com.example.chen.wsscapp.Util.MyApplication;
import com.example.chen.wsscapp.activity.PushInfo;
import com.example.chen.wsscapp.adapter.MyPushLIseViewAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class PushFragment extends Fragment {
    private TextView nopush;
    private SwipeMenuListView listView;
    private List<PushMsg> list=new ArrayList<PushMsg>();
    private MyPushLIseViewAdapter adapter;
    public static int flag=0;
    private MyPushReceiver receiver;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        flag=1;
        View view=inflater.inflate(R.layout.fragment_push,container,false);
        nopush=(TextView)view.findViewById(R.id.push_nopush);
        listView=(SwipeMenuListView)view.findViewById(R.id.push_listview);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
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
        listView.setMenuCreator(creator);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ACache aCache=ACache.get(MyApplication.getContext(),MyApplication.getUser_id());
        String datastring=aCache.getAsString("pushinfo");
        Type type=new TypeToken<List<PushMsg>>(){}.getType();
        if (datastring==null){
            nopush.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
            list=new ArrayList<PushMsg>();
        }else {
            nopush.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            list=new Gson().fromJson(datastring,type);
        }
        if (list.size()==0){
            nopush.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        }
        adapter=new MyPushLIseViewAdapter(list,MyApplication.getContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clickitem(i,aCache);
            }
        });
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index){
                    case 0:
                        //open
                        clickitem(position,aCache);
                        break;
                    case 1:
                        //delete
                        deleteitem(position,aCache);
                        break;
                }
                return false;
            }
        });
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("newpush");
        receiver=new MyPushReceiver();
        getActivity().registerReceiver(receiver,intentFilter);


    }


    @Override
    public void onResume() {
        super.onResume();
        if (flag==0){
            flag=1;
            ACache aCache=ACache.get(MyApplication.getContext(),MyApplication.user_id);
            String datastring=aCache.getAsString("pushinfo");
            Type type=new TypeToken<List<PushMsg>>(){}.getType();
            list=new Gson().fromJson(datastring,type);
            if (list!=null)
            adapter.notifyDataSetChanged();
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        flag=0;
    }

    @Override
    public void onStop() {
        super.onStop();
        flag=0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flag=0;
        getActivity().unregisterReceiver(receiver);
    }

    public void clickitem(int position, ACache aCache){
        PushMsg e=list.get(position);
        if (e.getStatus()==null){
            e.setStatus("1");
            list.remove(position);
            list.add(position,e);
            adapter.notifyDataSetChanged();
            aCache.put("pushinfo",new Gson().toJson(list));
        }else if (e.getStatus().equals("0")){
            e.setStatus("1");
            list.remove(position);
            list.add(position,e);
            adapter.notifyDataSetChanged();
            aCache.put("pushinfo",new Gson().toJson(list));
        }
        Intent intent=new Intent(MyApplication.getContext(), PushInfo.class);
        intent.putExtra("bean",e);
        getActivity().startActivity(intent);
    }
    public void deleteitem(int position,ACache aCache){
        list.remove(position);
        if (list.size()==0){
            listView.setVisibility(View.INVISIBLE);
            nopush.setVisibility(View.VISIBLE);
        }
        String da=new Gson().toJson(list);
        aCache.put("pushinfo",da);
        adapter.setDate(list);
        adapter.notifyDataSetChanged();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
    class MyPushReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if (nopush.getVisibility()==View.VISIBLE){
                nopush.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.VISIBLE);
            }
            ACache aCache=ACache.get(MyApplication.getContext(),MyApplication.user_id);
            String datastring=aCache.getAsString("pushinfo");
            Type type=new TypeToken<List<PushMsg>>(){}.getType();
            list=new Gson().fromJson(datastring,type);
            adapter.setDate(list);
            adapter.notifyDataSetChanged();

        }
    }

}
