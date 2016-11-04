package com.example.android.habittrackerapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Felipe on 01/11/2016.
 */

public class HabitContract {

    private HabitContract() {}

    public static final class HabitEntry implements BaseColumns {

        public static final String TABLE_NAME = "habits";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TIMES_IN_WEEK = "times_in_week";

        public static final int TIMES_WEEK_ONE = 1;
        public static final int TIMES_WEEK_TWO = 2;
        public static final int TIMES_WEEK_THREE = 3;
        public static final int TIMES_WEEK_FOUR = 4;
        public static final int TIMES_WEEK_FIVE = 5;
        public static final int TIMES_WEEK_SIX = 6;
        public static final int TIMES_WEEK_SEVEN = 7;

    }
}
