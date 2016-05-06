package io.github.ashwinwadte.popularmovies.models;

import java.io.Serializable;

import static io.github.ashwinwadte.popularmovies.utils.Utils.formatDate;

/**
 * Created by Ashwin on 05-May-16.
 */
public class Movie implements Serializable {
    String mTitle;
    String mPosterUrl;
    String mPlotSynopsis;
    String mUserRating;
    String mReleaseDate;

    public Movie(String mTitle, String mPosterUrl, String mPlotSynopsis, String mUserRating, String mReleaseDate) {
        this.mTitle = mTitle;
        this.mPosterUrl = mPosterUrl;
        this.mPlotSynopsis = mPlotSynopsis;
        this.mUserRating = mUserRating;
        this.mReleaseDate = formatDate(mReleaseDate);
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmPosterUrl() {
        return mPosterUrl;
    }

    public String getmPlotSynopsis() {
        return mPlotSynopsis;
    }

    public String getmUserRating() {
        return mUserRating;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }
}
