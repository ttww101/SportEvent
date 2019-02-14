package com.yaming.android.sportsevent;

public class SeasonPage {
    private int season;
    private int pageNum;
    public SeasonPage() {
        this.season = 0;
        this.pageNum = 0;
    }
    public SeasonPage(int season, int pageNum) {
        this.season = season;
        this.pageNum = pageNum;
    }
    public int getSeason() {
        return season;
    }
    public void setSeason(int season) {
        this.season = season;
    }
    public int getPageNum() {
        return pageNum;
    }
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}
