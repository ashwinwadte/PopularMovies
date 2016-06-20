package io.github.ashwinwadte.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import io.github.ashwinwadte.popularmovies.adapters.PosterAdapter;
import io.github.ashwinwadte.popularmovies.asynctasks.FetchMoviesTask;
import io.github.ashwinwadte.popularmovies.interfaces.TheMovieDbApi;
import io.github.ashwinwadte.popularmovies.models.Movie;
import io.github.ashwinwadte.popularmovies.models.Movies;
import io.github.ashwinwadte.popularmovies.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieListActivity extends AppCompatActivity {

    SharedPreferences pref;
    Toolbar toolbar;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private ProgressBar mProgressBar;
    private PosterAdapter mPosterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        GridView mGridView = (GridView) findViewById(R.id.gv_movie_posters);

        mPosterAdapter = new PosterAdapter(MovieListActivity.this, new ArrayList<Movie>());
        mProgressBar = (ProgressBar) findViewById(R.id.pb_movies_list);

        mGridView.setEmptyView(findViewById(android.R.id.empty));
        mGridView.setAdapter(mPosterAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = mPosterAdapter.getItem(position);

                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(MovieDetailFragment.ARG_ITEM_ID, movie);
                    MovieDetailFragment fragment = new MovieDetailFragment();
                    fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.movie_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtra(MovieDetailFragment.ARG_ITEM_ID, movie);

                    context.startActivity(intent);
                }
            }
        });

        pref = PreferenceManager.getDefaultSharedPreferences(MovieListActivity.this);


        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateRetrofit();
    }

    private void updateRetrofit() {

        String sortOrder = pref.getString(getString(R.string.pref_sort_by_key), getString(R.string.pref_sort_by_default));

        String title;
        String[] titleArray = getResources().getStringArray(R.array.pref_sort_by_titles);
        String[] sortOrderArray = getResources().getStringArray(R.array.pref_sort_by_values);


        TheMovieDbApi movieDbApi = Utils.getTheMovieDbApiInstance();

        //user chose favorite sort order. Show favorite movies of the user
        if (sortOrder.equals(sortOrderArray[2])) {
            title = titleArray[2];
            FetchMoviesTask task = new FetchMoviesTask(MovieListActivity.this, mPosterAdapter, mProgressBar);
            task.execute();
        } else {

            mProgressBar.setVisibility(View.VISIBLE);
            mPosterAdapter.clear();

            if (sortOrder.equals(sortOrderArray[0]))
                title = titleArray[0];
            else
                title = titleArray[1];

            //fetch movies for respective order
            Call<Movies> call = movieDbApi.getMovies(sortOrder, BuildConfig.THE_MOVIE_DB_API_KEY);

            call.enqueue(new Callback<Movies>() {
                @Override
                public void onResponse(Call<Movies> call, Response<Movies> response) {
                    mPosterAdapter.clear();
                    Movies movies = response.body();

                    for (Movies.Result r :
                            movies.getResults()) {
                        Movie movie = new Movie(String.valueOf(r.getId()), r.getTitle(), r.getPosterPath(), r.getOverview(), String.valueOf(r.getVoteAverage()), r.getReleaseDate());
                        mPosterAdapter.add(movie);
                    }
                    mProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<Movies> call, Throwable t) {
                    mProgressBar.setVisibility(View.GONE);
                }
            });


        }

        toolbar.setTitle(title + " Movies");

    }
}
