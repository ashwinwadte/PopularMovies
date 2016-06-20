/**
 * Created by Ashwin on 21-Jun-16
 */
package io.github.ashwinwadte.popularmovies.asynctasks;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.CheckBox;

import io.github.ashwinwadte.popularmovies.database.MovieContract;
import io.github.ashwinwadte.popularmovies.models.Movie;

public class CheckFavoriteMovieTask extends AsyncTask<Movie, Void, Boolean> {
    private Context mContext;
    private CheckBox cbFavorite;

    public CheckFavoriteMovieTask(Context mContext, CheckBox cbFavorite) {
        this.mContext = mContext;
        this.cbFavorite = cbFavorite;
    }

    @Override
    protected Boolean doInBackground(Movie... params) {
        if (params.length == 0)
            return null;

        return isMoviePresent(params[0]);
    }

    @Override
    protected void onPostExecute(Boolean checked) {
        String message;
        if (checked) message = "Favorite";
        else message = "Mark Favorite";

        cbFavorite.setText(message);
        cbFavorite.setChecked(checked);
    }

    private boolean isMoviePresent(Movie movie) {
        String id = movie.getId();
        boolean flagMoviePresent;

        Cursor movieCursor = mContext.getContentResolver().query(MovieContract.MoviesEntry.CONTENT_URI,
                new String[]{MovieContract.MoviesEntry.COLUMN_ID},
                MovieContract.MoviesEntry.COLUMN_ID + " = ?",
                new String[]{id},
                null);

        //if movie is already present return true
        if (movieCursor.moveToFirst()) {
            flagMoviePresent = true;
        } else {
            flagMoviePresent = false;
        }

        movieCursor.close();
        return flagMoviePresent;

    }
}
