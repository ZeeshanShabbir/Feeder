package io.droidninja.feeder.contentProvider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import io.droidninja.feeder.contentProvider.FeederContract.ArticleEntry;
import io.droidninja.feeder.contentProvider.FeederContract.SourceEntry;

/**
 * Created by Zeeshan on 2/12/17.
 */

public class FeederDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "weather.db";
    private static final int DATABASE_VERSION = 2;

    public FeederDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_SOURCE_TABLE = "CREATE TABLE " + SourceEntry.TABLE_NAME + " (" +
                SourceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                SourceEntry.IDENTIFIER + " TEXT NOT NULL, " +
                SourceEntry.NAME + " TEXT);";
        final String SQL_CREATE_ARTICLE_TABLE = "CREATE TABLE " + ArticleEntry.TABLE_NAME + " (" +
                ArticleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ArticleEntry.AUTHOR + " TEXT , " +
                ArticleEntry.TITLE + " TEXT NOT NULL, " +
                ArticleEntry.DESCRIPTION + " TEXT NOT NULL, " +
                ArticleEntry.PUBLISH_AT + " TEXT , " +
                ArticleEntry.URL + " TEXT NOT NULL, " +
                ArticleEntry.URL_TO_IMAGE + " TEXT NOT NULL, " +
                ArticleEntry.SOURCE + " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_SOURCE_TABLE);
        db.execSQL(SQL_CREATE_ARTICLE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SourceEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ArticleEntry.TABLE_NAME);
        onCreate(db);
    }
}
