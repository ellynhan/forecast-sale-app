package com.example.ready.DB.Model;

import android.provider.BaseColumns;

public class Sale {
    public static final String TABLE_NAME = "sales";
    public static final String ID = "_id";
    public static final String MENU_ID = "menu_id";
    public static final String QTY = "qty";
    public static final String SKY = "sky";
    public static final String RAIN = "rain";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String HOLIDAY = "holiday";

    public int _id;
    public int menu_id;
    public int qty;
    public int sky;
    public int rain;
    public String date;
    public Boolean time;
    public Boolean holiday;


    public Sale() {}

    public Sale(int _id, int menu_id, int qty, String date, int sky, int rain, Boolean time) {
        this._id = _id;
        this.menu_id = menu_id;
        this.qty = qty;
        this.date = date;
        this.sky = sky;
        this.rain = rain;
        this.time = time;
    }

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                    + MENU_ID + " INTEGER NOT NULL,"
                    + QTY + " INTEGER NOT NULL,"
                    + SKY + " INTEGER,"
                    + RAIN + " INTEGER,"
                    + DATE + " TEXT NOT NULL,"
                    + TIME + " INTEGER NOT NULL,"
                    + HOLIDAY + " INTEGER NOT NULL"
                    + ")";

    public static final String DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public void setId(int _id) { this._id = _id; }
    public void setMenuId(int menu_id) { this.menu_id = menu_id; }
    public void setSaleQty(int qty) { this.qty = qty; }
//    public void setSaleWeather(int weather) { this.weather = weather; }
    public void setSaleDate(String date) { this.date = date; }
    public void setSaleTime(Boolean time) { this.time = time; }
    public void setSaleHoliday(Boolean holiday) { this.holiday = holiday; }
}