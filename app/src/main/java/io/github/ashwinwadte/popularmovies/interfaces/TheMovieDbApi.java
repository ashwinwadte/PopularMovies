package io.github.ashwinwadte.popularmovies.interfaces;

import io.github.ashwinwadte.popularmovies.models.Movies;
import io.github.ashwinwadte.popularmovies.models.Reviews;
import io.github.ashwinwadte.popularmovies.models.Videos;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Ashwin on 09-May-16.
 */
public interface TheMovieDbApi {
    @GET("{sort_by}/")
    Call<Movies> getMovies(@Path("sort_by") String sort_by, @Query("api_key") String Api_Key);

    @GET("{id}/reviews")
    Call<Reviews> getReviews(@Path("id") String id, @Query("api_key") String Api_Key);

    @GET("{id}/videos")
    Call<Videos> getVideos(@Path("id") String id, @Query("api_key") String Api_Key);
}
