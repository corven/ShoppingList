package com.example.constantine.shoppinglist.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.constantine.shoppinglist.database.adapter.DBAdapter;
import com.example.constantine.shoppinglist.database.utils.ListUtilities;
import com.example.constantine.shoppinglist.database.utils.PurchaseContentValuesBuilder;
import com.example.constantine.shoppinglist.model.Purchase;

import java.util.ArrayList;

import static com.example.constantine.shoppinglist.database.utils.DBQueryStrings.GET_ALL;
import static com.example.constantine.shoppinglist.database.utils.DBTableHeaders.KEY_ID;
import static com.example.constantine.shoppinglist.database.utils.DBTableHeaders.TABLE_NAME;

public class PurchaseDao {
    private DBAdapter dbAdapter;
    private SQLiteDatabase database;

    public PurchaseDao(Context context) {
        dbAdapter = DBAdapter.getInstance();
        database = dbAdapter.open(context);
    }

    public void create(Purchase purchase) {
        ContentValues values = PurchaseContentValuesBuilder.createContentValues(purchase);
        database.insert(TABLE_NAME, null, values);
    }

    public void update(Purchase purchase) {
        ContentValues values = PurchaseContentValuesBuilder.createContentValues(purchase);
        String where = KEY_ID + "=" + purchase.getId();
        database.update(TABLE_NAME, values, where, null);
    }

    public void delete(Purchase purchase) {
        String where = KEY_ID + "=" + purchase.getId();
        database.delete(TABLE_NAME, where, null);
    }

    public ArrayList<Purchase> getAll() {
        Cursor cursor = database.rawQuery(GET_ALL, null);
        ArrayList<Purchase> purchases = ListUtilities.getAll(cursor);
        cursor.close();

        return purchases;
    }

    public int getCount() {
        Cursor cursor = database.rawQuery(GET_ALL, null);
        return cursor.getCount();
    }
}
