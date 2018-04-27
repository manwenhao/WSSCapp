package com.example.chen.wsscapp.Bean;

import java.io.Serializable;

/**
 * Created by xuxuxiao on 2018/4/26.
 */

public class PushMsg implements Serializable {
    private String title;
    private String content;
    private String time;
    private String status;   //0 -> 未读 1 -> 已读

    public PushMsg() {
    }

    public PushMsg(String title, String content, String time, String status) {
        this.title = title;
        this.content = content;
        this.time = time;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PushMsg{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
