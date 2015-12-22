package com.example.constantine.shoppinglist.database.utils;

import android.content.ContentValues;

import com.example.constantine.shoppinglist.model.Purchase;

import static com.example.constantine.shoppinglist.database.utils.DBTableHeaders.*;

public class PurchaseContentValuesBuilder {
    public static ContentValues createContentValues(Purchase purchase) {
        ContentValues values = new ContentValues();

        values.put(KEY_NAME, purchase.getName());
        values.put(KEY_PRICE, purchase.getPrice());

        return values;
    }
}
