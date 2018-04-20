package com.example.chen.wsscapp.Bean;

import java.io.Serializable;

/**
 * Created by xuxuxiao on 2018/4/16.
 */

public class WeiTaoBean implements Serializable {
    private String shop_mster;
    private String mster_name;
    private String mster_touxiang;
    private String uptime;
    private String dyn_content;
    private String dyn_photots;

    public WeiTaoBean() {
    }

    public WeiTaoBean(String shop_mster, String mster_name, String mster_touxiang, String uptime, String dyn_content, String dyn_photots) {
        this.shop_mster = shop_mster;
        this.mster_name = mster_name;
        this.mster_touxiang = mster_touxiang;
        this.uptime = uptime;
        this.dyn_content = dyn_content;
        this.dyn_photots = dyn_photots;
    }

    public String getShop_mster() {
        return shop_mster;
    }

    public void setShop_mster(String shop_mster) {
        this.shop_mster = shop_mster;
    }

    public String getMster_name() {
        return mster_name;
    }

    public void setMster_name(String mster_name) {
        this.mster_name = mster_name;
    }

    public String getMster_touxiang() {
        return mster_touxiang;
    }

    public void setMster_touxiang(String mster_touxiang) {
        this.mster_touxiang = mster_touxiang;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getDyn_content() {
        return dyn_content;
    }

    public void setDyn_content(String dyn_content) {
        this.dyn_content = dyn_content;
    }

    public String getDyn_photots() {
        return dyn_photots;
    }

    public void setDyn_photots(String dyn_photots) {
        this.dyn_photots = dyn_photots;
    }

    @Override
    public String toString() {
        return "WeiTaoBean{" +
                "shop_mster='" + shop_mster + '\'' +
                ", mster_name='" + mster_name + '\'' +
                ", mster_touxiang='" + mster_touxiang + '\'' +
                ", uptime='" + uptime + '\'' +
                ", dyn_content='" + dyn_content + '\'' +
                ", dyn_photots='" + dyn_photots + '\'' +
                '}';
    }
}
