package com.example.launcher.myapplication.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.launcher.myapplication.Models.Merchant;

import java.util.ArrayList;

public class SellersDBManager {
    private SQLiteOpenHelper sqLiteOpenHelper;
    private SQLiteDatabase sqLiteDatabase;
    private String[] column_names = new String[]{"id", SqliteHelper.SELLER_ID,
            SqliteHelper.NAME, SqliteHelper.EMAIL, SqliteHelper.PHONE};

    public SellersDBManager(Context context) {
        this.sqLiteOpenHelper = new SqliteHelper(context, SqliteHelper.SELLERS_TABLE_NAME, null, 1);
    }

    public void open() {
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
    }

    public void close() {
        sqLiteDatabase.close();
    }

    public void deleteSeller(int id) {
        try {
            sqLiteDatabase.delete(SqliteHelper.SELLERS_TABLE_NAME, "id=?", new String[]{String.valueOf(id)});
        } catch (Exception exception) {
            Log.i("TAG", "exception occurred in delete seller and " + exception.getMessage());
        }
    }

    public Merchant getSellerByID(int id) {
        Cursor cursor = sqLiteDatabase.query(SqliteHelper.SELLERS_TABLE_NAME, column_names,
                "id=?", new String[]{String.valueOf(id)}, null, null, null);
        Merchant merchant = new Merchant();
        if (cursor.moveToFirst()) {
            merchant.setId(cursor.getInt(cursor.getColumnIndex(SqliteHelper.SELLER_ID)));
            merchant.setEmail(cursor.getString(cursor.getColumnIndex(SqliteHelper.EMAIL)));
            merchant.setName(cursor.getString(cursor.getColumnIndex(SqliteHelper.NAME)));
            merchant.setPhone(cursor.getString(cursor.getColumnIndex(SqliteHelper.PHONE)));
        }
        cursor.close();
        return merchant;
    }

    public ArrayList<Merchant> getAllSellers() {
        ArrayList<Merchant> list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(SqliteHelper.SELLERS_TABLE_NAME, column_names,
                null, null, null, null, null);
        Cursor carpet_cursor = sqLiteDatabase.query(SqliteHelper.SELLERS_TABLE_NAME, column_names,
                null, null, null, null, null);
        if (cursor.moveToFirst()) {
            Merchant merchant;
            do {
                merchant = getSellerByID(cursor.getInt(cursor.getColumnIndex(SqliteHelper.SELLER_ID)));
                list.add(merchant);
            } while (cursor.moveToNext());
        }
        cursor.close();
        carpet_cursor.close();
        return list;
    }


    public void dropTable() {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SqliteHelper.SELLERS_TABLE_NAME);
    }

    public void addSeller(Merchant merchant) {
        ContentValues values = new ContentValues();
        values.put(SqliteHelper.SELLER_ID, merchant.getId());
        values.put(SqliteHelper.EMAIL, merchant.getEmail());
        values.put(SqliteHelper.NAME, merchant.getName());
        values.put(SqliteHelper.PHONE, merchant.getPhone());
        sqLiteDatabase.insert(SqliteHelper.SELLERS_TABLE_NAME, null, values);
    }
}
