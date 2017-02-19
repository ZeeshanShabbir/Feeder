package io.droidninja.feeder.contentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Zeeshan on 2/12/17.
 */

public class FeederContentProvider extends ContentProvider {
    public static final int CODE_INSERT_SOURCE = 100;
    public static final int CODE_INSERT_SOURCE_SINGLE = 101;
    public static final int CODE_VIEW_ARTICLE = 102;
    public static final int CODE_INSERT_ARTICLE = 102;
    private FeederDbHelper feederDbHelper;
    private UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(FeederContract.CONTENT_AUTHORITY, FeederContract.PATH_SOURCE, CODE_INSERT_SOURCE);
        uriMatcher.addURI(FeederContract.CONTENT_AUTHORITY, FeederContract.PATH_ARTICLE, CODE_VIEW_ARTICLE);
        uriMatcher.addURI(FeederContract.CONTENT_AUTHORITY, FeederContract.PATH_ARTICLE, CODE_INSERT_ARTICLE);
        uriMatcher.addURI(FeederContract.CONTENT_AUTHORITY, FeederContract.PATH_SOURCE + "/#", CODE_INSERT_SOURCE_SINGLE);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        feederDbHelper = new FeederDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int match = sUriMatcher.match(uri);
        Cursor cursor = null;
        switch (match) {
            case CODE_INSERT_SOURCE:
                cursor = feederDbHelper.getReadableDatabase().query(FeederContract.SourceEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_VIEW_ARTICLE:
                cursor = feederDbHelper.getReadableDatabase().query(FeederContract.ArticleEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = feederDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnedUri;
        switch (match) {
            case CODE_INSERT_SOURCE:
                long id = db.insert(FeederContract.SourceEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnedUri = ContentUris.withAppendedId(FeederContract.SourceEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.sqlite.SQLiteException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnedUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (null == selection) selection = "1";

        int numRowsDeleted;
        switch (sUriMatcher.match(uri)) {

            case CODE_INSERT_SOURCE:
                numRowsDeleted = feederDbHelper.getWritableDatabase().delete(
                        FeederContract.SourceEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = feederDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        switch (match) {
            case CODE_INSERT_SOURCE:
                //Return the number of rows inserted from our implementation of bulkInsert
                return insertRecords(FeederContract.SourceEntry.TABLE_NAME, db, values, uri);
            case CODE_INSERT_ARTICLE:
                return insertRecords(FeederContract.ArticleEntry.TABLE_NAME, db, values, uri);
            default:
                return super.bulkInsert(uri, values);
        }
    }

    /**
     * generic method use to insert record to any table
     *
     * @param tabbleName
     * @param db
     * @param values
     * @param uri
     * @return
     */
    public int insertRecords(String tabbleName, SQLiteDatabase db, ContentValues[] values, Uri uri) {
        db.beginTransaction();
        int rowsInserted = 0;
        try {
            for (ContentValues value : values) {
                long _id = db.insertWithOnConflict(tabbleName, null, value, SQLiteDatabase.CONFLICT_REPLACE);
                if (_id != -1) {
                    rowsInserted++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        if (rowsInserted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        //Return the number of rows inserted from our implementation of bulkInsert
        return rowsInserted;
    }
}


