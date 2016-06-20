package io.github.ashwinwadte.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import io.github.ashwinwadte.popularmovies.R;
import io.github.ashwinwadte.popularmovies.models.Movie;

/**
 * Created by Ashwin on 05-May-16.
 */
public class PosterAdapter extends ArrayAdapter<Movie> {


    public PosterAdapter(Context context, List<Movie> objects) {
        //we are not using single TextView but an imageView, so second argument is not useful to us
        //it can have any value, we are using 0 here.
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_posters, parent, false);
        }

        ImageView posterImage = (ImageView) convertView.findViewById(R.id.grid_item_poster_image);

        Picasso.with(getContext())
                .load(movie.getPosterUrl())
                .placeholder(R.drawable.ic_placeholder_photo_grey_500_36dp)
                .error(R.drawable.ic_broken_image_grey_400_36dp)
                .noFade()
                .into(posterImage);

        return convertView;
    }
}
