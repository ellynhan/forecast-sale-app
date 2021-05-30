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

        Cursor cursor = db.rawQuery("SELECT * FROM menus", null);
        if(cursor.moveToFirst()) {
            do {
                Menu menu = new Menu();

                menu.setId(cursor.getInt(cursor.getColumnIndex("_id")));
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

    public void deleteMenu(ArrayList<Integer> deleteIndex) {
        SQLiteDatabase db = this.getWritableDatabase();

        for(int i : deleteIndex) {
            if(i != 0)
                db.execSQL("DELETE FROM menus WHERE _id =" + i);
        }

        db.close();
    }

    public ArrayList<Sale> getSale(String date) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Sale> sales = new ArrayList<>();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM sales WHERE date = ?",
                new String[] { date }
        );
        if(cursor.moveToFirst()) {
            do {
                Sale sale = new Sale();

                sale.setMenuId(cursor.getInt(cursor.getColumnIndex(Sale.MENU_ID)));
                sale.setSaleQty(cursor.getInt(cursor.getColumnIndex(Sale.QTY)));
//                sale.setSaleWeather(cursor.getInt(cursor.getColumnIndex(Sale.WEATHER)));
                sale.setSaleDate(cursor.getString(cursor.getColumnIndex(Sale.DATE)));
                sale.setSaleTime(cursor.getInt(cursor.getColumnIndex(Sale.TIME)) > 0);
                sale.setSaleHoliday(cursor.getInt(cursor.getColumnIndex(Sale.HOLIDAY)) > 0);

                sales.add(sale);
            } while(cursor.moveToNext());
        }
        cursor.close();

        return sales;
    }

    // 보고 있는 달만 추후 수정 필요
    public ArrayList<String> getSaleDate() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> dates = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT date FROM sales", null);
        if(cursor.moveToFirst()) {
            do {
                String date;
                date = cursor.getString(cursor.getColumnIndex(Sale.DATE));

                if(!dates.contains(date))
                    dates.add(date);
            } while(cursor.moveToNext());
        }
        cursor.close();

        return dates;
    }

    public void insertSale(Sale sale) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Sale.MENU_ID, sale.menu_id);
        values.put(Sale.QTY, sale.qty);
        values.put(Sale.WEATHER, sale.weather);
        values.put(Sale.DATE, sale.date);
        values.put(Sale.TIME, sale.time);
        values.put(Sale.HOLIDAY, sale.holiday);

        db.insert(Sale.TABLE_NAME, null, values);
        db.close();
    }

    public void updateSale(Sale sale) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Sale.QTY, sale.qty);
        values.put(Sale.TIME, sale.time);

        db.update(Sale.TABLE_NAME, values, "menu_id = ? AND date = ?", new String[] { String.valueOf(sale.menu_id), sale.date });
        db.close();
    }
}
