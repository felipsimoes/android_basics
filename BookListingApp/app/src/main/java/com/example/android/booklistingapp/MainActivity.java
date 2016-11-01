package com.example.android.booklistingapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String BOOKS_API_REQUEST_URL_DEFAULT =
            "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=1";
    private static final String BOOKS_API_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=";
    private String maxResults = "&maxResults=10";
    public static final String BOOKS_ARRAY = "booksArray";
    private Button button;
    private EditText editText;
    private TextView textView;
    private ArrayList<Book> books;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button_search);
        editText = (EditText) findViewById(R.id.search_text);
        textView = (TextView) findViewById(R.id.example);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchForConnection();
            }
        });
    }

    private void searchForConnection() {
        if(isNetworkOn()){
            BooksAsyncTask task = new BooksAsyncTask();
            task.execute();
        }else {
            Toast.makeText(MainActivity.this, "You don't have connection right now",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkOn(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void updateUI(ArrayList<Book> books) {
        Intent intent = new Intent(this, BookListActivity.class);
        intent.putParcelableArrayListExtra(BOOKS_ARRAY, books);
        startActivity(intent);
    }

    private void noDataMessage() {
        textView.setText(R.string.noResult);
    }

    public String getSearchURL() {
        String searchURL = BOOKS_API_REQUEST_URL_DEFAULT;
        if (!TextUtils.isEmpty(editText.getText())){

            String searchTerm = null;
            try {
                searchTerm = URLEncoder.encode(
                        editText.getText().toString().trim(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                Log.e(LOG_TAG,"ERRO DE ENCONDING");
            }
            searchURL = BOOKS_API_REQUEST_URL
                    .concat(searchTerm)
                    .concat(maxResults);
        }
        return searchURL;
    }

    private class BooksAsyncTask extends AsyncTask<URL, Void, ArrayList<Book>>{

        @Override
        protected ArrayList<Book> doInBackground(URL... urls) {
            URL url = createURL(getSearchURL());
            String jsonResponse;
            try {
                jsonResponse = makeHttpResponse(url);
            } catch (IOException e) {
                Log.e(LOG_TAG, "ERRO NO MAKEHTTP RESPONSE");
                return null;
            }

            return extractFeatureFromJson(jsonResponse);
        }

        @Override
        protected void onPostExecute(ArrayList<Book> books) {
            if (books == null){
                noDataMessage();
            }else {
                updateUI(books);
            }
        }

        private ArrayList<Book> extractFeatureFromJson(String jsonResponse) {
            if(TextUtils.isEmpty(jsonResponse)){
                return null;
            }

            try {
                books = new ArrayList<Book>();
                JSONObject baseJsonResp = new JSONObject(jsonResponse);
                if(!baseJsonResp.has("items")){
                    return null;
                }
                JSONArray itemsArray = baseJsonResp.getJSONArray("items");
                JSONObject object;
                JSONObject volumeInfo;
                String title, publisher = "-", authors = "-";
                if (itemsArray.length() > 0 ){
                    for (int i = 0; i < itemsArray.length(); i++) {
                        object = itemsArray.getJSONObject(i);
                        volumeInfo = object.getJSONObject("volumeInfo");
                        title = volumeInfo.getString("title");
                        if(volumeInfo.has("publisher")){
                            publisher = volumeInfo.getString("publisher");
                        }
                        if (volumeInfo.has("authors")){
                            authors = setStringFromJSONArray(volumeInfo.getJSONArray("authors"));
                        }
                        books.add(new Book(title,publisher,authors));
                    }
                    return books;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "PROBLEMA NO PARSE DO JSON", e);
            }
            return null;
        }

        private String setStringFromJSONArray(JSONArray authors) {
            String stringReturn = "";
            try {
                stringReturn = authors.getString(0).toString();
                if(authors.length() > 1){
                    for (int i = 1; i < authors.length(); i++) {
                        stringReturn += ", ".concat(authors.getString(i).toString());
                    }
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "ERRO PARSE ARRAY AUTORES PARA STRING");
            }finally {
                return stringReturn;
            }
        }

        private String makeHttpResponse(URL url) throws IOException {
            String jsonResponse = "";
            if(url == null){
                return jsonResponse;
            }

            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.connect();

                if(urlConnection.getResponseCode() == 200){
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                }else {
                    Log.e(LOG_TAG, "ERRO CODIGO RESPOSTA: "+urlConnection.getResponseCode());
                }
            }catch (IOException e){
                Log.e(LOG_TAG, "PROBLEMA DURANTE REQUEST HTTP", e);

            }finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if (inputStream != null){
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null){
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }
        private URL createURL(String stringURL) {
            URL url = null;
            try {
                url = new URL(stringURL);
            }catch (MalformedURLException e){
                Log.e(LOG_TAG, "ERRO CRIANDO URL", e);
            }finally {
                return url;
            }
        }

    }
}
