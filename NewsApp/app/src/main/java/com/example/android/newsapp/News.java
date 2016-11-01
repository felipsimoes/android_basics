package com.example.android.newsapp;

/**
 * Created by Felipe on 31/10/2016.
 */

public class News {

    private String webTitle;
    private String webUrl;
    private String sectionName;

    public News(String title, String url, String section) {
        webTitle = title;
        webUrl = url;
        sectionName = section;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public void setWebTitle(String webTitle) {
        this.webTitle = webTitle;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }
}
