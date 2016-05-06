package io.github.ashwinwadte.popularmovies.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.github.ashwinwadte.popularmovies.R;
import io.github.ashwinwadte.popularmovies.models.Movie;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    ImageView mPosterImage;
    TextView mTitle, mReleaseDate, mUserRating, mPlotSynopsis;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Movie movie = null;

        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            movie = (Movie) intent.getSerializableExtra(Intent.EXTRA_TEXT);
        }

        initWidgets(rootView);
        populateWidgets(movie);
        return rootView;
    }

    private void initWidgets(View rootView) {
        mPosterImage = (ImageView) rootView.findViewById(R.id.iv_poster);
        mTitle = (TextView) rootView.findViewById(R.id.tvTitle);
        mReleaseDate = (TextView) rootView.findViewById(R.id.tvReleaseDate);
        mUserRating = (TextView) rootView.findViewById(R.id.tvUserRating);
        mPlotSynopsis = (TextView) rootView.findViewById(R.id.tvPlotSynopsis);
    }

    private void populateWidgets(Movie movie) {
        String formattedUserRating = movie.getmUserRating() + "/10";

        Picasso.with(getContext()).load(movie.getmPosterUrl()).into(mPosterImage);
        mTitle.setText(movie.getmTitle());
        mReleaseDate.setText(movie.getmReleaseDate());
        mUserRating.setText(formattedUserRating);
        mPlotSynopsis.setText(movie.getmPlotSynopsis());
    }
}
