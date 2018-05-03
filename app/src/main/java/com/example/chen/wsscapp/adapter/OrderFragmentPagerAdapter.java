package com.example.chen.wsscapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by chen on 2018/4/26.
 */

public class OrderFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> list;

    private String mtitle[] ={"待发货","待收货","已完成"};

    public OrderFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    private int mChildCount = 0;

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object)   {
        if ( mChildCount > 0) {
            mChildCount --;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    //用来设置tab的标题
    @Override
    public CharSequence getPageTitle(int position) {
        return mtitle[position];
    }
}
