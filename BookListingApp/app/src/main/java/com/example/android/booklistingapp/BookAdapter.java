package com.example.android.booklistingapp;

import android.content.Context;
import android.content.res.Resources;
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

        ViewHolder holder = null;
        View listItemView = convertView;

        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
            holder = new ViewHolder();
            createHolder(holder,listItemView);
        } else {
            holder = (ViewHolder) listItemView.getTag();
        }

        Book currentBook = getItem(position);
        Resources res = getContext().getResources();
        holder.titleText.setText(res.getString(R.string.titleLayout, currentBook.getTitle()));
        holder.authorsText.setText(res.getString(R.string.authorsLayout,currentBook.getAuthors()));
        holder.publisherText.setText(res.getString(R.string.publisherLayout,currentBook.getPublisher()));

        return listItemView;
    }

    private void createHolder(ViewHolder holder, View view) {
        holder.titleText = (TextView) view.findViewById(R.id.title_text) ;
        holder.authorsText = (TextView) view.findViewById(R.id.authors_text);
        holder.publisherText = (TextView) view.findViewById(R.id.publisher_text);
        view.setTag(holder);
    }

    static class ViewHolder {

        TextView titleText;
        TextView authorsText;
        TextView publisherText;

    }

}
