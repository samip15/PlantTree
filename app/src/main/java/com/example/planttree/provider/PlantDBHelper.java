package com.example.planttree.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class PlantDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "planttree.db";
    private static final int DB_VERSION = 1;

    public PlantDBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_PLANTS_TABLE = "CREATE TABLE " + PlantContract.PlantEntry.TABLE_NAME + " (" +
                PlantContract.PlantEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PlantContract.PlantEntry.COLUMN_PLANT_TYPE + " INTEGER NOT NULL, " +
                PlantContract.PlantEntry.COLUMN_CREATION_TIME + " TIMESTAMP NOT NULL, " +
                PlantContract.PlantEntry.COLUMN_LAST_WATERED_TIME + " TIMESTAMP NOT NULL)";
        sqLiteDatabase.execSQL(SQL_CREATE_PLANTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PlantContract.PlantEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
