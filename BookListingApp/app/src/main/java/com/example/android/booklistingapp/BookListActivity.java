package com.example.android.booklistingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Felipe on 25/10/2016.
 */

public class BookListActivity extends AppCompatActivity {

    private BookAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);

        Intent intent = getIntent();
//        ArrayList<Book> books = intent.getExtras().getParcelable(MainActivity.BOOKS_ARRAY);
        ArrayList<Book> books = (ArrayList<Book>) intent.getSerializableExtra(MainActivity.BOOKS_ARRAY);
        adapter = new BookAdapter(this, books);
        adapter.addAll(books);

        listView = (ListView) findViewById(R.id.book_list);
        listView.setAdapter(adapter);
    }
}
