package com.example.ready.DB.Model;

public class Menu {
    public static final String TABLE_NAME = "menus";
    public static final String MENU_ID = "menu_id";
    public static final String MENU_NAME = "menu_name";
    public static final String MENU_PRICE = "menu_price";

    public int _id;
//    public int menu_id;
    public String menu_name;
    public int menu_price;

    public Menu() {}

    public Menu(String menu_name, int menu_price) {
//        this.menu_id = menu_id;
        this.menu_name = menu_name;
        this.menu_price = menu_price;
    }

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
            + "_id INTEGER PRIMARY KEY NOT NULL,"
//            + MENU_ID + " INTEGER NOT NULL,"
            + MENU_NAME + " TEXT NOT NULL,"
            + MENU_PRICE + " TEXT NOT NULL"
            + ")";

    public static final String DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public void setId(int _id) { this._id = _id; }
//    public void setMenuId(int menu_id) { this.menu_id = menu_id; }
    public void setMenuName(String menu_name) { this.menu_name = menu_name; }
    public void setMenuPrice(int menu_price) { this.menu_price = menu_price; }
}
