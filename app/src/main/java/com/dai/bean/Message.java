package com.dai.bean;

import java.util.Date;

/**
 * Created by dai on 2017/4/19.
 */

public class Message {

    private Date date;
    private String name;
    private String message;
    private boolean isComing;
    private int headIcon;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isComing() {
        return isComing;
    }

    public void setComing(boolean coming) {
        isComing = coming;
    }

    public int getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(int headIcon) {
        this.headIcon = headIcon;
    }
}
