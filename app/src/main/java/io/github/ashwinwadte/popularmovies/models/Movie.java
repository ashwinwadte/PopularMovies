package io.github.ashwinwadte.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import io.github.ashwinwadte.popularmovies.utils.Utils;

/**
 * Created by Ashwin on 05-May-16.
 */
public class Movie implements Parcelable {
    public static final Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    String id;
    String title;
    String posterUrl;
    String plotSynopsis;
    String userRating;
    String releaseDate;

    public Movie(Parcel source) {
        this.id = source.readString();
        this.title = source.readString();
        this.posterUrl = "http://image.tmdb.org/t/p/w185/" + source.readString();
        this.plotSynopsis = source.readString();
        this.userRating = source.readString();
        this.releaseDate = source.readString();

    }


    public Movie(String id, String title, String posterUrl, String plotSynopsis, String userRating, String releaseDate) {
        this.id = id;
        this.title = title;
        this.posterUrl = "http://image.tmdb.org/t/p/w185/" + posterUrl;
        this.plotSynopsis = plotSynopsis;
        this.userRating = userRating;
        this.releaseDate = Utils.formatDate(releaseDate);

    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public String getUserRating() {
        return userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(posterUrl);
        dest.writeString(plotSynopsis);
        dest.writeString(userRating);
        dest.writeString(releaseDate);
    }
}
