package com.yaming.android.sportsevent;

public class MainObject {
    private String title,contentUrl;
    public MainObject() {
        this.title = "";
        this.contentUrl = "";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }
}
