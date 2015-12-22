package com.example.constantine.shoppinglist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.constantine.shoppinglist.database.utils.DBQueryStrings.CREATE_TABLE;
import static com.example.constantine.shoppinglist.database.utils.DBQueryStrings.DROP_TABLE;
import static com.example.constantine.shoppinglist.database.utils.DBTableHeaders.DATABASE_NAME;
import static com.example.constantine.shoppinglist.database.utils.DBTableHeaders.DATABASE_VERSION;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);

        onCreate(db);
    }
}
