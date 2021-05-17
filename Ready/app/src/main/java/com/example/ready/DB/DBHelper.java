package com.example.ready.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.ready.DB.Model.Menu;
import com.example.ready.DB.Model.Sale;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "test.db";
    private static DBHelper instance;

    public static synchronized DBHelper getInstance(Context context) {
        if(instance == null)
            instance = new DBHelper(context.getApplicationContext());
        return instance;
    }
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Menu.CREATE_TABLE);
        db.execSQL(Sale.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Menu.DROP_TABLE);
        db.execSQL(Sale.DROP_TABLE);

        onCreate(db);
    }

    public ArrayList<Menu> getMenu() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Menu> menus = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM menus ORDER BY _id DESC", null);
        if(cursor.moveToFirst()) {
            do {
                Menu menu = new Menu();

                menu.setMenuId(cursor.getInt(cursor.getColumnIndex(Menu.MENU_ID)));
                menu.setMenuName(cursor.getString(cursor.getColumnIndex(Menu.MENU_NAME)));
                menu.setMenuPrice(cursor.getInt(cursor.getColumnIndex(Menu.MENU_PRICE)));

                menus.add(menu);
            } while(cursor.moveToNext());
        }
        cursor.close();

        return menus;
    }

    public void insertMenu(Menu menu) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Menu.MENU_ID, menu.menu_id);
        values.put(Menu.MENU_NAME, menu.menu_name);
        values.put(Menu.MENU_PRICE, menu.menu_price);

        db.insert(Menu.TABLE_NAME, null, values);
        db.close();
    }

    public void deleteMenu(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM menus WHERE _id =" + id);
    }
}
