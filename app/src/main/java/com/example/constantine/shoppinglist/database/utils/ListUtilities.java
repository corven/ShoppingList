package com.example.constantine.shoppinglist.database.utils;

import android.database.Cursor;

import com.example.constantine.shoppinglist.model.Purchase;

import java.util.ArrayList;

import static com.example.constantine.shoppinglist.database.utils.DBTableHeaders.KEY_ID;
import static com.example.constantine.shoppinglist.database.utils.DBTableHeaders.KEY_NAME;
import static com.example.constantine.shoppinglist.database.utils.DBTableHeaders.KEY_PRICE;

public class ListUtilities {
    public static ArrayList<Purchase> getAll(Cursor c) {
        ArrayList<Purchase> groups = new ArrayList<>();
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Purchase group = createGroupBy(c);
            groups.add(group);
            c.moveToNext();
        }

        return groups;
    }

    private static Purchase createGroupBy(Cursor c) {
        int id = c.getInt(c.getColumnIndex(KEY_ID));
        String name = c.getString(c.getColumnIndex(KEY_NAME));
        double price = Double.parseDouble(c.getString(c.getColumnIndex(KEY_PRICE)));


        return new Purchase(id, name, price);
    }
}
