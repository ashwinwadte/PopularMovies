/**
 * Created by Ashwin on 20-Jun-16
 */
package io.github.ashwinwadte.popularmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDatabaseHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "favorite_movies.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MovieContract.MoviesEntry.TABLE_NAME + " (" +
                MovieContract.MoviesEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " +
                MovieContract.MoviesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.MoviesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieContract.MoviesEntry.COLUMN_PLOT_SYNOPSIS + " TEXT NOT NULL, " +
                MovieContract.MoviesEntry.COLUMN_USER_RATING + " TEXT NOT NULL, " +
                MovieContract.MoviesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL " +
                ");";

        db.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MoviesEntry.TABLE_NAME);
        onCreate(db);
    }
}
