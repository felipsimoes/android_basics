package com.example.android.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.android.inventoryapp.data.ProductContract.ProductEntry;

/**
 * Created by Felipe on 06/11/2016.
 */

public class ProductDbHelper extends SQLiteOpenHelper{
    public static final String LOG_TAG = ProductDbHelper.class.getSimpleName();
    public static final String DATABASE_NAME = "inventory.db";
    public static final int DATABASE_VERSION = 1;
    private Context mContext;

    public ProductDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String TEXT_TYPE = " TEXT";
        final String INTEGER_TYPE = " INTEGER";
        final String FLOAT_TYPE = " REAL";
        final String BLOB_TYPE = " BLOB";
        final String COMMA_SEPARATOR = ",";

        String CREATE_TABLE = "CREATE TABLE " + ProductEntry.TABLE_NAME + "("
                + ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ProductEntry.COLUMN_PRODUCT_NAME + TEXT_TYPE + COMMA_SEPARATOR
                + ProductEntry.COLUMN_PRODUCT_IMAGE + BLOB_TYPE + COMMA_SEPARATOR
                + ProductEntry.COLUMN_PRODUCT_PRICE + FLOAT_TYPE + COMMA_SEPARATOR
                + ProductEntry.COLUMN_PRODUCT_STOCK + INTEGER_TYPE + COMMA_SEPARATOR
                + ProductEntry.COLUMN_PRODUCT_SALES + INTEGER_TYPE + " DEFAULT 0 " + COMMA_SEPARATOR
                + ProductEntry.COLUMN_SUPPLIER_CONTACT + TEXT_TYPE + ")";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ProductEntry.TABLE_NAME);
        onCreate(db);
    }



}
