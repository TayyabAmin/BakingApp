package com.example.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Tayyab on 8/20/2017.
 */

public class IngredientContentProvider extends ContentProvider {

    private static final int INGREDIENT = 100;
    private static final int INGREDIENT_WITH_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(BakingContract.AUTHORITY, BakingContract.PATH_INGREDIENT, INGREDIENT);
        uriMatcher.addURI(BakingContract.AUTHORITY, BakingContract.PATH_INGREDIENT, INGREDIENT_WITH_ID);
        return uriMatcher;
    }

    private IngredientDBHelper mIngredientDBHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mIngredientDBHelper = new IngredientDBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mIngredientDBHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor retCursor;
        switch (match) {
            case INGREDIENT_WITH_ID:
                retCursor = db.query(BakingContract.IngredientEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case INGREDIENT:
                retCursor = db.query(BakingContract.IngredientEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: "+uri);
        }
        if (getContext().getContentResolver() == null) {
            return null;
        }else {
            retCursor.setNotificationUri(getContext().getContentResolver(), uri);
            return retCursor;
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mIngredientDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case INGREDIENT_WITH_ID:
                long id = db.insert(BakingContract.IngredientEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(BakingContract.IngredientEntry.CONTENT_URI, id);
                }else {
                    throw new android.database.SQLException("Failed to insert row into: "+uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: "+uri);
        }
        if (getContext().getContentResolver() == null) {
            return null;
        }else {
            getContext().getContentResolver().notifyChange(uri, null);
            return returnUri;
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }
}
