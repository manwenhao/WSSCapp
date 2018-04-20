package com.example.chen.wsscapp.Bean;

import java.io.Serializable;

/**
 * Created by chen on 2018/4/13.
 * pro_id  商品id
 * pro_name 商品名称
 * pro_brand 商品品牌
 * pro_price 商品原价
 * pro_discount 商品折扣
 * pro_describe 商品简介
 * pro_photo 商品图片路径
 */


public class TalentShow {
    private String pro_id;
    private String pro_name;
    private String pro_brand;
    private float pro_price;
    private float pro_discount;
    private String pro_describe;
    private String pro_photo;

    public TalentShow() {
        super();
    }

    public TalentShow(String pro_id, String pro_name, String pro_brand, float pro_price, float pro_discount, String pro_describe, String pro_photo) {
        this.pro_id = pro_id;
        this.pro_name = pro_name;
        this.pro_brand = pro_brand;
        this.pro_price = pro_price;
        this.pro_discount = pro_discount;
        this.pro_describe = pro_describe;
        this.pro_photo = pro_photo;
    }

    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public String getPro_name() {
        return pro_name;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }

    public String getPro_brand() {
        return pro_brand;
    }

    public void setPro_brand(String pro_brand) {
        this.pro_brand = pro_brand;
    }

    public float getPro_price() {
        return pro_price;
    }

    public void setPro_price(float pro_price) {
        this.pro_price = pro_price;
    }

    public float getPro_discount() {
        return pro_discount;
    }

    public void setPro_discount(float pro_discount) {
        this.pro_discount = pro_discount;
    }

    public String getPro_describe() {
        return pro_describe;
    }

    public void setPro_describe(String pro_describe) {
        this.pro_describe = pro_describe;
    }

    public String getPro_photo() {
        return pro_photo;
    }

    public void setPro_photo(String pro_photo) {
        this.pro_photo = pro_photo;
    }
}
