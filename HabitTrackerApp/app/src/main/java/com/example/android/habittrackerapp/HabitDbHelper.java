package com.example.android.habittrackerapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static com.example.android.habittrackerapp.HabitContract.HabitEntry;

/**
 * Created by Felipe on 01/11/2016.
 */

public class HabitDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "habits.db";
    public static final int DATABASE_VERSION = 1;

    public HabitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_HABITS_TABLE =  "CREATE TABLE " + HabitEntry.TABLE_NAME + " ("
                + HabitEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + HabitEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + HabitEntry.COLUMN_TIMES_IN_WEEK + " INTEGER NOT NULL DEFAULT 0);";

        db.execSQL(SQL_CREATE_HABITS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertHabit(String name, int timesWeek){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(HabitEntry.COLUMN_NAME, name);
        values.put(HabitEntry.COLUMN_TIMES_IN_WEEK, timesWeek);

        long insertedId = db.insert(HabitEntry.TABLE_NAME, null, values);

        return insertedId;
    }

    public Cursor selectHabits(){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                HabitEntry._ID,
                HabitEntry.COLUMN_NAME,
                HabitEntry.COLUMN_TIMES_IN_WEEK
        };

        Cursor cursor = db.query(HabitEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        return cursor;
    }
}