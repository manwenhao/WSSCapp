package com.example.chen.wsscapp.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.chen.wsscapp.R;
import com.example.chen.wsscapp.Util.CustomViewPager;
import com.example.chen.wsscapp.adapter.MyFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {
    private RadioGroup radioGroup;
    private RadioButton msg,push;
    private CustomViewPager viewPager;
    ConversationListFragment conversationListFragment =new ConversationListFragment();
    PushFragment pushFragment=new PushFragment();


    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chat, container, false);
        radioGroup=(RadioGroup)view.findViewById(R.id.tongzhi_rg);
        msg=(RadioButton)view.findViewById(R.id.msg_rbt);
        push=(RadioButton)view.findViewById(R.id.push_rbt);
        viewPager=(CustomViewPager) view.findViewById(R.id.chat_vp);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.msg_rbt:
                        viewPager.setCurrentItem(0,false);
                        break;
                    case R.id.push_rbt:
                        viewPager.setCurrentItem(1,false);
                        break;
                }
            }
        });
        viewPager.setScanScroll(false);
        final List<Fragment> allfragment=new ArrayList<Fragment>();
        allfragment.add(conversationListFragment);
        allfragment.add(pushFragment);
        final MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getChildFragmentManager(), allfragment);
        viewPager.setAdapter(adapter);
        //ViewPager显示第一个Fragment
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        radioGroup.check(R.id.msg_rbt);
                        break;
                    case 1:
                        radioGroup.check(R.id.push_rbt);
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }
}
