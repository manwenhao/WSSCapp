package com.example.chen.wsscapp.Bean;

/**
 * Created by chen on 2018/3/1.
 */

public class Items {
    private String text;
    private int imageId;

    public Items(String text, int imageId) {
        this.text = text;
        this.imageId = imageId;
    }

    public String getText() {
        return text;
    }

    public int getImageId() {
        return imageId;
    }
}
