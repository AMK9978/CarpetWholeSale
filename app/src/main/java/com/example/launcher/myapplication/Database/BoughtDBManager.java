package com.example.launcher.myapplication.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.launcher.myapplication.Models.Report;

import java.util.ArrayList;

public class BoughtDBManager {
    private SQLiteOpenHelper sqLiteOpenHelper;
    private SQLiteDatabase sqLiteDatabase;
    private String[] column_names = new String[]{"id", SqliteHelper.AMOUNT, SqliteHelper.CARPET_ID,
            SqliteHelper.BUYER_ID, SqliteHelper.DATE};

    public BoughtDBManager(Context context) {
        this.sqLiteOpenHelper = new SqliteHelper(context, SqliteHelper.BOUGHT_REPORTS_TABLE_NAME, null, 1);
    }

    public void open() {
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
    }

    public void close() {
        sqLiteDatabase.close();
    }

    public void deleteBoughtReport(int id) {
        try {
            Report report = getBoughtReportByID(id);
            sqLiteDatabase.delete(SqliteHelper.BOUGHT_REPORTS_TABLE_NAME, "id=?", new String[]{String.valueOf(id)});
        } catch (Exception exception) {
            Log.i("TAG", "exception occurred in delete bought report and " + exception.getMessage());
        }
    }

    public Report getBoughtReportByID(int id) {
        Cursor cursor = sqLiteDatabase.query(SqliteHelper.BOUGHT_REPORTS_TABLE_NAME, column_names,
                "id=?", new String[]{String.valueOf(id)}, null, null, null);
        Report report = new Report();
        if (cursor.moveToFirst()) {
            report.setAmount(cursor.getInt(cursor.getColumnIndex(SqliteHelper.AMOUNT)));
            report.setCarpet_id(cursor.getInt(cursor.getColumnIndex(SqliteHelper.CARPET_ID)));
            report.setDate(cursor.getString(cursor.getColumnIndex(SqliteHelper.DATE)));
            report.setMerchant_id(cursor.getInt(cursor.getColumnIndex(SqliteHelper.BUYER_ID)));
        }
        cursor.close();
        return report;
    }


    public ArrayList<Report> getAllBoughtReports() {
        ArrayList<Report> list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(SqliteHelper.BOUGHT_REPORTS_TABLE_NAME, column_names,
                null, null, null, null, null);
        if (cursor.moveToFirst()) {
            Report report;
            do {
                report = new Report();
                report.setAmount(cursor.getInt(cursor.getColumnIndex(SqliteHelper.AMOUNT)));
                report.setCarpet_id(cursor.getInt(cursor.getColumnIndex(SqliteHelper.CARPET_ID)));
                report.setDate(cursor.getString(cursor.getColumnIndex(SqliteHelper.DATE)));
                report.setMerchant_id(cursor.getInt(cursor.getColumnIndex(SqliteHelper.BUYER_ID)));
                list.add(report);
                Log.i("TAG", "getAll : " + report.getAmount() + " , " + report.getCarpet_id());
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }


    public void dropTable() {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SqliteHelper.BOUGHT_REPORTS_TABLE_NAME);
    }

    public void addBoughtReport(Report report) {
        ContentValues values = new ContentValues();
        values.put(SqliteHelper.AMOUNT, report.getAmount());
        values.put(SqliteHelper.DATE, report.getDate());
        values.put(SqliteHelper.BUYER_ID, report.getMerchant_id());
        values.put(SqliteHelper.CARPET_ID, report.getCarpet_id());
        sqLiteDatabase.insert(SqliteHelper.BOUGHT_REPORTS_TABLE_NAME, null, values);
    }

}
