package com.ms.moviesapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.ms.moviesapp.R;

/**
 * Created by Mohammad-Sayed-PC on 12/18/2015.
 */
public class MovieRecyclerViewHolder extends RecyclerView.ViewHolder {

    private ImageView mMovieImageView;

    public MovieRecyclerViewHolder(View itemView) {
        super(itemView);
        mMovieImageView = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
    }

    public ImageView getMovieImageView() {
        return mMovieImageView;
    }

    public void setmMovieImageView(ImageView mMovieImageView) {
        this.mMovieImageView = mMovieImageView;
    }
}
