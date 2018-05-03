package com.example.chen.wsscapp.Bean;

/**
 * Created by chen on 2018/4/30.
 */




public class Order {


    private String ord_id;
    private String ord_time;
    private String ord_money;
    private String ord_products;
    private String ord_status;
    private String ord_expressname;
    private String ord_expressid;
    private String rev_name;
    private String rev_phone;
    private String rev_address;


    public Order(){
        super();
    }


    public Order(String ord_id, String ord_time, String ord_money, String ord_products, String ord_status, String ord_expressname, String ord_expressid, String rev_name, String rev_phone, String rev_address) {
        this.ord_id = ord_id;
        this.ord_time = ord_time;
        this.ord_money = ord_money;
        this.ord_products = ord_products;
        this.ord_status = ord_status;
        this.ord_expressname = ord_expressname;
        this.ord_expressid = ord_expressid;
        this.rev_name = rev_name;
        this.rev_phone = rev_phone;
        this.rev_address = rev_address;
    }

    public String getOrd_id() {
        return ord_id;
    }

    public void setOrd_id(String ord_id) {
        this.ord_id = ord_id;
    }

    public String getOrd_time() {
        return ord_time;
    }

    public void setOrd_time(String ord_time) {
        this.ord_time = ord_time;
    }

    public String getOrd_money() {
        return ord_money;
    }

    public void setOrd_money(String ord_money) {
        this.ord_money = ord_money;
    }

    public String getOrd_products() {
        return ord_products;
    }

    public void setOrd_products(String ord_products) {
        this.ord_products = ord_products;
    }

    public String getOrd_status() {
        return ord_status;
    }

    public void setOrd_status(String ord_status) {
        this.ord_status = ord_status;
    }

    public String getOrd_expressname() {
        return ord_expressname;
    }

    public void setOrd_expressname(String ord_expressname) {
        this.ord_expressname = ord_expressname;
    }

    public String getOrd_expressid() {
        return ord_expressid;
    }

    public void setOrd_expressid(String ord_expressid) {
        this.ord_expressid = ord_expressid;
    }

    public String getRev_name() {
        return rev_name;
    }

    public void setRev_name(String rev_name) {
        this.rev_name = rev_name;
    }

    public String getRev_phone() {
        return rev_phone;
    }

    public void setRev_phone(String rev_phone) {
        this.rev_phone = rev_phone;
    }

    public String getRev_address() {
        return rev_address;
    }

    public void setRev_address(String rev_address) {
        this.rev_address = rev_address;
    }
}