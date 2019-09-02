package com.example.launcher.myapplication.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.launcher.myapplication.Models.Carpet;
import com.example.launcher.myapplication.Models.Report;

import java.util.ArrayList;

public class ReportDBManager {
    private SQLiteOpenHelper sqLiteOpenHelper;
    private SQLiteDatabase sqLiteDatabase;
    private String[] column_names = new String[]{"id", SqliteHelper.CARPET_ID,
            SqliteHelper.BUYER_ID, SqliteHelper.SELLER_ID, SqliteHelper.DATE};
    private Context context;

    public ReportDBManager(Context context) {
        this.context = context;
        this.sqLiteOpenHelper = new SqliteHelper(context, SqliteHelper.REPORTS_TABLE_NAME, null, 1);
    }

    public void open() {
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
    }

    public void close() {
        sqLiteDatabase.close();
    }

    public void deleteBoughtReport(int id) {
        try {
            Report report = getReportByID(id);
            sqLiteDatabase.delete(SqliteHelper.REPORTS_TABLE_NAME, "id=?", new String[]{String.valueOf(id)});
        } catch (Exception exception) {
            Log.i("TAG", "exception occurred in delete bought report and " + exception.getMessage());
        }
    }

    private Report getReportByID(int id) {
        Cursor cursor = sqLiteDatabase.query(SqliteHelper.REPORTS_TABLE_NAME, column_names,
                "id=?", new String[]{String.valueOf(id)}, null, null, null);
        Report report = new Report();
        CarpetDBManager carpetDBManager = new CarpetDBManager(context);
        carpetDBManager.open();
        if (cursor.moveToFirst()) {
            Carpet carpet = carpetDBManager.getCarpetById(cursor.getInt(cursor.getColumnIndex(SqliteHelper.CARPET_ID)));
            report.setDate(cursor.getString(cursor.getColumnIndex(SqliteHelper.DATE)));
            report.setCarpet(carpet);
            report.setBuyer_id(cursor.getInt(cursor.getColumnIndex(SqliteHelper.BUYER_ID)));
            report.setSeller_id(cursor.getInt(cursor.getColumnIndex(SqliteHelper.SELLER_ID)));
        }
        carpetDBManager.close();
        cursor.close();
        return report;
    }


    public ArrayList<Report> getAllReports() {
        ArrayList<Report> list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(SqliteHelper.REPORTS_TABLE_NAME, column_names,
                null, null, null, null, null);
        if (cursor.moveToFirst()) {
            Report report;
            do {
                report = getReportByID(cursor.getInt(cursor.getColumnIndex("id")));
                list.add(report);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }


    public void dropTable() {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SqliteHelper.REPORTS_TABLE_NAME);
    }

    public void addReport(Report report) {
        ContentValues values = new ContentValues();
        values.put(SqliteHelper.DATE, report.getDate());
        values.put(SqliteHelper.BUYER_ID, report.getBuyer_id());
        values.put(SqliteHelper.CARPET_ID, report.getCarpet().getId());
        sqLiteDatabase.insert(SqliteHelper.REPORTS_TABLE_NAME, null, values);
    }

}
