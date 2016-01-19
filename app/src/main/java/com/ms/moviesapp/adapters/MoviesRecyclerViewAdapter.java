package com.ms.moviesapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ms.moviesapp.R;
import com.ms.moviesapp.entities.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mohammad-Sayed-PC on 12/18/2015.
 */
public class MoviesRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewHolder> {

    private Context mContext;
    private List<Movie> mMoviesList;
    private OnMovieSelectedListener mOnMovieSelectedListener;

    public MoviesRecyclerViewAdapter(Context context, List<Movie> moviesList,
                                     OnMovieSelectedListener onMovieSelectedListener) {
        this.mContext = context;
        this.mMoviesList = moviesList;
        this.mOnMovieSelectedListener = onMovieSelectedListener;
    }

    @Override
    public int getItemCount() {
        if (mMoviesList == null)
            return 0;
        return mMoviesList.size();
    }


    public void setItems(List<Movie> movies) {
        this.mMoviesList = movies;
        notifyDataSetChanged();
        //notifyItemRangeChanged(0, mMoviesList.size());
    }

    @Override
    public MovieRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        MovieRecyclerViewHolder movieRecyclerViewHolder = new MovieRecyclerViewHolder(view);
        return movieRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(MovieRecyclerViewHolder holder, int position) {

        final Movie movie = mMoviesList.get(position);

        String thumbnailPath = mContext.getString(R.string.movie_api_base_url)
                .concat(mContext.getString(R.string.poster_size_w500))
                .concat(movie.getThumbnail());
        ImageView movieImageView = holder.getMovieImageView();
        Picasso.with(mContext).load(thumbnailPath).into(movieImageView);
        movieImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnMovieSelectedListener != null)
                    mOnMovieSelectedListener.onMovieSelected(movie);
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static interface OnMovieSelectedListener {
        public void onMovieSelected(Movie movie);
    }
}
