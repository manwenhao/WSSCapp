package com.example.chen.wsscapp.Bean;

/**
 * Created by chen on 2018/4/7.
 * 此pojo类为根据pro-id返回json to bean
 * pro_id 商品编号
 * pro_name  商品名称
 * pro_classify 商品分类名称
 * pro_suitperson  适用人群
 * pro_material  主材料
 * pro_brand  品牌
 * pro_size 包含尺寸
 * pro_color 包含颜色
 * pro_price 总价
 * pro_discount 打折率
 * pro_describe 产品说明
 * pro_photo 产品图片
 * pro_describephoto 产品宣传图片
 */

public class GetProduct {
    private String pro_id;
    private String pro_name;
    private String pro_classify;
    private String pro_suitperson;
    private String pro_material;
    private String pro_brand;
    private String pro_size;
    private String pro_color;
    private String pro_price;
    private String pro_discount;
    private String pro_describe;
    private String pro_photo;
    private String pro_describephoto;

    public GetProduct() {
        super();
    }

    public GetProduct(String pro_id, String pro_name, String pro_classify, String pro_suitperson, String pro_material,
                      String pro_brand, String pro_size, String pro_color, String pro_price, String pro_discount,
                      String pro_describe, String pro_photo, String pro_describephoto) {
        this.pro_id = pro_id;
        this.pro_name = pro_name;
        this.pro_classify = pro_classify;
        this.pro_suitperson = pro_suitperson;
        this.pro_material = pro_material;
        this.pro_brand = pro_brand;
        this.pro_size = pro_size;
        this.pro_color = pro_color;
        this.pro_price = pro_price;
        this.pro_discount = pro_discount;
        this.pro_describe = pro_describe;
        this.pro_photo = pro_photo;
        this.pro_describephoto = pro_describephoto;
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

    public String getPro_classify() {
        return pro_classify;
    }

    public void setPro_classify(String pro_classify) {
        this.pro_classify = pro_classify;
    }

    public String getPro_suitperson() {
        return pro_suitperson;
    }

    public void setPro_suitperson(String pro_suitperson) {
        this.pro_suitperson = pro_suitperson;
    }

    public String getPro_material() {
        return pro_material;
    }

    public void setPro_material(String pro_material) {
        this.pro_material = pro_material;
    }

    public String getPro_brand() {
        return pro_brand;
    }

    public void setPro_brand(String pro_brand) {
        this.pro_brand = pro_brand;
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

    public String getPro_describephoto() {
        return pro_describephoto;
    }

    public void setPro_describephoto(String pro_describephoto) {
        this.pro_describephoto = pro_describephoto;
    }
}
