package com.example.ready.DB.Model;

import android.provider.BaseColumns;

public class Menu {
    private Menu() {}

    public static class MenuEntry implements BaseColumns {
        public static final String TABLE_NAME = "menu";
        public static final String MENUID = "menu_id";
        public static final String PRICE = "price";
    }
}
