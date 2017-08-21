package com.example.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tayyab on 8/20/2017.
 */

public class IngredientDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ingredient_list.db";
    private static final int DATABASE_VERSION = 1;

    public IngredientDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_INGREDIENT_CREATE_TABLE = "Create Table "+
                BakingContract.IngredientEntry.TABLE_NAME + "("+
                BakingContract.IngredientEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BakingContract.IngredientEntry.INGREDIENT_QUANTITY + " TEXT NOT NULL,"+
                BakingContract.IngredientEntry.INGREDIENT_MEASURE + " TEXT NOT NULL,"+
                BakingContract.IngredientEntry.INGREDIENT_NAME + " TEXT NOT NULL,"+
                BakingContract.IngredientEntry.RECIPE_ID + " INTEGER NOT NULL"+
                ");";
        db.execSQL(SQL_INGREDIENT_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ BakingContract.IngredientEntry.TABLE_NAME);
        onCreate(db);
    }
}
