package com.example.launcher.myapplication.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import com.example.launcher.myapplication.Models.Carpet;

import java.io.File;
import java.util.ArrayList;

public class CarpetDBManager {
    private SQLiteOpenHelper sqLiteOpenHelper;
    private SQLiteDatabase sqLiteDatabase;
    private String column_names [] = new String[]{"id",SqliteHelper.PRICE, SqliteHelper.PATH};

    public CarpetDBManager(Context context) {
        this.sqLiteOpenHelper = new SqliteHelper(context,SqliteHelper.DB_NAME,null,1);
    }

    public void open() {
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
    }

    public void close() {
        sqLiteDatabase.close();
    }

    public void deleteCarpet(int id) {
        try {
            Carpet carpet = getCarpetById(id);
            File file = new File(carpet.getPath());
            file.delete();
            sqLiteDatabase.delete(SqliteHelper.CARPET_TABLE_NAME, "id=?", new String[]{String.valueOf(id)});
        } catch (Exception exception) {
            Log.i("TAG","exception occurred in deleteCarpet and " + exception.getMessage());
        }
    }

    public Carpet getCarpetById(int id) {
        Cursor cursor = sqLiteDatabase.query(SqliteHelper.CARPET_TABLE_NAME, column_names,
                "id=?", new String[]{String.valueOf(id)}, null, null, null);
        Carpet carpet = new Carpet();
        if (cursor.moveToFirst()) {
            carpet.setPrice(cursor.getInt(cursor.getColumnIndex(SqliteHelper.PRICE)));
            carpet.setPath(cursor.getString(cursor.getColumnIndex(SqliteHelper.PATH)));
        }
        cursor.close();
        return carpet;
    }


    public ArrayList<Carpet> getALLCarpets() {
        ArrayList<Carpet> list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(SqliteHelper.CARPET_TABLE_NAME, column_names,
                null, null, null, null, null);
        if (cursor.moveToFirst()) {
            Carpet carpet;
            do {
                carpet = new Carpet();
                carpet.setPrice(cursor.getInt(cursor.getColumnIndex(SqliteHelper.PRICE)));
                carpet.setPath(cursor.getString(cursor.getColumnIndex(SqliteHelper.PATH)));
                list.add(carpet);
                Log.i("TAG","getAll : " + carpet.getPrice() + " , " + carpet.getPath());
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }


    public void dropTable(){
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SqliteHelper.CARPET_TABLE_NAME);
    }
    public void addCarpet(Carpet carpet) {
        ContentValues values = new ContentValues();
        values.put(SqliteHelper.PRICE, carpet.getPrice());
        values.put(SqliteHelper.PATH, String.valueOf(carpet.getPath()));
        sqLiteDatabase.insert(SqliteHelper.CARPET_TABLE_NAME, null, values);
    }

}
