package com.ms.moviesapp.moviesapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ms.moviesapp.moviesapp.R;
import com.ms.moviesapp.moviesapp.entities.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mohammad-Sayed-PC on 12/18/2015.
 */
public class MoviesItemsAdapter extends BaseAdapter {

    private Context mContext;
    private List<Movie> mMoviesList;
    private OnMovieSelectedListener mOnMovieSelectedListener;

    public MoviesItemsAdapter(Context context, List<Movie> moviesList,
                              OnMovieSelectedListener onMovieSelectedListener) {
        this.mContext = context;
        this.mMoviesList = moviesList;
        this.mOnMovieSelectedListener = onMovieSelectedListener;
    }

    @Override
    public int getCount() {
        if (mMoviesList == null)
            return 0;
        return mMoviesList.size();
    }

    @Override
    public Movie getItem(int position) {
        if (mMoviesList == null)
            return null;
        return mMoviesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setItems(List<Movie> movies) {
        this.mMoviesList = movies;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MovieItemHolder movieItemHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
            movieItemHolder = new MovieItemHolder();
            movieItemHolder.setMovieImageView((ImageView) convertView.findViewById(R.id.iv_movie_thumbnail));
            movieItemHolder.setMovieTextView((TextView) convertView.findViewById(R.id.tv_movie_title));
            convertView.setTag(movieItemHolder);
        } else {
            movieItemHolder = (MovieItemHolder) convertView.getTag();
        }

        final Movie movie = mMoviesList.get(position);

        String thumbnailPath = mContext.getString(R.string.movie_api_base_url)
                .concat(mContext.getString(R.string.poster_size_w185))
                .concat(movie.getThumbnail());
        ImageView movieImageView = movieItemHolder.getMovieImageView();
        Picasso.with(mContext).load(thumbnailPath)
                .error(R.drawable.no_thumbnail).into(movieImageView);

        movieItemHolder.getMovieTextView().setText(movie.getTitle());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnMovieSelectedListener != null)
                    mOnMovieSelectedListener.onMovieSelected(movie);
            }
        });
        return convertView;
    }

    public static interface OnMovieSelectedListener {
        public void onMovieSelected(Movie movie);
    }
}
