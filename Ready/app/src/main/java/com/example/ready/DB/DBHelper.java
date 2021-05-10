package com.example.ready.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.ready.DB.Model.Menu;
import com.example.ready.DB.Model.Sale;

public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper instance;

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "test.db";
    private static final String MENU_CREATE =
            String.format(
                    "CREATE TABLE IF NOT EXISTS %s (" +
                            "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "%s INTEGER," +
                            "%s INTEGER)",
                    Menu.MenuEntry.TABLE_NAME,
                    Menu.MenuEntry._ID,
                    Menu.MenuEntry.MENUID,
                    Menu.MenuEntry.PRICE
            );
    private static final String SALE_CREATE =
            String.format(
                    "CREATE TABLE IF NOT EXISTS %s (" +
                            "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "%s INTEGER," +
                            "%s INTEGER," +
                            "%s INTEGER," +
                            "%s TEXT," +
                            "%s INTEGER," +
                            "%s INTEGER)",
                    Sale.SaleEntry.TABLE_NAME,
                    Sale.SaleEntry._ID,
                    Sale.SaleEntry.MENUID,
                    Sale.SaleEntry.QTY,
                    Sale.SaleEntry.WEATHER,
                    Sale.SaleEntry.DATE,
                    Sale.SaleEntry.TIME,
                    Sale.SaleEntry.HOLIDAY
            );
    private static final String MENU_DELETE =
            "DROP TABLE IF EXISTS " + Menu.MenuEntry.TABLE_NAME;
    private static final String SALE_DELETE =
            "DROP TABLE IF EXISTS " + Sale.SaleEntry.TABLE_NAME;

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized DBHelper getInstance(Context context) {
        if( instance == null )
            instance = new DBHelper(context.getApplicationContext());

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MENU_CREATE);
        db.execSQL(SALE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MENU_DELETE);
        db.execSQL(SALE_DELETE);

        onCreate(db);
    }
}
