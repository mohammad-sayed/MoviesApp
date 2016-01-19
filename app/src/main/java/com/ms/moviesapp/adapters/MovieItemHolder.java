package com.ms.moviesapp.adapters;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Mohammad-Sayed-PC on 12/18/2015.
 */
public class MovieItemHolder {

    private ImageView movieImageView;
    private TextView movieTextView;

    public ImageView getMovieImageView() {
        return movieImageView;
    }

    public void setMovieImageView(ImageView mMovieImageView) {
        this.movieImageView = mMovieImageView;
    }

    public TextView getMovieTextView() {
        return movieTextView;
    }

    public void setMovieTextView(TextView movieTextView) {
        this.movieTextView = movieTextView;
    }
}
