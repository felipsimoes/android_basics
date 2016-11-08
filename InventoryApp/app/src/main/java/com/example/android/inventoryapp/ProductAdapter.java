package com.example.android.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import static android.content.ContentValues.TAG;
import static com.example.android.inventoryapp.data.ProductContract.ProductEntry;

/**
 * Created by Felipe on 04/11/2016.
 */

public class ProductAdapter extends CursorAdapter {

    public ProductAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        final int productId = cursor.getInt(cursor.getColumnIndex(ProductEntry._ID));

        TextView nameTextView = (TextView) view.findViewById(R.id.list_product_name);
        TextView priceTextView = (TextView) view.findViewById(R.id.list_product_price);
        TextView stockTextView = (TextView) view.findViewById(R.id.list_product_stock);
        TextView soldTextView = (TextView) view.findViewById(R.id.list_product_sold);

        int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int stockColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_STOCK);
        int soldColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SALES);

        String productName = cursor.getString(nameColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);
        final int productStock = cursor.getInt(stockColumnIndex);
        final int productSales = cursor.getInt(soldColumnIndex);

        Button orderButton = (Button) view.findViewById(R.id.list_sell_btn);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri productUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, productId);
                makeSale(context, productUri, productStock, productSales);
            }
        });

        nameTextView.setText(productName);
        priceTextView.setText(productPrice);
        stockTextView.setText(String.valueOf(productStock));
        soldTextView.setText(String.valueOf(productSales));
    }

    private void makeSale(Context context, Uri productUri, int productStock, int productSales) {
        int currentCount = productStock;
        int currentSales = productSales;
        int newCount = 0;
        if(currentCount >= 1){
            newCount = currentCount - 1;
            currentSales++;
        }

        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_STOCK, newCount );
        values.put(ProductEntry.COLUMN_PRODUCT_SALES, currentSales );
        int numRowsUpdated = context.getContentResolver().update(productUri, values, null, null);

        if (numRowsUpdated > 0) {
            Log.i(TAG, "Sale was successful");
        } else {
            Log.i(TAG, "Could not sell product");
        }
    }
}
