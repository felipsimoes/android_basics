package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felipe on 25/10/2016.
 */

public class NewsListActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<News>>{

    private static final String LOG_TAG = NewsListActivity.class.getSimpleName();
    private static final int NEWS_LIST_LOADER_ID = 1;
    private static final String NEWS_API_REQUEST_URL_DEFAULT =
            "http://content.guardianapis.com/search?q=debates&api-key=test";
    private NewsAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_list);

        ListView newsList = (ListView) findViewById(R.id.news_list);
        adapter = new NewsAdapter(this, new ArrayList<News>());

        newsList.setAdapter(adapter);

        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News currentNews = adapter.getItem(position);

                Uri newsUri = Uri.parse(currentNews.getWebUrl());

                Intent webUrl = new Intent(Intent.ACTION_VIEW, newsUri);

                startActivity(webUrl);
            }
        });

        searchForConnection();
    }

    private void searchForConnection() {
        if(isNetworkOn()){
            Log.i(LOG_TAG, "Network is on");

            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(NEWS_LIST_LOADER_ID, null, this);

            listView = (ListView) findViewById(R.id.news_list);
            listView.setAdapter(adapter);
        }else {
            Toast.makeText(NewsListActivity.this, "You don't have connection right now",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkOn(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "ON CREATE LOADER");

        return new NewsLoader(this, NEWS_API_REQUEST_URL_DEFAULT);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        Log.i(LOG_TAG, "ON LOAD FINISHED");
        adapter.clear();

        if(data != null && !data.isEmpty()){
            adapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        Log.i(LOG_TAG, "ON LOADER RESET");
        adapter.clear();
    }
}
