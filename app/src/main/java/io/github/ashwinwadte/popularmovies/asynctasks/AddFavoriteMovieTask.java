/**
 * Created by Ashwin on 20-Jun-16
 */
package io.github.ashwinwadte.popularmovies.asynctasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.CheckBox;

import io.github.ashwinwadte.popularmovies.database.MovieContract;
import io.github.ashwinwadte.popularmovies.models.Movie;

public class AddFavoriteMovieTask extends AsyncTask<Movie, Void, Boolean> {
    private Context mContext;
    private boolean mCheckBoxChecked;
    private CheckBox cbFavorite;

    public AddFavoriteMovieTask(Context context, boolean mCheckBoxChecked, CheckBox cbFavorite) {
        this.mContext = context;
        this.mCheckBoxChecked = mCheckBoxChecked;
        this.cbFavorite = cbFavorite;
    }

    @Override
    protected Boolean doInBackground(Movie... params) {
        if (params.length == 0)
            return null;

        if (mCheckBoxChecked) {
            return insertFavoriteMovie(params[0]);
        }

        return removeFavoriteMovie(params[0]);
    }


    @Override
    protected void onPostExecute(Boolean checked) {
        String message;
        if (checked) message = "Favorite";
        else message = "Mark Favorite";

        cbFavorite.setText(message);
    }

    private Boolean insertFavoriteMovie(Movie movie) {
        String id = movie.getId();

        Cursor movieCursor = mContext.getContentResolver().query(MovieContract.MoviesEntry.CONTENT_URI,
                new String[]{MovieContract.MoviesEntry.COLUMN_ID},
                MovieContract.MoviesEntry.COLUMN_ID + " = ?",
                new String[]{id},
                null);

        assert movieCursor != null;
        if (!movieCursor.moveToFirst()) {

            ContentValues values = new ContentValues();

            values.put(MovieContract.MoviesEntry.COLUMN_ID, Integer.parseInt(movie.getId()));
            values.put(MovieContract.MoviesEntry.COLUMN_TITLE, movie.getTitle());
            values.put(MovieContract.MoviesEntry.COLUMN_POSTER_PATH, movie.getPosterUrl());
            values.put(MovieContract.MoviesEntry.COLUMN_PLOT_SYNOPSIS, movie.getPlotSynopsis());
            values.put(MovieContract.MoviesEntry.COLUMN_USER_RATING, movie.getUserRating());
            values.put(MovieContract.MoviesEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());

            mContext.getContentResolver().insert(MovieContract.MoviesEntry.CONTENT_URI, values);
        }
        movieCursor.close();
        return true;
    }

    private boolean removeFavoriteMovie(Movie movie) {
        String id = movie.getId();

        Cursor movieCursor = mContext.getContentResolver().query(MovieContract.MoviesEntry.CONTENT_URI,
                new String[]{MovieContract.MoviesEntry.COLUMN_ID},
                MovieContract.MoviesEntry.COLUMN_ID + " = ?",
                new String[]{id},
                null);

        //if movie is already present then remove it
        assert movieCursor != null;
        if (movieCursor.moveToFirst()) {
            int idIndex = movieCursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_ID);
            long movie_id = movieCursor.getLong(idIndex);

            mContext.getContentResolver().delete(MovieContract.MoviesEntry.CONTENT_URI,
                    MovieContract.MoviesEntry.COLUMN_ID + " = ? ",
                    new String[]{String.valueOf(id)}
            );
        }
        movieCursor.close();
        return false;
    }
}
