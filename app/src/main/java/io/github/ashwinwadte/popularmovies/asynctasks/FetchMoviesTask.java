/**
 * Created by Ashwin on 08-May-16
 */

package io.github.ashwinwadte.popularmovies.asynctasks;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import io.github.ashwinwadte.popularmovies.adapters.PosterAdapter;
import io.github.ashwinwadte.popularmovies.database.MovieContract;
import io.github.ashwinwadte.popularmovies.models.Movie;

public class FetchMoviesTask extends AsyncTask<Void, Void, List<Movie>> {
    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    PosterAdapter mPosterAdapter;
    ProgressBar mProgressBar;
    private Context mContext;

    public FetchMoviesTask(Context context, PosterAdapter mPosterAdapter, ProgressBar mProgressBar) {
        this.mContext = context;
        this.mPosterAdapter = mPosterAdapter;
        this.mProgressBar = mProgressBar;
    }

    @Override
    protected void onPreExecute() {
        mProgressBar.setVisibility(View.VISIBLE);
        mPosterAdapter.clear();
    }

    @Override
    protected List<Movie> doInBackground(Void... params) {
        /*if (params.length == 0)
            return null;*/

        return getFavoriteMovies();
    }

    @Override
    protected void onPostExecute(List<Movie> results) {
        if (results != null) {
            for (Movie movie :
                    results) {
                mPosterAdapter.add(movie);
            }
        }

        mProgressBar.setVisibility(View.GONE);
    }

    private List<Movie> getFavoriteMovies() {
        List<Movie> movieList = new ArrayList<>();

        Cursor cursor = mContext.getContentResolver().query(MovieContract.MoviesEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        assert cursor != null;
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_ID);
            int titleIndex = cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_TITLE);
            int plotSynopsisIndex = cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_PLOT_SYNOPSIS);
            int posterPathIndex = cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_POSTER_PATH);
            int userRatingIndex = cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_USER_RATING);
            int releaseDateIndex = cursor.getColumnIndex(MovieContract.MoviesEntry.COLUMN_RELEASE_DATE);


            do {
                Movie movie = new Movie(String.valueOf(cursor.getLong(idIndex)),
                        cursor.getString(titleIndex),
                        cursor.getString(posterPathIndex),
                        cursor.getString(plotSynopsisIndex),
                        cursor.getString(userRatingIndex),
                        cursor.getString(releaseDateIndex));

                movieList.add(movie);
            } while (cursor.moveToNext());

            cursor.close();
            return movieList;
        }
        return null;
    }
}