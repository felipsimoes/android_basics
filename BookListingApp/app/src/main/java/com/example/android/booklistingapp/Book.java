package com.example.android.booklistingapp;

import java.io.Serializable;

/**
 * Created by Felipe on 25/10/2016.
 */

public class Book implements Serializable {

    private String title;
    private String publisher;
    private String authors;

    public Book(String t, String p, String a){
        title = t;
        publisher = p;
        authors = a;
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

    public String getAuthors() { return authors; }

    public void setAuthors(String authors) { this.authors = authors; }
}
