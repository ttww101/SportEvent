package com.yaming.android.sportsevent;

public class NewsObject {
    private int sportId,eventId;
    private String newsId,newsDatetime,newsSource,newsUrl,sportName,eventName,title,content;
    public NewsObject() {
        this.sportId = 0;
        this.eventId = 0;
        this.newsId = "";
        this.newsDatetime = "";
        this.newsSource = "";
        this.newsUrl = "";
        this.sportName = "";
        this.eventName = "";
        this.title = "";
        this.content = "";
    }
    public int getSportId() {
        return sportId;
    }
    public void setSportId(int sportId) {
        this.sportId = sportId;
    }
    public int getEventId() {
        return eventId;
    }
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
    public String getNewsId() {
        return newsId;
    }
    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }
    public String getNewsDatetime() {
        return newsDatetime;
    }
    public void setNewsDatetime(String newsDatetime) {
        this.newsDatetime = newsDatetime;
    }
    public String getNewsSource() {
        return newsSource;
    }
    public void setNewsSource(String newsSource) {
        this.newsSource = newsSource;
    }
    public String getNewsUrl() {
        return newsUrl;
    }
    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }
    public String getSportName() {
        return sportName;
    }
    public void setSportName(String sportName) {
        this.sportName = sportName;
    }
    public String getEventName() {
        return eventName;
    }
    public void setEventName(String eventName) {
        this.eventName = eventName;
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
}
