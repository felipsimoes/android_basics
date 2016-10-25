package com.example.android.booklistingapp;

/**
 * Created by Felipe on 25/10/2016.
 */

public class EventBook {

    private String title;
    private String publisher;

    public EventBook(String t, String p){
        title = t;
        publisher = p;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
