package com.example.constantine.shoppinglist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by constantine on 15.10.14.
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final int  DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "shoppingList",
            TABLE_NAME = "shopping",
            KEY_ID = "id",
            KEY_PRICE = "price",
            KEY_NAME = "name";

    public DBHandler (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT," + KEY_PRICE + " DOUBLE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    public void createPurchase(Purchase purchase) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, purchase.getName());
        values.put(KEY_PRICE, purchase.getPrice());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void deletePurchase(Purchase purchase){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + "=?", new String[]{String.valueOf(purchase.getId())});
        db.close();
    }

    public int getPuschaseCount(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        int count = cursor.getCount();
        db.close();
        cursor.close();

        return count;
    }

    public List<Purchase> getAllPurchases(){
        List<Purchase> books = new ArrayList<Purchase>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()){
            do {
                books.add(new Purchase(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                        Double.parseDouble(cursor.getString(2))));
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return books;
    }
}
