package com.example.ready.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.ready.DB.Model.Menu;
import com.example.ready.DB.Model.Sale;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

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

        dbSeedTest(db);
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

        values.put(Menu.MENU_NAME, menu.menu_name);
        values.put(Menu.MENU_PRICE, menu.menu_price);

        db.insert(Menu.TABLE_NAME, null, values);
        db.close();
    }

    public void deleteMenu(ArrayList<Integer> deleteIndex) {
        SQLiteDatabase db = this.getWritableDatabase();

        for(int i : deleteIndex) {
            if(i != 0) {
                db.execSQL("DELETE FROM menus WHERE _id =" + i);
                db.execSQL("DELETE FROM sales WHERE menu_id =" + i);
            }
        }

        db.close();
    }

    public ArrayList<Sale> getSaleWithDate(String date) {
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
                sale.setSaleSky(cursor.getInt(cursor.getColumnIndex(Sale.SKY)));
                sale.setSaleRain(cursor.getInt(cursor.getColumnIndex(Sale.RAIN)));
                sale.setSaleDate(cursor.getString(cursor.getColumnIndex(Sale.DATE)));
                sale.setSaleTime(cursor.getInt(cursor.getColumnIndex(Sale.TIME)) > 0);
                sale.setSaleHoliday(cursor.getInt(cursor.getColumnIndex(Sale.HOLIDAY)) > 0);
                sales.add(sale);
            } while(cursor.moveToNext());
        }
        cursor.close();
        return sales;
    }

    // ?????? ?????? ?????? ?????? ?????? ??????
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

    public ArrayList<Sale> getAllSaleWithId(int menu_id) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Sale> sales = new ArrayList<>();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM sales WHERE menu_id = ?",
                new String[] { String.valueOf(menu_id) });
        if(cursor.moveToFirst()) {
            do {
                Sale sale = new Sale();

                sale.setMenuId(cursor.getInt(cursor.getColumnIndex(Sale.MENU_ID)));
                sale.setSaleQty(cursor.getInt(cursor.getColumnIndex(Sale.QTY)));
//                sale.setSaleWeather(cursor.getInt(cursor.getColumnIndex(Sale.WEATHER)));
                sale.setSaleDate(cursor.getString(cursor.getColumnIndex(Sale.DATE)));
                sale.setSaleDay(cursor.getInt(cursor.getColumnIndex(Sale.DAY)));
                sale.setSaleTime(cursor.getInt(cursor.getColumnIndex(Sale.TIME)) > 0);
                sale.setSaleHoliday(cursor.getInt(cursor.getColumnIndex(Sale.HOLIDAY)) > 0);
                sales.add(sale);
            } while(cursor.moveToNext());
        }
        cursor.close();

        return sales;
    }

    // SecondPage??? ?????? ????????? ????????????
    public ArrayList<String> getSaleDateWithId(int menu_id) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> dates = new ArrayList<>();

        Cursor cursor = db.rawQuery(
                "SELECT date, qty FROM sales WHERE menu_id = ?",
                new String[] { String.valueOf(menu_id) });
        if(cursor.moveToFirst()) {
            do {
                String date;
                date = cursor.getString(cursor.getColumnIndex(Sale.DATE));
                dates.add(date);
            } while(cursor.moveToNext());
        }

        return dates;
    }

    public ArrayList<Integer> getSaleQtySkyWithIdAndDate(int menu_id, String date) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Integer> sale = new ArrayList<>();

        Cursor cursor = db.rawQuery(
                "SELECT qty, sky FROM sales WHERE menu_id = ? AND date = ?",
                new String[] { String.valueOf(menu_id), date });

        if(cursor.moveToFirst()) {
            sale.add(cursor.getInt(cursor.getColumnIndex(Sale.QTY)));
            sale.add(cursor.getInt(cursor.getColumnIndex(Sale.SKY)));
        }

        return sale;
    }

    public void insertSale(Sale sale) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Sale.MENU_ID, sale.menu_id);
        values.put(Sale.QTY, sale.qty);
        values.put(Sale.SKY, sale.sky);
        values.put(Sale.RAIN, sale.rain);
        values.put(Sale.DATE, sale.date);
        values.put(Sale.TIME, sale.time);
        values.put(Sale.HOLIDAY, sale.holiday);

        db.insertWithOnConflict(Sale.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void updateSale(Sale sale) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Sale.ID, sale._id);
        values.put(Sale.MENU_ID, sale.menu_id);
        values.put(Sale.QTY, sale.qty);
        values.put(Sale.SKY, sale.sky);
        values.put(Sale.RAIN, sale.rain);
        values.put(Sale.DATE, sale.date);
        values.put(Sale.TIME, sale.time);
        values.put(Sale.HOLIDAY, sale.holiday);

        db.updateWithOnConflict(
                Sale.TABLE_NAME,
                values,
                "menu_id = ? AND date = ?",
                new String[] { String.valueOf(sale.menu_id), sale.date },
                SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    private void dbSeedTest(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        
        values.put(Menu.MENU_NAME, "????????????");
        values.put(Menu.MENU_PRICE, 19000);
        db.insert(Menu.TABLE_NAME, null, values);

        values.clear();
        values.put(Menu.MENU_NAME, "????????????");
        values.put(Menu.MENU_PRICE, 19000);
        db.insert(Menu.TABLE_NAME, null, values);

        values.clear();
        values.put(Menu.MENU_NAME, "????????????");
        values.put(Menu.MENU_PRICE, 21000);
        db.insert(Menu.TABLE_NAME, null, values);

        values.clear();
        // 2021/4/1

        ArrayList<String> date = new ArrayList<>(Arrays.asList(
//                "2021??? 05??? 21???",
//                "2021??? 05??? 22???",
//                "2021??? 05??? 23???",
                "2021??? 05??? 24???",
                "2021??? 05??? 25???",
                "2021??? 05??? 26???",
                "2021??? 05??? 27???",
                "2021??? 05??? 28???",
                "2021??? 05??? 29???",
                "2021??? 05??? 30???",
                "2021??? 05??? 31???",
                "2021??? 06??? 01???",
                "2021??? 06??? 02???",
                "2021??? 06??? 03???",
                "2021??? 06??? 04???",
                "2021??? 06??? 05???",
                "2021??? 06??? 06???",
                "2021??? 06??? 07???",
                "2021??? 06??? 08???",
                "2021??? 06??? 09???",
                "2021??? 06??? 10???",
                "2021??? 06??? 11???",
                "2021??? 06??? 12???",
                "2021??? 06??? 13???",
                "2021??? 06??? 14???",
                "2021??? 06??? 15???",
                "2021??? 06??? 16???",
                "2021??? 06??? 17???",
                "2021??? 06??? 18???",
                "2021??? 06??? 19???",
                "2021??? 06??? 20???",
                "2021??? 06??? 21???",
                "2021??? 06??? 22???",
                "2021??? 06??? 23???"
//                "2021??? 06??? 24???",
//                "2021??? 06??? 25???",
//                "2021??? 06??? 26???",
//                "2021??? 06??? 27???",
//                "2021??? 06??? 28???",
//                "2021??? 06??? 29???",
//                "2021??? 06??? 30???",
//                "2021??? 06??? 31???"
        ));

        ArrayList<Integer> day = new ArrayList<>(Arrays.asList(
                2, 3, 4, 5, 6, 0, 1, 2, 3, 4, 5, 6, 0, 1, 2, 3, 4, 5, 6, 0, 1, 2, 3, 4, 5, 6, 0, 1, 2, 3, 4
        ));

        ArrayList<Integer> sky = new ArrayList<>(Arrays.asList(
                0, 1, 2, 0, 1, 2,0, 1, 2, 0, 1, 2,0, 1, 2,0, 1, 2,0, 1, 2,0, 1, 2,0, 1, 2,0, 1, 2, 0
                ));
        ArrayList<Integer> rain = new ArrayList<>(Arrays.asList(
                3 ,2 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,2 ,2 ,0 ,0 ,0 ,1 ,2 ,0 ,0 ,0 ,0 ,0 ,1 ,1 ,2 ,0,1
        ));
        ArrayList<Integer> qty1 = new ArrayList<>(Arrays.asList(
                39, 53, 35, 34, 59, 32, 42, 18, 35, 80, 36, 61, 45, 63, 66, 45, 33, 70, 53, 42, 71, 78, 90, 27, 51, 68, 52, 67, 52, 78,72
        ));

        ArrayList<Integer> qty2 = new ArrayList<>(Arrays.asList(
                66, 45, 33, 70, 93, 12, 71, 78, 90, 37, 51, 68, 52, 27, 52, 78,32, 39, 53, 35, 34, 59, 22, 42, 18, 35, 80, 36, 61, 45, 63
        ));

        ArrayList<Integer> qty3 = new ArrayList<>(Arrays.asList(
                 27, 51, 68, 52, 27, 52, 78,32, 39, 53, 35, 34, 59, 22, 42, 18, 35, 80, 36, 61, 45, 63,66, 45, 33, 70, 43, 52, 71, 78, 90
        ));

        for(int i = 1; i < 31; i++) {
            values.put(Sale.MENU_ID, 1);
            values.put(Sale.QTY, qty1.get(i));
            values.put(Sale.TIME, 0);
            values.put(Sale.HOLIDAY, 0);
            values.put(Sale.SKY, sky.get(i));
            values.put(Sale.RAIN, rain.get(i));
            values.put(Sale.DAY, day.get(i));
            values.put(Sale.DATE, date.get(i));
            db.insert(Sale.TABLE_NAME, null, values);
        }

        for(int i = 1; i < 31; i++) {
            values.put(Sale.MENU_ID, 2);
            values.put(Sale.QTY, qty2.get(i));
            values.put(Sale.TIME, 0);
            values.put(Sale.HOLIDAY, 0);
            values.put(Sale.SKY, sky.get(i));
            values.put(Sale.RAIN, rain.get(i));
            values.put(Sale.DAY, day.get(i));
            values.put(Sale.DATE, date.get(i));
            db.insert(Sale.TABLE_NAME, null, values);
        }

        for(int i = 1; i < 31; i++) {
            values.put(Sale.MENU_ID, 3);
            values.put(Sale.QTY, qty3.get(i));
            values.put(Sale.TIME, 0);
            values.put(Sale.HOLIDAY, 0);
            values.put(Sale.SKY, sky.get(i));
            values.put(Sale.RAIN, rain.get(i));
            values.put(Sale.DAY, day.get(i));
            values.put(Sale.DATE, date.get(i));
            db.insert(Sale.TABLE_NAME, null, values);
        }
    }
}