package com.example.fangxq.myapplication.bean;

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;


/**
 * Created by Fangxq on 2018/5/22.
 */

public class MessageNotificationItem implements Serializable {

    @SerializedName("category")
    private String category;
    @SerializedName("title")
    private String title;
    @SerializedName("content")
    private String content;
    @SerializedName("jumpUrl")
    private String jumpUrl;
    @SerializedName("showed")
    private boolean showed;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public boolean isShowed() {
        return showed;
    }

    public void setShowed(boolean showed) {
        this.showed = showed;
    }
}
