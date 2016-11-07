package com.example.android.inventoryapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.inventoryapp.Product;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.inventoryapp.data.ProductContract.ProductEntry;

/**
 * Created by Felipe on 06/11/2016.
 */

public class ProductDbHelper extends SQLiteOpenHelper{
    private Context mContext;
    public static final String LOG_TAG = ProductDbHelper.class.getSimpleName();
    public static final String DATABASE_NAME = "inventory.db";
    public static final int DATABASE_VERSION = 1;

    public ProductDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String TEXT_TYPE = " TEXT";
        final String COMMA_SEPARATOR = ",";
        final String INTEGER_TYPE = " INTEGER";
        final String FLOAT_TYPE = " REAL";

        String CREATE_TABLE = "CREATE TABLE " + ProductEntry.TABLE_NAME + "("
                + ProductEntry._ID + " INTEGER PRIMARY KEY,"
                + ProductEntry.COLUMN_PRODUCT_NAME + TEXT_TYPE + COMMA_SEPARATOR
                + ProductEntry.COLUMN_PRODUCT_IMAGE + TEXT_TYPE + COMMA_SEPARATOR
                + ProductEntry.COLUMN_PRODUCT_PRICE + FLOAT_TYPE + COMMA_SEPARATOR
                + ProductEntry.COLUMN_PRODUCT_STOCK + INTEGER_TYPE + COMMA_SEPARATOR
                + ProductEntry.COLUMN_PRODUCT_SALES + INTEGER_TYPE + COMMA_SEPARATOR
                + ProductEntry.COLUMN_SUPPLIER_CONTACT + TEXT_TYPE + ")";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ProductEntry.TABLE_NAME);
        onCreate(db);
    }

    public void addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ProductEntry.COLUMN_PRODUCT_NAME, product.getName());
        values.put(ProductEntry.COLUMN_PRODUCT_IMAGE, product.getImage());
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, product.getPrice());
        values.put(ProductEntry.COLUMN_PRODUCT_STOCK, product.getStock());
        values.put(ProductEntry.COLUMN_PRODUCT_SALES, product.getSales());
        values.put(ProductEntry.COLUMN_SUPPLIER_CONTACT, product.getSupplier());

        db.insert(ProductEntry.TABLE_NAME, null, values);
        db.close();
    }

    public Cursor getProduct(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_IMAGE,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_STOCK,
                ProductEntry.COLUMN_PRODUCT_SALES,
                ProductEntry.COLUMN_SUPPLIER_CONTACT
        };

        Cursor cursor = db.query(ProductEntry.TABLE_NAME,
                projection,
                ProductEntry._ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        if (cursor != null) cursor.moveToFirst();

        return cursor;
    }

    public List<Product> getAllProducts() {
        List<Product> productsList = new ArrayList<Product>();
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + ProductEntry.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(Integer.parseInt(cursor.getString(0)));
                product.setName(cursor.getString(1));
                product.setImage(cursor.getString(2));
                product.setPrice(cursor.getFloat(3));
                product.setStock(Integer.parseInt(cursor.getString(4)));
                product.setSales(Integer.parseInt(cursor.getString(5)));
                product.setSupplier(cursor.getString(6));

                productsList.add(product);

            } while (cursor.moveToNext());
        }

        return productsList;
    }

    public int getProductsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT * FROM " + ProductEntry.TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public int updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ProductEntry.COLUMN_PRODUCT_STOCK, product.getStock());
        values.put(ProductEntry.COLUMN_PRODUCT_SALES, product.getSales());

        return db.update(ProductEntry.TABLE_NAME,
                values,
                ProductEntry._ID + " = ?",
                new String[]{String.valueOf(product.getId())});
    }

    public void deleteProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(ProductEntry.TABLE_NAME,
                ProductEntry._ID + " = ?",
                new String[]{String.valueOf(product.getId())});
        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(ProductEntry.TABLE_NAME, null, null);
        db.execSQL("DELETE FROM " + ProductContract.ProductEntry.TABLE_NAME);

        db.close();
    }

    public void deleteDatabase() {
        mContext.deleteDatabase(DATABASE_NAME);
    }
}
