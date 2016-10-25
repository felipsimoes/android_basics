package com.example.android.booklistingapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.R.id.edit;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String BOOKS_API_REQUEST_URL_DEFAULT =
            "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=1";
    private static final String BOOKS_API_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=";
    private String maxResults = "&maxResults=1";
    private BooksAsyncTask task;
    private Button button;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button_search);
        editText = (EditText) findViewById(R.id.search_text);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task = new BooksAsyncTask();
                task.execute();
                task = null;
            }
        });
    }

    private void updateUI(EventBook eventBook) {
        TextView textView = (TextView) findViewById(R.id.example);
        textView.setText(eventBook.getTitle());
    }

    public String getSearchURL() {
        String searchURL = BOOKS_API_REQUEST_URL_DEFAULT;
        if (!TextUtils.isEmpty(editText.getText())){
            searchURL = BOOKS_API_REQUEST_URL
                    .concat(editText.getText().toString())
                    .concat(maxResults);
        }
        return searchURL;
    }

    private class BooksAsyncTask extends AsyncTask<URL, Void, EventBook>{

        private String URL;

        @Override
        protected EventBook doInBackground(URL... urls) {
            URL url = createURL(getSearchURL());
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpResponse(url);
            } catch (IOException e) {
                Log.e(LOG_TAG, "ERRO NO MAKEHTTP RESPONSE");
                return null;
            }

            EventBook book = extractFeatureFromJson(jsonResponse);

            return book;
        }

        @Override
        protected void onPostExecute(EventBook eventBook) {
            if (eventBook == null){
                return;
            }

            updateUI(eventBook);
        }

        private EventBook extractFeatureFromJson(String jsonResponse) {
            if(TextUtils.isEmpty(jsonResponse)){
                return null;
            }

            try {
                JSONObject baseJsonResp = new JSONObject(jsonResponse);
                JSONArray itemsArray = baseJsonResp.getJSONArray("items");

                if (itemsArray.length() > 0 ){
                    JSONObject object = itemsArray.getJSONObject(0);
                    JSONObject volumeInfo = object.getJSONObject("volumeInfo");
                    String title = volumeInfo.getString("title");
                    String publisher = volumeInfo.getString("publisher");
                    return new EventBook(title,publisher);
                }else{
                    return new EventBook("Sem resultados","Sem resultados");
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "PROBLEMA NO PARSE DO JSON", e);
            }
            return null;
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
