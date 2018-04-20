package com.example.chen.wsscapp.Bean;

/**
 * Created by chen on 2018/4/13.
 */

public class HomeItem {
    private ItemType itemType;
    private Ad ad;
    private MenuPo[] menuPos;


    public ItemType getItemType(){
        return itemType;
    }


    public void setItemType(ItemType itemType){
        this.itemType=itemType;
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    public MenuPo[] getMenuPos() {
        return menuPos;
    }

    public void setMenuPos(MenuPo[] menuPos) {
        this.menuPos = menuPos;
    }
}
