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
import io.github.ashwinwadte.popularmovies.models.Review;

public class ReviewAdapter extends ArrayAdapter<Review> {
    public ReviewAdapter(Context context, List<Review> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Review review = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_reviews, parent, false);
        }

        TextView tvReviewer = (TextView) convertView.findViewById(R.id.tvReviewer);
        TextView tvReview = (TextView) convertView.findViewById(R.id.tvReview);

        tvReviewer.setText(review.getAuthor());
        tvReview.setText(review.getContent());
        return convertView;
    }
}
