package io.github.ashwinwadte.popularmovies.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.github.ashwinwadte.popularmovies.interfaces.TheMovieDbApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ashwin on 06-May-16.
 */
public class Utils {
    public static String formatDate(String date) {
        final String DATE_FORMAT = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.US);

        try {
            Date parsedDate = format.parse(date);
            return DateFormat.getDateInstance().format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(Constants.TMDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static TheMovieDbApi getTheMovieDbApiInstance(){
        Retrofit retrofit = getRetrofitInstance();
        return retrofit.create(TheMovieDbApi.class);
    }
}
