package com.example.android.booklistingapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Felipe on 25/10/2016.
 */

public class Book implements Parcelable {

    private String title;
    private String publisher;
    private String authors;

    public Book(String t, String p, String a){
        title = t;
        publisher = p;
        authors = a;
    }

    public Book(Parcel in) {
        title = in.readString();
        publisher = in.readString();
        authors = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(publisher);
        dest.writeString(authors);
    }

    static final Parcelable.Creator<Book> CREATOR
            = new Parcelable.Creator<Book>() {

        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
