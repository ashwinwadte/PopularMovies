package io.github.ashwinwadte.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import io.github.ashwinwadte.popularmovies.adapters.ReviewAdapter;
import io.github.ashwinwadte.popularmovies.adapters.TrailerAdapter;
import io.github.ashwinwadte.popularmovies.asynctasks.AddFavoriteMovieTask;
import io.github.ashwinwadte.popularmovies.asynctasks.CheckFavoriteMovieTask;
import io.github.ashwinwadte.popularmovies.interfaces.TheMovieDbApi;
import io.github.ashwinwadte.popularmovies.models.Movie;
import io.github.ashwinwadte.popularmovies.models.Review;
import io.github.ashwinwadte.popularmovies.models.Reviews;
import io.github.ashwinwadte.popularmovies.models.Video;
import io.github.ashwinwadte.popularmovies.models.Videos;
import io.github.ashwinwadte.popularmovies.utils.Constants;
import io.github.ashwinwadte.popularmovies.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    static final ButterKnife.Setter<TextView, String[]> TEXT_VIEW_SETTER = new ButterKnife.Setter<TextView, String[]>() {
        @Override
        public void set(@NonNull TextView view, String[] value, int index) {
            view.setText(value[index]);
        }
    };
    @BindView(R.id.iv_poster)
    ImageView mPosterImage;
    @BindViews({R.id.tvTitle, R.id.tvReleaseDate, R.id.tvUserRating, R.id.tvPlotSynopsis})
    List<TextView> mMovieDetails;
    @BindView(R.id.cbFavorite)
    CheckBox cbFavorite;
    @BindView(R.id.lvReviews)
    ListView lvReviews;
    @BindView(R.id.lvTrailers)
    ListView lvTrailers;
    private ReviewAdapter mReviewAdapter;
    private TrailerAdapter mTrailerAdapter;
    private Movie mMovie;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @OnCheckedChanged(R.id.cbFavorite)
    void onChecked(boolean checked) {

        if (checked) {
            AddFavoriteMovieTask task = new AddFavoriteMovieTask(getContext(), true, cbFavorite);
            task.execute(mMovie);
        } else {
            AddFavoriteMovieTask task = new AddFavoriteMovieTask(getContext(), false, cbFavorite);
            task.execute(mMovie);
        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mMovie = getArguments().getParcelable(ARG_ITEM_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);

        ButterKnife.bind(this, rootView);

        if (mMovie != null) {
            populateWidgets(mMovie, rootView);
            fetchReviewsAndTrailers(mMovie);
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        CheckFavoriteMovieTask task = new CheckFavoriteMovieTask(getContext(), cbFavorite);
        task.execute(mMovie);
    }

    private void populateWidgets(Movie movie, View rootView) {
        String formattedUserRating = movie.getUserRating() + "/10";

        Picasso.with(getContext())
                .load(movie.getPosterUrl())
                .placeholder(R.drawable.ic_placeholder_photo_grey_500_36dp)
                .error(R.drawable.ic_broken_image_grey_400_36dp)
                .into(mPosterImage);

        String[] texts = {movie.getTitle(), movie.getReleaseDate(), formattedUserRating, movie.getPlotSynopsis()};
        ButterKnife.apply(mMovieDetails, TEXT_VIEW_SETTER, texts);


        //initialize adapters
        mReviewAdapter = new ReviewAdapter(getContext(), new ArrayList<Review>());
        mTrailerAdapter = new TrailerAdapter(getContext(), new ArrayList<Video>());

        //set empty views
        lvReviews.setEmptyView(rootView.findViewById(R.id.empty));
        lvTrailers.setEmptyView(rootView.findViewById(android.R.id.empty));

        //set the adapters in the list views
        lvReviews.setAdapter(mReviewAdapter);
        lvTrailers.setAdapter(mTrailerAdapter);

        lvTrailers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Video video = mTrailerAdapter.getItem(position);

                String videoUrl = Constants.YOUTUBE_BASE_URL + video.getKey();
                String alternateVideoUrl = Constants.YOUTUBE_URI + video.getKey();

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));

                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(alternateVideoUrl));

                    startActivity(intent);
                }
            }
        });
    }

    private void fetchReviewsAndTrailers(Movie movie) {
        String id = movie.getId();

        TheMovieDbApi theMovieDbApi = Utils.getTheMovieDbApiInstance();

        Call<Reviews> reviewsCall = theMovieDbApi.getReviews(id, BuildConfig.THE_MOVIE_DB_API_KEY);

        reviewsCall.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                mReviewAdapter.clear();

                Reviews reviews = response.body();

                for (Reviews.Result r :
                        reviews.getResults()) {
                    Review review = new Review(r.getAuthor(), r.getContent());
                    mReviewAdapter.add(review);
                }
            }

            @Override
            public void onFailure(Call<Reviews> call, Throwable t) {

            }
        });

        Call<Videos> videosCall = theMovieDbApi.getVideos(id, BuildConfig.THE_MOVIE_DB_API_KEY);

        videosCall.enqueue(new Callback<Videos>() {
            @Override
            public void onResponse(Call<Videos> call, Response<Videos> response) {
                mTrailerAdapter.clear();

                Videos videos = response.body();

                for (Videos.Result r :
                        videos.getResults()) {
                    Video video = new Video(r.getName(), r.getKey());
                    mTrailerAdapter.add(video);
                }

                final String YOUTUBE_TRAILER = Constants.YOUTUBE_BASE_URL + mTrailerAdapter.getItem(0).getKey();

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor spe = sp.edit();
                spe.putString(getString(R.string.pref_trailer_link_key), YOUTUBE_TRAILER);
                spe.apply();
            }

            @Override
            public void onFailure(Call<Videos> call, Throwable t) {

            }
        });
    }
}
