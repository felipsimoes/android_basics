package com.example.android.newsapp;

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

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, List<News> objects) {
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

        News currentNews = getItem(position);
        Resources res = getContext().getResources();
        holder.titleText.setText(res.getString(R.string.titleLayout, currentNews.getWebTitle()));
        holder.sectionText.setText(res.getString(R.string.sectionLayout, currentNews.getSectionName()));

        return listItemView;
    }

    private void createHolder(ViewHolder holder, View view) {
        holder.titleText = (TextView) view.findViewById(R.id.title_text) ;
        holder.sectionText = (TextView) view.findViewById(R.id.section_text);
        view.setTag(holder);
    }

    static class ViewHolder {
        TextView titleText;
        TextView sectionText;
    }

}
