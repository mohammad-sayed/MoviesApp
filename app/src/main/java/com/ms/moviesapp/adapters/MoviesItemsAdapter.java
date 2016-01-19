package com.ms.moviesapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ms.moviesapp.R;
import com.ms.moviesapp.entities.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mohammad-Sayed-PC on 12/18/2015.
 */
public class MoviesItemsAdapter extends BaseAdapter {

    private Context mContext;
    private List<Movie> mMoviesList;
    private int mImageWidth;
    private int mImageHeight;

    public MoviesItemsAdapter(Context context, List<Movie> moviesList) {
        this.mContext = context;
        this.mMoviesList = moviesList;
        /*this.mImageWidth = imageWidth;
        this.mImageHeight = (imageWidth
                * (int) context.getResources().getDimension(R.dimen.image_height_dp)
                / (int) context.getResources().getDimension(R.dimen.image_width_dp));*/
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
            mImageWidth = parent.getWidth() / mContext.getResources().getInteger(R.integer.number_of_columns);
            mImageHeight = mImageWidth
                    * (int) mContext.getResources().getDimension(R.dimen.image_height_dp)
                    / (int) mContext.getResources().getDimension(R.dimen.image_width_dp);
            movieItemHolder.getMovieImageView().getLayoutParams().width = mImageWidth;
            movieItemHolder.getMovieImageView().getLayoutParams().height = mImageHeight;
            movieItemHolder.getMovieTextView().getLayoutParams().width = mImageWidth;
            convertView.setTag(movieItemHolder);
        } else {
            movieItemHolder = (MovieItemHolder) convertView.getTag();
        }

        final Movie movie = mMoviesList.get(position);

        String posterPath = mContext.getString(R.string.movie_api_base_url)
                .concat(mContext.getString(R.string.poster_size_w185))
                .concat(movie.getPosterPath());
        ImageView movieImageView = movieItemHolder.getMovieImageView();
        Picasso.with(mContext).load(posterPath).error(R.drawable.no_poster)
                .into(movieImageView);
        movieItemHolder.getMovieTextView().setText(movie.getTitle());
        return convertView;
    }
}
