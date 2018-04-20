package com.example.chen.wsscapp.Bean;

/**
 * Created by chen on 2018/4/13.
 */

public enum ItemType{
    AD(0),
    TALENT_SHOW(1);


    public int getValue(){
        return value;
    }

    private int value;
    ItemType(int value){
        this.value = value;
    }

}
