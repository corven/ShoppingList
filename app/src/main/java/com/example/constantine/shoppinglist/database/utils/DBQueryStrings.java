package com.example.constantine.shoppinglist.database.utils;

import static com.example.constantine.shoppinglist.database.utils.DBTableHeaders.*;

public class DBQueryStrings {
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_NAME + " TEXT," + KEY_PRICE + " DOUBLE)";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final String GET_ALL = "SELECT * FROM " + TABLE_NAME;
}
