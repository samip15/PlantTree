package com.example.planttree.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class PlantContentProvider extends ContentProvider {
    //match
    public static final int PLANTS = 100;
    public static final int PLANT_WITH_ID = 101;

    //uri matcher
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String TAG = PlantContentProvider.class.getName();

    public static UriMatcher buildUriMatcher() {
        // Initialize a UriMatcher
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // Add URI matches
        uriMatcher.addURI(PlantContract.AUTHORITY, PlantContract.PATH_PLANTS, PLANTS);
        uriMatcher.addURI(PlantContract.AUTHORITY, PlantContract.PATH_PLANTS + "/#", PLANT_WITH_ID);
        return uriMatcher;
    }


    private PlantDBHelper mPlantDbHelper;


    @Override
    public boolean onCreate() {
        Context context = getContext();
        mPlantDbHelper = new PlantDBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mPlantDbHelper.getReadableDatabase();
        //match uri
        int match = sUriMatcher.match(uri);
        Cursor retCursor;
        switch (match) {
            // Query for the plants directory
            case PLANTS:
                retCursor = db.query(PlantContract.PlantEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case PLANT_WITH_ID:
                String id = uri.getPathSegments().get(1);
                retCursor = db.query(PlantContract.PlantEntry.TABLE_NAME,
                        projection,
                        "_id=?",
                        new String[]{id},
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        //notify
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mPlantDbHelper.getWritableDatabase();

        // match uri
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case PLANTS:
                long id = db.insert(PlantContract.PlantEntry.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(PlantContract.PlantEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        //notify
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = mPlantDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int plantsDeleted;
        switch (match) {
            case PLANT_WITH_ID:
                String id = uri.getPathSegments().get(1);
                plantsDeleted = db.delete(PlantContract.PlantEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        //notify plant delete
        if (plantsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return plantsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = mPlantDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int plantsUpdated;
        //match
        switch (match) {
//            case PLANTS:
//                plantsUpdated = db.update(PlantContract.PlantEntry.TABLE_NAME, contentValues, selection, selectionArgs);
//                break;
            case PLANT_WITH_ID:
                if (selection == null) {
                    selection = PlantContract.PlantEntry._ID + "=?";
                } else {
                    selection += " AND " + PlantContract.PlantEntry._ID + "=?";
                }
                String id = uri.getPathSegments().get(1);
                if (selectionArgs == null) selectionArgs = new String[]{id};
                else {
                    ArrayList<String> selectionArgsList = new ArrayList<String>();
                    selectionArgsList.addAll(Arrays.asList(selectionArgs));
                    selectionArgsList.add(id);
                    selectionArgs = selectionArgsList.toArray(new String[selectionArgsList.size()]);
                }
                plantsUpdated = db.update(PlantContract.PlantEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (plantsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return plantsUpdated;
    }
}
