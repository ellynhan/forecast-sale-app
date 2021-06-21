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

    // SecondPage의 수량 데이터 가져오기
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

    public ArrayList<Integer> getSaleQtyWithId(int menu_id) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Integer> qtys = new ArrayList<>();

        Cursor cursor = db.rawQuery(
                "SELECT qty FROM sales WHERE menu_id = ?",
                new String[] { String.valueOf(menu_id) });
        if(cursor.moveToFirst()) {
            do {
                int qty;
                qty = cursor.getInt(cursor.getColumnIndex(Sale.QTY));
                qtys.add(qty);
            } while(cursor.moveToNext());
        }

        return qtys;
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

        db.insert(Sale.TABLE_NAME, null, values);
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
        
        values.put(Menu.MENU_NAME, "후라이드");
        values.put(Menu.MENU_PRICE, 19000);
        db.insert(Menu.TABLE_NAME, null, values);

        values.clear();
        values.put(Menu.MENU_NAME, "양념치킨");
        values.put(Menu.MENU_PRICE, 19000);
        db.insert(Menu.TABLE_NAME, null, values);

        values.clear();
        values.put(Menu.MENU_NAME, "간장치킨");
        values.put(Menu.MENU_PRICE, 21000);
        db.insert(Menu.TABLE_NAME, null, values);

        values.clear();
        // 2021/4/1

        ArrayList<String> date = new ArrayList<>(Arrays.asList(
                "2021년 05월 21일",
                "2021년 05월 22일",
                "2021년 05월 23일",
                "2021년 05월 24일",
                "2021년 05월 25일",
                "2021년 05월 26일",
                "2021년 05월 27일",
                "2021년 05월 28일",
                "2021년 05월 29일",
                "2021년 05월 30일",
                "2021년 05월 31일",
                "2021년 06월 01일",
                "2021년 06월 02일",
                "2021년 06월 03일",
                "2021년 06월 04일",
                "2021년 06월 05일",
                "2021년 06월 06일",
                "2021년 06월 07일",
                "2021년 06월 08일",
                "2021년 06월 09일",
                "2021년 06월 10일",
                "2021년 06월 11일",
                "2021년 06월 12일",
                "2021년 06월 13일",
                "2021년 06월 14일",
                "2021년 06월 15일",
                "2021년 06월 16일",
                "2021년 06월 17일",
                "2021년 06월 18일",
                "2021년 06월 19일",
                "2021년 06월 20일"
        ));

        ArrayList<Integer> sky = new ArrayList<>(Arrays.asList(
                0, 1, 2, 0, 1, 2,0, 1, 2,0, 1, 2,0, 1, 2,0, 1, 2,0, 1, 2,0, 1, 2,0, 1, 2,0, 1, 2,0
                ));
        ArrayList<Integer> rain = new ArrayList<>(Arrays.asList(
                3 ,2 ,0 ,0 ,0 ,0 ,1 ,1 ,0 ,0 ,0 ,0 ,0 ,0 ,2 ,2 ,0 ,0 ,0 ,1 ,2 ,0 ,0 ,0 ,0 ,0 ,1 ,1 ,2 ,0,1
        ));
        ArrayList<Integer> qty1 = new ArrayList<>(Arrays.asList(
                39, 53, 35, 34, 59, 22, 42, 28, 35, 40, 36, 51, 45, 23, 96, 45, 33, 70, 93, 42, 71, 78, 90, 57, 61, 68, 52,57, 22, 78, 32
        ));
        ArrayList<Integer> qty2 = new ArrayList<>(Arrays.asList(
                44, 31, 30, 36, 61, 66, 33, 15, 92, 3, 28, 50, 17, 67, 16, 61, 69, 65, 37, 72, 49, 78, 64, 91, 96, 77, 53, 91, 35, 45, 52
        ));
        ArrayList<Integer> qty3 = new ArrayList<>(Arrays.asList(
                69, 65, 37, 72, 39, 78, 34, 61, 96, 17, 53, 91, 35, 25,32,44, 31, 30, 36, 21, 66, 33, 45, 102, 30, 28, 100, 17, 67, 56, 60
        ));

        for(int i = 1; i < 31; i++) {
            values.put(Sale.MENU_ID, 1);
            values.put(Sale.QTY, qty1.get(i));
            values.put(Sale.TIME, 0);
            values.put(Sale.HOLIDAY, 0);
            values.put(Sale.SKY, sky.get(i));
            values.put(Sale.RAIN, rain.get(i));
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
            values.put(Sale.DATE, date.get(i));
            db.insert(Sale.TABLE_NAME, null, values);
        }
    }
}