package com.example.desafioshipp.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Connection extends SQLiteOpenHelper {
    private static final String name = "database.db";
    private static final int version = 1;

    public Connection(@Nullable Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
            "create table cards(" +
                "id integer primary key autoincrement," +
                "number varchar(50)," +
                "name varchar(50)," +
                "validity varchar(50)," +
                "cpf varchar(50)," +
                "cvv varchar(50)" +
            ")"
        );
        sqLiteDatabase.execSQL(
            "create table orders(" +
                    "id integer primary key autoincrement," +
                    "placeId varchar(150)," +
                    "description varchar(150)," +
                    "value real," +
                    "cardId int," +
                    "storeLat real," +
                    "storeLong real," +
                    "userLat real," +
                    "userLong real" +
            ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
