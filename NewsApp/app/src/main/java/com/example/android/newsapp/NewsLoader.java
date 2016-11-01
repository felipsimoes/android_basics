package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Felipe on 31/10/2016.
 */

public class NewsLoader extends AsyncTaskLoader {

    private String mUrl;

    private static final String LOG_TAG = NewsLoader.class.getName();

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.e(LOG_TAG, "ON START LOADING");
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        Log.i(LOG_TAG, "LOAD IN BACKGROUND");
        if(mUrl == null){
            return null;
        }
        List<News> newsList = QueryUtils.fetchNewsData(mUrl);
        return newsList;
    }
}
