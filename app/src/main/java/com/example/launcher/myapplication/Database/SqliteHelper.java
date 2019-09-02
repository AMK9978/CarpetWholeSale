package com.example.launcher.myapplication.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHelper extends SQLiteOpenHelper {
    static final String DB_NAME = "MY_DB";
    static final String CARPET_TABLE_NAME = "AppCarpets";
    static final String PRICE = "Price";
    static final String CARPET_ID = "Carpet_id";
    static final String PATH = "Path";
    static final String SELLERS_TABLE_NAME = "SellersTable";
    static final String BUYERS_TABLE_NAME = "BuyersTable";
    static final String DATE = "Date";
    static final String BUYER_ID = "Buyer_id";
    static final String SELLER_ID = "Seller_id";
    static final String NAME = "name";
    static final String PHONE = "phone";
    static final String EMAIL = "email";
    static final String REPORTS_TABLE_NAME = "reports";

    public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, 14);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + CARPET_TABLE_NAME + "( id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PRICE + " INTEGER, " + PATH + " TEXT)");

        db.execSQL("create table if not exists " + REPORTS_TABLE_NAME + "( id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CARPET_ID + " INTEGER, " + BUYER_ID + " INTEGER, " + SELLER_ID + " INTEGER, " + DATE + " TEXT)");

        db.execSQL("create table if not exists " + SELLERS_TABLE_NAME + "( id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " TEXT, " + EMAIL + " TEXT, " + PHONE + " TEXT)");

        db.execSQL("create table if not exists " + BUYERS_TABLE_NAME + "( id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " TEXT, " + EMAIL + " TEXT, " + PHONE + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + CARPET_TABLE_NAME);
        db.execSQL("drop table if exists " + REPORTS_TABLE_NAME);
        db.execSQL("drop table if exists " + BUYERS_TABLE_NAME);
        db.execSQL("drop table if exists " + SELLERS_TABLE_NAME);
        onCreate(db);
    }
}