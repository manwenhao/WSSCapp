package com.example.chen.wsscapp.Util.keyboard.interfaces;

import android.view.View;
import android.view.ViewGroup;

import com.example.chen.wsscapp.Util.keyboard.data.PageEntity;


public interface PageViewInstantiateListener<T extends PageEntity> {

    View instantiateItem(ViewGroup container, int position, T pageEntity);
}
