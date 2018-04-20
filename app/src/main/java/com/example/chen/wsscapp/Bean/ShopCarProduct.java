package com.example.chen.wsscapp.Bean;

import java.io.Serializable;

/**
 * Created by xuxuxiao on 2018/4/19.
 */

public class ShopCarProduct implements Serializable {
    private String pro_id;
    private String pro_name;
    private String pro_brand;
    private String pro_price;
    private String pro_discount;
    private String pro_photo;
    private String pro_num;
    private String pro_size;
    private String pro_color;
    private boolean ischoose;

    public ShopCarProduct() {
    }

    public ShopCarProduct(String pro_id, String pro_name, String pro_brand, String pro_price, String pro_discount, String pro_photo, String pro_num, String pro_size, String pro_color, boolean ischoose) {
        this.pro_id = pro_id;
        this.pro_name = pro_name;
        this.pro_brand = pro_brand;
        this.pro_price = pro_price;
        this.pro_discount = pro_discount;
        this.pro_photo = pro_photo;
        this.pro_num = pro_num;
        this.pro_size = pro_size;
        this.pro_color = pro_color;
        this.ischoose = ischoose;
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

    public String getPro_price() {
        return pro_price;
    }

    public void setPro_price(String pro_price) {
        this.pro_price = pro_price;
    }

    public String getPro_discount() {
        return pro_discount;
    }

    public void setPro_discount(String pro_discount) {
        this.pro_discount = pro_discount;
    }

    public String getPro_photo() {
        return pro_photo;
    }

    public void setPro_photo(String pro_photo) {
        this.pro_photo = pro_photo;
    }

    public String getPro_num() {
        return pro_num;
    }

    public void setPro_num(String pro_num) {
        this.pro_num = pro_num;
    }

    public String getPro_size() {
        return pro_size;
    }

    public void setPro_size(String pro_size) {
        this.pro_size = pro_size;
    }

    public String getPro_color() {
        return pro_color;
    }

    public void setPro_color(String pro_color) {
        this.pro_color = pro_color;
    }

    public boolean isIschoose() {
        return ischoose;
    }

    public void setIschoose(boolean ischoose) {
        this.ischoose = ischoose;
    }

    @Override
    public String toString() {
        return "ShopCarProduct{" +
                "pro_id='" + pro_id + '\'' +
                ", pro_name='" + pro_name + '\'' +
                ", pro_brand='" + pro_brand + '\'' +
                ", pro_price='" + pro_price + '\'' +
                ", pro_discount='" + pro_discount + '\'' +
                ", pro_photo='" + pro_photo + '\'' +
                ", pro_num='" + pro_num + '\'' +
                ", pro_size='" + pro_size + '\'' +
                ", pro_color='" + pro_color + '\'' +
                ", ischoose=" + ischoose +
                '}';
    }
}
