/**
 * Created by Ashwin on 19-Jun-16
 */

package io.github.ashwinwadte.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import io.github.ashwinwadte.popularmovies.R;
import io.github.ashwinwadte.popularmovies.models.Video;

public class TrailerAdapter extends ArrayAdapter<Video> {
    public TrailerAdapter(Context context, List<Video> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Video video = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_trailers, parent, false);
        }

        TextView tvTrailer = (TextView) convertView.findViewById(R.id.tvTrailerHolder);

        tvTrailer.setText(video.getName());

        return convertView;
    }
}
