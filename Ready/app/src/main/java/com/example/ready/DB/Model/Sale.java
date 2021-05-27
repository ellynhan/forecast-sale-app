package com.example.ready.DB.Model;

import android.provider.BaseColumns;

public class Sale {
    public static final String TABLE_NAME = "sales";
    public static final String MENU_ID = "menu_id";
    public static final String QTY = "qty";
    public static final String WEATHER = "weather";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String HOLIDAY = "holiday";

    private Sale() {}

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + MENU_ID + " INTEGER NOT NULL,"
                    + QTY + " INTEGER NOT NULL,"
                    + WEATHER + " INTEGER NOT NULL,"
                    + DATE + " TEXT NOT NULL,"
                    + TIME + " INTEGER NOT NULL,"
                    + HOLIDAY + " INTEGER NOT NULL"
                    + ")";

    public static final String DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}