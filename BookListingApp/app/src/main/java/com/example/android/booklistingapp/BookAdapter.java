package com.example.android.booklistingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Felipe on 25/10/2016.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, List<Book> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Book currentBook = getItem(position);

        TextView titleText = (TextView) listItemView.findViewById(R.id.title_text) ;
        TextView authorsText = (TextView) listItemView.findViewById(R.id.authors_text);
        TextView publisherText = (TextView) listItemView.findViewById(R.id.publisher_text);

        titleText.setText(currentBook.getTitle());
        authorsText.setText(currentBook.getAuthors());
        publisherText.setText(currentBook.getPublisher());

        return listItemView;
    }
}
