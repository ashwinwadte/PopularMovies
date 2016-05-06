package io.github.ashwinwadte.popularmovies.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.github.ashwinwadte.popularmovies.BuildConfig;
import io.github.ashwinwadte.popularmovies.DetailActivity;
import io.github.ashwinwadte.popularmovies.R;
import io.github.ashwinwadte.popularmovies.adapters.PosterAdapter;
import io.github.ashwinwadte.popularmovies.models.Movie;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    public static final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    SharedPreferences pref;

    private ProgressBar mProgressBar;
    private PosterAdapter mPosterAdapter;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        GridView mGridView = (GridView) rootView.findViewById(R.id.gv_movie_posters);

        mPosterAdapter = new PosterAdapter(getContext(), new ArrayList<Movie>());
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.pb_movies_list);

        mGridView.setAdapter(mPosterAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = mPosterAdapter.getItem(position);

                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, movie);

                startActivity(intent);
            }
        });

        pref = PreferenceManager.getDefaultSharedPreferences(getContext());

        return rootView;
    }

    private void updateMovies() {
        FetchMoviesTask moviesTask = new FetchMoviesTask();
        String sortOrder = pref.getString(getString(R.string.pref_sort_by_key), getString(R.string.pref_sort_by_default));

        String title;
        if (sortOrder.equals(getString(R.string.pref_sort_by_default)))
            title = "Most Popular";
        else
            title = "Top Rated";

        getActivity().setTitle(title + " Movies");

        moviesTask.execute(sortOrder);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {
        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(String... params) {
            if (params.length == 0)
                return null;

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String responseJsonStr = null;

            try {
                final String TMDB_BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0] + "?";
                final String API_KEY = "api_key";

                Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null)
                    return null;

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0)
                    return null;

                responseJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error", e);
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                if (responseJsonStr != null)
                    return getPostersFromJson(responseJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> results) {
            if (results != null) {
                mPosterAdapter.clear();
                for (Movie movie :
                        results) {
                    mPosterAdapter.add(movie);
                }
            }

            mProgressBar.setVisibility(View.GONE);
        }

        private List<Movie> getPostersFromJson(String responseJsonStr) throws JSONException {
            final String TMDB_RESULTS = "results";
            final String TMDB_TITLE = "original_title";
            final String TMDB_POSTER_PATH = "poster_path";
            final String TMDB_PLOT_SYNOPSIS = "overview";
            final String TMDB_USER_RATING = "vote_average";
            final String TMDB_RELEASE_DATE = "release_date";

            final String[] IMAGE_SIZE_ARRAY = {"w92", "w154", "w185", "w342", "w500", "w780", "original"};
            final String IMAGE_SIZE = IMAGE_SIZE_ARRAY[2];
            final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/" + IMAGE_SIZE + "/";

            JSONObject moviesJSON = new JSONObject(responseJsonStr);
            JSONArray moviesArray = moviesJSON.getJSONArray(TMDB_RESULTS);

            List<Movie> resultStr = new ArrayList<>();
            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject movie = moviesArray.getJSONObject(i);
                String posterPath = movie.getString(TMDB_POSTER_PATH);
                String posterUrl = IMAGE_BASE_URL + posterPath;

                String title = movie.getString(TMDB_TITLE);
                String plotSynopsis = movie.getString(TMDB_PLOT_SYNOPSIS);
                String userRating = movie.getString(TMDB_USER_RATING);
                String releaseDate = movie.getString(TMDB_RELEASE_DATE);

                Movie movieObj = new Movie(title, posterUrl, plotSynopsis, userRating, releaseDate);

                resultStr.add(movieObj);
            }

            return resultStr;
        }
    }
}
