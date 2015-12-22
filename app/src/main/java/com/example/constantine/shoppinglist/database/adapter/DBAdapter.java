package com.example.constantine.shoppinglist.database.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.constantine.shoppinglist.database.DBHelper;

public class DBAdapter {

    private SQLiteDatabase database;
    private SQLiteOpenHelper dbHelper;

    public static DBAdapter getInstance() {
        return new DBAdapter();
    }

    public SQLiteDatabase open(Context context) {
        this.dbHelper = new DBHelper(context);
        this.database = dbHelper.getWritableDatabase();

        return database;
    }

    public void close() {
        dbHelper.close();
        database.close();
    }

}
