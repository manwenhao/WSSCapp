package com.example.chen.wsscapp.Util.keyboard.interfaces;

import android.view.ViewGroup;

import com.example.chen.wsscapp.Util.keyboard.adpater.EmoticonsAdapter;


public interface EmoticonDisplayListener<T> {

    void onBindView(int position, ViewGroup parent, EmoticonsAdapter.ViewHolder viewHolder, T t, boolean isDelBtn);
}
