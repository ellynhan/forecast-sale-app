package com.example.ready.DB.Model;

import android.provider.BaseColumns;

public class Sale {
    private Sale() {}

    public static class SaleEntry implements BaseColumns {
        public static final String TABLE_NAME = "sale";
        public static final String MENUID = "menu_id";
        public static final String QTY = "qty";
        public static final String WEATHER = "weather";
        public static final String DATE = "date";
        public static final String TIME = "time";
        public static final String HOLIDAY = "holiday";
    }
}
